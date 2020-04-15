package nl.hvanderheijden.kioto

import java.util.logging.Logger

import nl.hvanderheijden.kioto.consumers.{CustomStreamsProcessor, PlainProcessor, PlainStreamsProcessor}
import nl.hvanderheijden.kioto.events.{EventProcessor, EventProducer}
import nl.hvanderheijden.kioto.producers.{CustomProducer, PlainProducer}
import org.apache.commons.cli.{DefaultParser, Options, UnrecognizedOptionException}

object Main extends Object with App {
  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  val options = new Options

  val HOST_KEY = "h"
  val TYPE_KEY = "t"

  options.addOption(HOST_KEY, "host", true, "set the host of kafka")
  options.addOption(TYPE_KEY, "type", true, "set the program function")

  val parser = new DefaultParser

  try {
    val cmd = parser.parse(options, args);

    if (cmd.hasOption(HOST_KEY) && cmd.hasOption(TYPE_KEY)) {
      val host = cmd.getOptionValue(HOST_KEY)
      logger.info(s"Setting host ${host}")

      cmd.getOptionValue(TYPE_KEY).toLowerCase match {
        case "producer" => new CustomProducer(host).produce(2)
        case "events" => new EventProducer(host).produce()
        case "events-processor" => new EventProcessor(host).process()
        case "uptime" => new CustomStreamsProcessor(host).process()
        case _ => logger.severe(s"Unknown command")
      }


    }
  } catch {
    case e: UnrecognizedOptionException => logger.severe("Please use --host <hostname> and --type <producer|events|events-processor|uptime>")
  }

}

