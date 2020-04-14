package nl.hvanderheijden.chapter2
import java.util.logging.Logger

import nl.hvanderheijden.chapter2.core.{Producer, Reader}
import nl.hvanderheijden.chapter2.producers.{Enricher, Validator, Writer}

object Main {

  val logger: Logger = Logger.getLogger(Main.getClass.getName)

  def main(args: Array[String]): Unit = {
    this.logger.info("Starting monedero")

    // TODO: make sure its wrapped in a maybe.
    val servers: String = "localhost:29092"
    val groupId: String = "foo"
    val inputTopic: String = "input-topic"
    val validTopic: String = "valid-messages"
    val invalidTopic: String = "invalid-messages"

    def getProducer(arg: String): Producer = {
      arg.toLowerCase match {
        case "enricher" => new Enricher(servers, validTopic, invalidTopic)
        case "validator" => new Validator(servers, validTopic, invalidTopic)
        case "writer" => new Writer(servers, validTopic)
      }
    }

    val reader = new Reader(servers, groupId, inputTopic)
    val validator = getProducer("validator")
    reader.run(validator)

  }


}
