package nl.hvanderheijden.kioto

import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule



object Constants {

  val jsonMapper: ObjectMapper = new ObjectMapper()
  jsonMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
  jsonMapper.registerModule(DefaultScalaModule)
  jsonMapper.setDateFormat(new StdDateFormat())

  def getHealthChecksTopic: String = "healthchecks"
  def getEventsTopic: String = "events"
  def getAggregatesTopic: String= "aggregates"

  def getUptimesTopic: String = "uptimes"

  object MachineType extends Enumeration {
    type MachineType = Value
    val GEOTHERMAL, HYDROELECTRIC, NUCLEAR, WIND, SOLAR = Value
  }

  object MachineStatus extends Enumeration {
    type MachineStatus = Value
    val STARTING, RUNNING, SHUTTING_DOWN, SHUT_DOWN = Value
  }


}
