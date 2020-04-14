package nl.hvanderheijden.kioto

import java.util

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
