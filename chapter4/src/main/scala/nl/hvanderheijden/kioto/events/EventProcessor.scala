package nl.hvanderheijden.kioto.events
import java.util.Properties

import nl.hvanderheijden.kioto.Constants
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.kstream.TimeWindows
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.kstream.Materialized

final class EventProcessor(private val brokers: String) {

  private val props: Properties = new Properties()
  props.put("bootstrap.servers", this.brokers)
  props.put("application.id", "kioto")
  props.put("auto.offset.reset", "latest")
  props.put("commit.interval.ms",30000)


  def process(): Unit = {
    val topology = new StreamsBuilder
    val stream = topology.stream[String, String](Constants.getEventsTopic)

    val aggregates = stream.groupBy((k, v) => "foo").windowedBy(TimeWindows.of(10000L)).count()
    aggregates.toStream.map((ws, i) => (s"${ws.window().start()}", s"${i}")).to(Constants.getAggregatesTopic)

    val streams = new KafkaStreams(topology.build(), props)
    streams.start()
  }



}
