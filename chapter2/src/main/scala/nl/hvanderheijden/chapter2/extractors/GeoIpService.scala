package nl.hvanderheijden.chapter2.extractors

import java.io.File
import java.net.InetAddress

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader

class GeoIpService {

  private val DATABASE_PATH = "/Users/heiko/Documents/StudieKafka/chapter2/src/main/resources/GeoLite2-Country.mmdb"
  private def getDataBaseURI: File = new File(DATABASE_PATH)

  private val client: DatabaseReader = new DatabaseReader.Builder(getDataBaseURI).withCache(new CHMCache()).build();

  def getLocation(ipAdress: String): Option[String] = {
    val ipAddress = InetAddress.getByName(ipAdress)
    val response = client.country(ipAddress)
    if(response != null) {
      Option(response.getCountry.getName)
    } else {
      None
    }

  }

}
