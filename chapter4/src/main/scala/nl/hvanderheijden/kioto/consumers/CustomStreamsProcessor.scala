package nl.hvanderheijden.kioto.consumers

import java.time.{Duration, LocalDate, Period, ZoneId}
import java.util.Properties

import nl.hvanderheijden.kioto.serdes.HealthCheckSerde
import nl.hvanderheijden.kioto.{Constants, HealthCheck}
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.kstream.Consumed
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}

final class CustomStreamsProcessor(
                                  private val brokers: String
                                  ) {
  val props: Properties = new Properties()
  props.put("bootstrap.servers", this.brokers)
  props.put("application.id", "kioto")

  val topology = new StreamsBuilder
  def process(): Unit = {
    val topology = new StreamsBuilder

    // Todo. FN?
    val customSerde = new HealthCheckSerde

    val healthCheckStream = topology.stream(Constants.getHealthChecksTopic)(Consumed.`with`(Serdes.String, customSerde))

    val uptimeStream = healthCheckStream.map[String, String]( (key , v ) =>  {
      val healthCheck = v
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
