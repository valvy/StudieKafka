package nl.hvanderheijden.chapter2

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "A " should "Test unit" in {
    Hello.hello should be("Hello")
  }
}
