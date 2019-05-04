package com.example.statemachine.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.statemachine.model.statemachine.EventEnum
import io.reactivex.Observer

class AlertService : Service() {

    private val alertServiceBinder = AlertServiceBinder()

    private lateinit var alertObserver: Observer<EventEnum>

    override fun onBind(intent: Intent?): IBinder? {
        return alertServiceBinder
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    fun setAlertObserver(alertObserver: Observer<EventEnum>) {
        this.alertObserver = alertObserver
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlertService {
            return this@AlertService
        }
    }
}