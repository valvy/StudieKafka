package nl.hvanderheijden.kioto

import java.util.concurrent.TimeUnit

import com.github.javafaker.Faker

object Hello extends Greeting with App {
  new PlainProducer("localhost:29092").produce(2)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
