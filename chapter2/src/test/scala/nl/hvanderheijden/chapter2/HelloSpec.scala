package nl.hvanderheijden.chapter2

import java.io.File
import java.net.InetAddress

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.maxmind.db.CHMCache
import com.maxmind.geoip2.{DatabaseReader, WebServiceClient}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.maxmind.geoip2.model.CountryResponse
class HelloSpec extends AnyFlatSpec with Matchers {
  "Producer server" should "Should have properly set server config" in {
    Producer.createConfig("test").getProperty("bootstrap.servers") should be("test")
  }

  "test " should "asdf" in {
    /*val database: File = new File(new Enricher("", "","").getDataBaseURI())//"/Users/heiko/Documents/StudieKafka/chapter2/src/main/resources/GeoLite2-Country.mmdb");
    val client: DatabaseReader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();


    val ipAddress = InetAddress.getByName("128.101.101.101")
    val response = client.country(ipAddress)
    println(response.getCountry.getName)
    client.close()*/
  }
}
