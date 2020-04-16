package nl.hvanderheijden.kioto.consumers

import java.time.{Duration, LocalDate, Period, ZoneId}
import java.util.{Collections, Properties}
import java.util.logging.Logger

import nl.hvanderheijden.kioto.serdes.HealthCheckDeserializer
import nl.hvanderheijden.kioto.{Constants, HealthCheck}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

class CustomConsumer( private val brokers: String) {


  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  private val consumerProps = new Properties()
  consumerProps.put("bootstrap.servers", brokers)
  consumerProps.put("group.id", "healthcheck-processor")
  consumerProps.put("key.deserializer", classOf[StringDeserializer])
  consumerProps.put("value.deserializer", classOf[HealthCheckDeserializer])

  private val consumer = new KafkaConsumer[String, HealthCheck](consumerProps)

  private val producerProps = new Properties()
  producerProps.put("bootstrap.servers", brokers)
  producerProps.put("key.serializer", classOf[StringSerializer] )
  producerProps.put("value.serializer", classOf[StringSerializer])
  private val producer = new KafkaProducer[String, String](producerProps)


  def process(): Unit = {
    consumer.subscribe(Collections.singletonList(Constants.getHealthChecksTopic))
    @scala.annotation.tailrec
    def loop(): Unit = {
      val records = consumer.poll(Duration.ofSeconds(1L))
      records.forEach( record => {
        val healthCheck = record.value()
        val upTime = Period.between(
          healthCheck.lastStartedAt.toInstant.atZone(ZoneId.systemDefault()).toLocalDate,
          LocalDate.now()
        ).getDays

        val future = producer.send(new ProducerRecord[String, String](Constants.getUptimesTopic, healthCheck.serialNumber, String.valueOf(upTime)))
        try {
          future.get()
        } catch {
          case e: Exception => logger.severe(s"could not send to kafka ${e.getMessage}")
        }
      })
      loop()
    }
    loop()
  }
}
