package nl.hvanderheijden.chapter2

import java.io.File
import java.net.InetAddress

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import org.apache.kafka.clients.producer.KafkaProducer

class Enricher(
                private val servers: String,
                private val validMessages: String,
                private val invalidMessages: String) extends Producer {

  private val producer = new KafkaProducer[String,String](Producer.createConfig(servers))

  private val MAPPER = new ObjectMapper()

  private def writeInvalid: String => Unit = Producer.write(this.producer)(this.invalidMessages)

  private def writeValid: String => Unit = Producer.write(this.producer)(this.validMessages)

  /*
  {
   "event": "CUSTOMER_CONSULTS_ETHPRICE",
   "customer": {"id": "14862768","name": "Snowden, Edward", "ipAddress": "95.31.18.111"},"currency": {"name": "ethereum","price": "RUB"},"timestamp": "2018-09-28T09:09:09Z"}
   */

  def getDataBaseURI: File = new File(Constants.DATABASE_PATH)

  private val client: DatabaseReader = new DatabaseReader.Builder(getDataBaseURI).withCache(new CHMCache()).build();

  override def process(message: String): Unit = {
    try {
      val root: JsonNode = MAPPER.readTree(message)
      val ipAddressNode: JsonNode = root.path("customer").path("ipAddress")

      if(ipAddressNode.isMissingNode) {
        writeInvalid(s"""{"error": "Customer.IP adress is missing"}""")
      } else {

        val ipAddress = InetAddress.getByName(ipAddressNode.textValue())
        val response = client.country(ipAddress)

        val nRoot: ObjectNode = root.deepCopy()
        nRoot.`with`("customer").put("country", response.getCountry.getName)

        writeValid(MAPPER.writeValueAsString(nRoot))
      }

    } catch {
      case e: Exception => {
        this.writeInvalid(s"""{"error": "${e.getClass.getSimpleName} with ${e.getMessage}"}""")
      }
    }
  }
}
