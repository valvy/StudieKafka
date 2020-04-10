package nl.hvanderheijden.chapter2

import org.apache.kafka.streams.KafkaStreams

object Hello {
  def main(args: Array[String]) = {
    println(s"${hello} world")

  }

  def hello: String = {
      "Hello"
  }
}

