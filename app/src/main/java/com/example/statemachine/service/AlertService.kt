package com.example.statemachine.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.statemachine.model.EventEnum
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class AlertService : Service() {

    private val alertServiceBinder = AlertServiceBinder()

    private lateinit var alertConsumer: (EventEnum) -> Unit

    private val alerts = listOf(
        Alert(EventEnum.ALERT, "alert 1", 10),
        Alert(EventEnum.ALERT, "alert 2", 20),
        Alert(EventEnum.ALERT, "alert 3", 30),
        Alert(EventEnum.ALERT, "alert 4", 40),
        Alert(EventEnum.ALERT, "alert 5", 50),
        Alert(EventEnum.ALERT, "alert 6", 60)
    )

    override fun onBind(intent: Intent?): IBinder? {
        sendAlert()
        return alertServiceBinder
    }

    fun setAlertConsumer(alertConsumer: (EventEnum) -> Unit) {
        this.alertConsumer = alertConsumer
    }

    /**
     * Get alert from alerts randomly.
     * Add a delay equals to alert.delayInSecond.
     * Finally pass alert.event to alertConsumer.
     *
     */
    fun sendAlert() {
        getRandomNumber()
            .map { i -> alerts[i] }
            .flatMap { alert ->
                Single.just(alert)
                    .delay(alert.delayInSecond, TimeUnit.SECONDS)
            }
            .doOnSuccess { alert -> alertConsumer(alert.event) }
            .repeat()
            .subscribe()
    }

    private fun getRandomNumber(): Single<Int> {
        return Single.fromCallable {
            (0 until alerts.size).random()
        }
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlertService {
            return this@AlertService
        }
    }
}

data class Alert(val event: EventEnum, val desc: String, val delayInSecond: Long)