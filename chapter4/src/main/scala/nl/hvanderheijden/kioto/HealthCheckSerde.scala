package nl.hvanderheijden.kioto

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class HealthCheckSerde  extends Serde[HealthCheck]{

  override def serializer(): Serializer[HealthCheck] = new HealthCheckSerializer

  override def deserializer(): Deserializer[HealthCheck] = new HealthCheckDeserializer
}
