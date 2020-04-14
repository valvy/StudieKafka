package nl.hvanderheijden.chapter2
import java.util.logging.Logger

import nl.hvanderheijden.chapter2.core.{Producer, Reader}
import nl.hvanderheijden.chapter2.producers.{Enricher, Validator, Writer}

import scala.Option

object Main {

  val logger: Logger = Logger.getLogger(Main.getClass.getName)



  def main(args: Array[String]): Unit = {
    this.logger.info("Starting monedero")

    def getFromItem(element: Int): Option[String] = {
      if(args.length > element) {
          Some(args(element))
      } else {
        None
      }
    }

    // TODO: make sure its wrapped in a maybe.
    val servers: String = getFromItem(0).getOrElse( "localhost:29092")

    val groupId: String = getFromItem(1).getOrElse("foo")
    val inputTopic: String = getFromItem(2).getOrElse("input-topic")
    val validTopic: String = getFromItem(3).getOrElse("valid-messages")
    val invalidTopic: String = getFromItem(4).getOrElse("invalid-messages")

    def getProducer(arg: String): Option[Producer] = {
      arg.toLowerCase match {
        case "enricher" => Some(new Enricher(servers, validTopic, invalidTopic))
        case "validator" => Some(new Validator(servers, validTopic, invalidTopic))
        case "writer" => Some(new Writer(servers, validTopic))
        case _ => None
      }
    }

    val producer = getFromItem(5).getOrElse("writer")

    val reader = new Reader(servers, groupId, inputTopic)
    getProducer(producer) match {
      case Some(x) => {
        logger.info(s"using producer ${ producer}")
        reader.run(x)

      }
      case _ => logger.info("could not recognise the producer.")
    }
  }


}
