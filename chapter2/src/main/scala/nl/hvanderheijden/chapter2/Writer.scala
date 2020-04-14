package nl.hvanderheijden.chapter2

import org.apache.kafka.clients.producer.KafkaProducer

class Writer(val servers: String, val topic: String ) extends Producer {

  val producer = new KafkaProducer[String, String](Producer.createConfig(servers))

  override def process(message: String): Unit = Producer.write(this.producer, this.topic, message)

}
