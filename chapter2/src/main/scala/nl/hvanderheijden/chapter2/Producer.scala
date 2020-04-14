package nl.hvanderheijden.chapter2

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

trait Producer {

  def process(message: String): Unit
}

object Producer{
  def write(
             producer: KafkaProducer[String,String],
             topic: String,
             message: String
           ): Unit = {
      producer.send(new ProducerRecord[String,String](topic, message))
  }

  def createConfig(servers: String): Properties = {
    val config = new Properties()
    config.put("bootstrap.servers", servers)
    config.put("acks", "all")
    config.put("retries", 0)
    config.put("batch.size", 1000)
    config.put("linger.ms", 1)
    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    config
  }
}
