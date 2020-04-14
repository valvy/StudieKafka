package nl.hvanderheijden.chapter2.producers

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import nl.hvanderheijden.chapter2.core.Producer
import nl.hvanderheijden.chapter2.extractors.{GeoIpService, OpenExhangeService}
import org.apache.kafka.clients.producer.KafkaProducer

class Enricher(
                private val servers: String,
                private val validMessages: String,
                private val invalidMessages: String) extends Producer {

  private val producer = new KafkaProducer[String,String](Producer.createConfig(servers))

  private val MAPPER = new ObjectMapper()

  private val geoIpService = new GeoIpService

  private val openExhangeService = new OpenExhangeService

  private def writeInvalid: String => Unit = Producer.write(this.producer)(this.invalidMessages)

  private def writeValid: String => Unit = Producer.write(this.producer)(this.validMessages)



  /*
  {"event": "CUSTOMER_CONSULTS_ETHPRICE", "customer": {"id": "14862768","name": "Snowden, Edward", "ipAddress": "95.31.18.111"},"currency": {"name": "ethereum","price": "RUB"},"timestamp": "2018-09-28T09:09:09Z"}
   */


  override def process(message: String): Unit = {
    try {
      val root: JsonNode = MAPPER.readTree(message)
      val ipAddressNode: JsonNode = root.path("customer").path("ipAddress")

      if(ipAddressNode.isMissingNode) {
        writeInvalid(s"""{"error": "Customer.IP adress is missing"}""")
      } else {
        val currencyNode: JsonNode = root.path("currency").path("price")
        if(currencyNode.isMissingNode) {
          writeInvalid(s"""{"error": "currency.price is missing"}""")
        } else {


          val nRoot: ObjectNode = root.deepCopy()


          nRoot.`with`("customer").put("country",
            geoIpService.getLocation(ipAddressNode.textValue()).getOrElse("null")
          )

          nRoot.`with`("currency").put("exchangeRate",
            s"${openExhangeService.getPrice(currencyNode.textValue()).getOrElse(0)}"
          )

          writeValid(MAPPER.writeValueAsString(nRoot))
        }
      }

    } catch {
      case e: Exception => {
        this.writeInvalid(s"""{"error": "${e.getClass.getSimpleName} with ${e.getMessage}"}""")
      }
    }
  }
}
