package nl.hvanderheijden.chapter2

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "A " should "Test unit" in {


    val MAPPER: ObjectMapper = new ObjectMapper()
    val root: JsonNode = MAPPER.readTree("{ \"event\": \"CUSTOMER_CONSULTS_ETHPRICE\", \"customer\": \"test\" , \"currency\":\"pizza\" , \"timestamp\": \"1\"}")

    /**
     * def getRequiredParameters(key: String*): String = key match {
     * case Seq(x) if root.has(x) => ""
     * case Seq(x) if !root.has(x) => s"The key [${key.head}] is missing. ".concat(getRequiredParameters(key.tail:_*))
     * case Seq(x, _) if root.has(x) => getRequiredParameters(key.tail:_*)
     * case Seq(x, _) if !root.has(x) => s"The key [${key.head}] is missing. ".concat(getRequiredParameters(key.tail:_*))
     * }
     *
     * @param key
     * @return
     */

    def getRequiredParameters(key: Seq[String]): String = key match {
      case Seq(x) if root.has(x) => ""
      case Seq(x) if !root.has(x) => s"The key [${x}] is missing. "
      case Seq(x:String, xs@_*) => s"${getRequiredParameters(key.tail)} FOADKLSJFADSKLFJADSFKLJA"
      case _ => "FOCK"
      /*case Seq(x) if root.has(x) => ""
      case Seq(x) if !root.has(x) => s"The key [${key.head}] is missing. ".concat(getRequiredParameters(key.tail:_*))
      case Seq(x, _) if root.has(x) => getRequiredParameters(key.tail:_*)
      case Seq(x, _) if !root.has(x) => s"The key [${key.head}] is missing. ".concat(getRequiredParameters(key.tail:_*))*/
    }

    val errors = getRequiredParameters(Seq("event", "customer", "currency", "timestamp"))

    println(errors)
  }
}
