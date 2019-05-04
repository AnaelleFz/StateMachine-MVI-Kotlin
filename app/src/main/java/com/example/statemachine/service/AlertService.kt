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
        Alert(EventEnum.ALERT, "alert 1", 100),
        Alert(EventEnum.ALERT, "alert 2", 150),
        Alert(EventEnum.ALERT, "alert 3", 300)
    )

    override fun onBind(intent: Intent?): IBinder? {
        sendAlertAndError()
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
     * If an error occurred pass EventEnum.Error to alertConsumer
     *
     */
    fun sendAlertAndError() {
        getRandomNumber()
            .map { i -> alerts[i] }
            .flatMap { alert ->
                Single.just(alert)
                    .delay(alert.delayInSecond, TimeUnit.SECONDS)
            }
            // When getRandomNumber throws IndexOutOfBoundsException
            .doOnError { alertConsumer(EventEnum.ERROR) }
            .doOnSuccess { alert -> alertConsumer(alert.event) }
            .repeat()
            .subscribe()
    }

    private fun getRandomNumber(): Single<Int> {
        return Single.fromCallable {
            (0 until alerts.size + 1).random()
        }
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlertService {
            return this@AlertService
        }
    }
}

data class Alert(val event: EventEnum, val desc: String, val delayInSecond: Long)