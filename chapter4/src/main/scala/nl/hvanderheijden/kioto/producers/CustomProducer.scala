package nl.hvanderheijden.kioto.producers

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.github.javafaker.Faker
import nl.hvanderheijden.kioto.{Constants, HealthCheck, HealthCheckSerializer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

class CustomProducer(
                    private val broker: String
                    ) {

  private val props = new Properties()
  props.put("bootstrap.servers", broker)
  props.put("key.serializer", classOf[StringSerializer])
  props.put("value.serializer", classOf[HealthCheckSerializer])

  private val producer = new KafkaProducer[String,HealthCheck](props)

  def produce(ratePerSecond: Int): Unit = {
    val waitTimeBetweenIterationMs = 1000L / ratePerSecond
    val faker = new Faker()
    @scala.annotation.tailrec
    def loop(): Unit = {
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
      // Instead of serializing it to a string.. now immediately to bytes
      val futureResult = producer.send(new ProducerRecord[String, HealthCheck](Constants.getHealthChecksTopic, fakeHealthCheck))

      Thread.sleep(waitTimeBetweenIterationMs)
      futureResult.get()

      loop()
    }
    loop()
  }


}
