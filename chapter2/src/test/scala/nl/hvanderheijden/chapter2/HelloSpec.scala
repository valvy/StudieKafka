package nl.hvanderheijden.chapter2
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import nl.hvanderheijden.chapter2.core.Producer

class HelloSpec extends AnyFlatSpec with Matchers {
  "Producer server" should "Should have properly set server config" in {
    Producer.createConfig("test").getProperty("bootstrap.servers") should be("test")
  }

}
