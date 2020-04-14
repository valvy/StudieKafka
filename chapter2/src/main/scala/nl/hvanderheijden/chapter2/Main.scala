package nl.hvanderheijden.chapter2
import java.util.logging.Logger

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


    //val sourceTopic: String = "input-topic"// args(2)
    //val targetTopic: String = "output-topic"//args(

    val reader = new Reader(servers, groupId, inputTopic)
    val validator = new Validator(servers, validTopic, invalidTopic)
    reader.run(validator)
  }


}
