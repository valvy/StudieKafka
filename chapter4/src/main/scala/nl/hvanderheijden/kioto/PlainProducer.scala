package nl.hvanderheijden.kioto

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.github.javafaker.Faker
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

final class PlainProducer(
                    private val brokers: String
                    ) {
  private val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("key.serializer", classOf[StringSerializer] )
  props.put("value.serializer", classOf[StringSerializer])

  private val producer: Producer[String, String] = new KafkaProducer[String, String](props)

  def produce(ratePerSecond: Int): Unit = {
    val waitTimeBetweenIterationMs = 1000L / ratePerSecond
    val faker = new Faker()
    def loop: Unit = {

      val fakeHealthCheck =  HealthCheck(
      "HEALTH_CHECK",
        faker.address().city(),
        faker.bothify("??##-??##", true),
        Constants.MachineType(faker.number().numberBetween(0,4)).toString,
        Constants.MachineStatus(faker.number().numberBetween(0,3)).toString,
        faker.date().past(100, TimeUnit.DAYS),
        faker.number().numberBetween(100L, 0L),
        faker.internet().ipV4Address()
      )
      val fakeHealthCheckJson = Constants.jsonMapper.writeValueAsString(fakeHealthCheck)

      val futureResult = producer.send(new ProducerRecord[String, String](Constants.getHealthChecksTopic, fakeHealthCheckJson))
      Thread.sleep(waitTimeBetweenIterationMs)
      futureResult.get()
      loop
    }
    loop
  }


}
