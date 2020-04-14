package nl.hvanderheijden.chapter2

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "Producer server" should "Should have properly set server config" in {
    Producer.createConfig("test").getProperty("bootstrap.servers") should be("test")
  }
}
