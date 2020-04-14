package nl.hvanderheijden.kioto

import java.time.{Duration, LocalDate, Period, ZoneId}
import java.util.logging.Logger
import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

final class PlainProcessor (
                           private val brokers: String
                           ) {


  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  private val consumerProps = new Properties()
  consumerProps.put("bootstrap.servers", brokers)
  consumerProps.put("group.id", "healthcheck-processor")
  consumerProps.put("key.deserializer", classOf[StringDeserializer])
  consumerProps.put("value.deserializer", classOf[StringDeserializer])

  private val consumer = new KafkaConsumer[String, String](consumerProps)

  private val producerProps = new Properties()
  producerProps.put("bootstrap.servers", brokers)
  producerProps.put("key.serializer", classOf[StringSerializer] )
  producerProps.put("value.serializer", classOf[StringSerializer])
  private val producer = new KafkaProducer[String, String](producerProps)

  private def parseHealthCheck(value: String): Option[HealthCheck] = {
    try {
      Some(Constants.jsonMapper.readValue(value, classOf[HealthCheck]))
    } catch {
      case e: Exception => None
    }
  }

  def process(): Unit = {
    consumer.subscribe(Collections.singletonList(Constants.getHealthChecksTopic))
    def loop(): Unit = {
      val records = consumer.poll(Duration.ofSeconds(1L))
      records.forEach( record => {
        parseHealthCheck(record.value()) match {
          case Some(healthCheck) => {
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
          }
          case _ => {
            logger.severe("could not parse healthCheck")
          }
        }
      })
      loop()
    }
    loop()
  }
}
