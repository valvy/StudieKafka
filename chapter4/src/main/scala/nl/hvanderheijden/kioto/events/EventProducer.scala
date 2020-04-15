package nl.hvanderheijden.kioto.events

import java.util.logging.Logger
import java.util.{Properties, Timer, TimerTask}

import nl.hvanderheijden.kioto.Constants
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

final class EventProducer(
                         private val brokers: String
                         ) {

    private val logger: Logger = Logger.getLogger(this.getClass.getName)
    private val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("key.serializer", classOf[StringSerializer])
    props.put("value.serializer", classOf[StringSerializer])

    private val producer = new KafkaProducer[String, String](props)

    def produce(): Unit = {
      val now = System.currentTimeMillis()
      val delay = 1300 - Math.floorMod(now, 1000)
      val timer = new Timer()
      timer.schedule(new TimerTask {
        override def run(): Unit = {
          val ts = System.currentTimeMillis()
          val second = Math.floorMod(ts / 1000, 60)
          if(second != 54) {
            EventProducer.this.sendMessage(second, ts, "on time")
          }
          if (second == 6) {
            EventProducer.this.sendMessage(54, ts - 12000, "late" )
          }
        }
      }, delay, 1000)

    }

    private def sendMessage(id: Long, ts: Long, info: String): Unit = {
      val window =ts / 10000 * 10000
      val value = s"${window},${id}, ${info}"
      val futureResult = this.producer.send(
        new ProducerRecord(Constants.getEventsTopic, null, ts, String.valueOf(id), value)
      )
      try {
        futureResult.get()
      } catch {
        case e: Exception => this.logger.warning(s"Could not send event ${e.getMessage}")
      }
    }

}
