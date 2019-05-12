package com.example.statemachine.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.statemachine.model.Event
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class AlertService : Service() {

    private val alertServiceBinder = AlertServiceBinder()

    private lateinit var alertConsumer: (Event) -> Unit?

    private val alerts = listOf(
        Event.Alert("Alert n°1", 5),
        Event.Alert("Alert n°2", 15),
        Event.Alert("Alert n°3", 25),
        Event.Alert("Alert n°4", 35)
    )

    private val errors = listOf(
        Event.Error("Error n°1"),
        Event.Error("Error n°2"),
        Event.Error("Error n°3"),
        Event.Error("Error n°4")
    )

    override fun onBind(intent: Intent?): IBinder? {
        sendAlert()
        sendErrorEveryMinute()
        return alertServiceBinder
    }

    fun setAlertConsumer(alertConsumer: (Event) -> Unit) {
        this.alertConsumer = alertConsumer
    }

    /**
     * Get alert from alerts randomly.
     * Add a delay equals to alert.delayInSecond.
     * Finally pass alert.event to alertConsumer.
     *
     */
    private fun sendAlert() {
        getRandomNumber(alerts.size)
            .map { i -> alerts[i] }
            .flatMap { alert ->
                Single.just(alert)
                    .delay(alert.delayInSecond, TimeUnit.SECONDS)
            }
            .doOnSuccess { alert -> alertConsumer(alert) }
            .repeat()
            .subscribe()
    }

    private fun getRandomNumber(maxSize: Int): Single<Int> {
        return Single.fromCallable {
            (0 until maxSize).random()
        }
    }

    /**
     * Send event error every minute to alertConsumer
     */
    private fun sendErrorEveryMinute() {
        getRandomNumber(errors.size)
            .map { i -> errors[i] }
            .delay(
                60, TimeUnit.SECONDS
            )
            .doOnSuccess { error -> alertConsumer(error) }
            .repeat()
            .subscribe()
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlertService {
            return this@AlertService
        }
    }
}