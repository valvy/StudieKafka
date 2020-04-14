package nl.hvanderheijden.kioto

import java.util

import com.fasterxml.jackson.core.JsonProcessingException
import org.apache.kafka.common.serialization.Serializer

class HealthCheckSerializer extends Serializer[HealthCheck] {

  override def serialize(topic: String, data: HealthCheck): Array[Byte] = {
    if(data == null) {
      null
    } else {
      try {
        Constants.jsonMapper.writeValueAsBytes(data)
      }
      catch {
        case e: JsonProcessingException => null
      }
    }
  }

  override def close(): Unit = {

  }

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}
}
