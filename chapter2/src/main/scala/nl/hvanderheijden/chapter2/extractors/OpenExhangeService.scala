package nl.hvanderheijden.chapter2.extractors

import java.net.URL

import com.fasterxml.jackson.databind.ObjectMapper

class OpenExhangeService {
  val API_KEY: String = ""

  val MAPPER = new ObjectMapper()

  def getPrice(currency: String): Option[Double] = {
    val url: URL = new URL(s"https://openexchangerates.org/api/latest.json?app_id=${API_KEY}")
    try {
      Option(MAPPER.readTree(url).path("rates").path(currency).toString.toDouble)
    }
    catch{
      case e: Exception => None
    }
  }


}
