package nl.hvanderheijden.kioto

import java.time.{Duration, LocalDate, Period, ZoneId}
import java.util.Properties
import java.util.logging.Logger

import org.apache.kafka.streams.kstream.KeyValueMapper
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._

final class PlainStreamsProcessor(private val brokers: String) {
    private val logger: Logger = Logger.getLogger(this.getClass.getName)

    def process(): Unit = {

      val props: Properties = new Properties()
      props.put("bootstrap.servers", this.brokers)
      props.put("application.id", "kioto")

      val topology = new StreamsBuilder


      val healthCheckJsonStream = topology.stream[String, String](Constants.getHealthChecksTopic)

      val healthCheckStream = healthCheckJsonStream.mapValues(v => {
        try {
          Constants.jsonMapper.readValue(v.toString, classOf[HealthCheck])

        } catch {
          case e: Exception => logger.severe(s"could not parse healthcheck ${e.getMessage}")
        }
      })

      val uptimeStream = healthCheckStream.map[String, String]( (key , v ) =>  {
        val healthCheck = v.asInstanceOf[HealthCheck]
        val uptime = Period.between(
          healthCheck.lastStartedAt.
            toInstant.atZone(ZoneId.systemDefault())
            .toLocalDate, LocalDate.now()
        ).getDays
        (healthCheck.serialNumber, uptime.toString)
      })

      uptimeStream.to(Constants.getUptimesTopic)

      val streams: KafkaStreams = new KafkaStreams(topology.build(), props)
      streams.start()

      sys.ShutdownHookThread {
        streams.close(Duration.ofSeconds(10))
      }

    }




}
