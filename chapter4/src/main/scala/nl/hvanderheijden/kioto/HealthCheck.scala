package nl.hvanderheijden.kioto

import java.util.Date

case class HealthCheck(
                  event: String,
                  factory: String,
                  serialNumber: String,
                  typ: String,
                  status: String,
                  lastStartedAt: Date,
                  temperature: Float,
                  ipAddress: String
                 ) {
}
