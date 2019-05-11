package com.example.statemachine.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.statemachine.model.Event
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class AlertService : Service() {

    private val alertServiceBinder = AlertServiceBinder()

    private lateinit var alertConsumer: (Event) -> Unit?

    private val alerts = listOf(
        Event.Alert("alert 1", 120)
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
    fun sendAlert() {
        getRandomNumber()
            .map { i -> alerts[i] }
            .flatMap { alert ->
                Single.just(alert)
                    .delay(alert.delayInSecond, TimeUnit.SECONDS)
            }
            .doOnSuccess { alert -> alertConsumer(alert) }
            .repeat()
            .subscribe()
    }

    private fun getRandomNumber(): Single<Int> {
        return Single.fromCallable {
            (0 until alerts.size).random()
        }
    }

    /**
     * Send event error every minute to alertConsumer
     */
    private fun sendErrorEveryMinute() {
        getError()
            .delay(
                600, TimeUnit.SECONDS
            )
            .doOnNext { error -> alertConsumer(error) }
            .repeat()
            .subscribe()
    }

    private fun getError(): Observable<Event> {
        return Observable.just(Event.Error())
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlertService {
            return this@AlertService
        }
    }
}