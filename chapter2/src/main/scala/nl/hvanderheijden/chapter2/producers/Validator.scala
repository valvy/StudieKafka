package nl.hvanderheijden.chapter2.producers

import java.util.logging.Logger

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import nl.hvanderheijden.chapter2.core.Producer
import org.apache.kafka.clients.producer.KafkaProducer
// {"event":"CUSTOMER_CONSULTS_ETHPRICE","customer":"test","currency":"pizza","timestamp":"1"}
class Validator(
                 private val servers: String,
                 private val validMessages: String,
                 private val invalidMessages: String ) extends Producer {

  private val producer = new KafkaProducer[String, String](Producer.createConfig(servers))

  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  private val MAPPER: ObjectMapper = new ObjectMapper()


  private def writeInvalid: String => Unit = Producer.write(this.producer)(this.invalidMessages)

  private def writeValid: String => Unit = Producer.write(this.producer)(this.validMessages)



  override def process(message: String): Unit = {
    try {
      val root: JsonNode = MAPPER.readTree(message)
      logger.info(message)

      def getRequiredParameters(key: Seq[String]): String = key match {
        case Seq(x) if root.has(x) => ""
        case Seq(x) if !root.has(x) => s"The key [${x}] is missing. "
        case Seq(x:String, xs@_*) if root.has(x) => s"${getRequiredParameters(key.tail)}"
        case Seq(x:String, xs@_*) if !root.has(x) => s"The key [${x}] is missing. ${getRequiredParameters(key.tail)}"
      }


      val errors = getRequiredParameters(Seq("event", "customer", "currency", "timestamp"))
      logger.info(errors)
      this.logger.info(errors)
      if (errors.length > 0) {
        writeInvalid( s"""{"error": "${errors}"}""")
      }
      else {
        writeValid(MAPPER.writeValueAsString(root))
      }
    } catch {
      case e: Exception => {
        this.logger.warning(s"Could not parse this message ${e.getMessage}")
        writeInvalid( s"""{"error": "${e.getClass.getSimpleName} with ${e.getMessage}"}""")
      }
    }
  }
}
