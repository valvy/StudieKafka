package nl.hvanderheijden.chapter2

import org.apache.kafka.clients.producer.KafkaProducer

class Writer(
              private val servers: String,
              private val topic: String ) extends Producer {

  private val producer = new KafkaProducer[String, String](Producer.createConfig(servers))

  override def process(message: String): Unit = Producer.write(this.producer)(this.topic)(message)

}
