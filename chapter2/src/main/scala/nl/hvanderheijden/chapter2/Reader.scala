package nl.hvanderheijden.chapter2

import java.time.Duration
import java.util.logging.Logger
import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

class Reader(
              val server: String,
              val groupId: String,
              val topic: String) extends Consumer {

  val logger: Logger = Logger.getLogger(this.getClass.getName)

  val consumer: KafkaConsumer[String, String] =  new KafkaConsumer(Consumer.createConfig(server, groupId))

  def run[K,V](producer: Producer): Unit = {
    logger.info(s"Reading ${server} on groupID ${groupId} with topic ${topic}")
    this.consumer.subscribe(Collections.singletonList(this.topic));
    @scala.annotation.tailrec
    def loop(): Unit = {

      val records: ConsumerRecords[String,String] = this.consumer.poll(Duration.ofMillis(1000))

      records.forEach(x => println(x.value()))
      records.forEach(x =>  producer.process(x.value()))
      loop()
    }
    loop()
  }


  def hello: String = {
      "Hello"
  }
}

