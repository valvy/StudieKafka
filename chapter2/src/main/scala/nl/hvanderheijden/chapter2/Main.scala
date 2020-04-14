package nl.hvanderheijden.chapter2
import java.util.logging.Logger

object Main {

  val logger: Logger = Logger.getLogger(Main.getClass.getName)

  def main(args: Array[String]): Unit = {
    this.logger.info("Starting monedero")

    // TODO: make sure its wrapped in a maybe.
    val servers: String = "localhost:29092"//rgs(0)
    val groupId: String = "foo"//args(1)
    val sourceTopic: String = "input-topic"// args(2)
    val targetTopic: String = "output-topic"//args(3)

    val reader = new Reader(servers, groupId, sourceTopic)
    val writer = new Writer(servers, targetTopic)

    reader.run(writer)
  }


}
