package nl.hvanderheijden.kioto.serdes

import java.util

import nl.hvanderheijden.kioto.{Constants, HealthCheck}
import org.apache.kafka.common.serialization.Deserializer

class HealthCheckDeserializer extends Deserializer[HealthCheck] {

  override def deserialize(topic: String, data: Array[Byte]): HealthCheck = {
    if(data == null) {
      null
    } else {
      try {
        Constants.jsonMapper.readValue(data, classOf[HealthCheck])
      } catch {
        case e: Exception => null
      }
    }

  }


  override def close(): Unit = {}

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

}
