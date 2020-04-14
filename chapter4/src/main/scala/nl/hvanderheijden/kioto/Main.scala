package nl.hvanderheijden.kioto

import java.util.logging.Logger

import nl.hvanderheijden.kioto.producers.{CustomProducer, PlainProducer}

object Main extends Object with App {
  private val logger: Logger = Logger.getLogger(this.getClass.getName)
  if(args.length == 1 ) {
    if (args(0).equals("producer")) {
      new CustomProducer("localhost:29092").produce(2)
    } else {
      new PlainProcessor("localhost:29092").process()
    }
  } else {
    logger.info("Run with producer")

  }


}

