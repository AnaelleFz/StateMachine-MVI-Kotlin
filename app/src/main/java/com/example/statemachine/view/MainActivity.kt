package com.example.statemachine.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.statemachine.R
import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.MainPresenter
import com.example.statemachine.model.StateEnum
import com.example.statemachine.service.AlertService
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val presenter = MainPresenter()

    private lateinit var alertService: AlertService

    private lateinit var intentAlertService: Intent

    val alertServiceObserver = PublishSubject.create<EventEnum>()

    private val alertServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AlertService.AlertServiceBinder
            alertService = binder.getService()
            alertService.setAlertConsumer { eventEnum -> alertServiceObserver.onNext(eventEnum) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bind alert service
        intentAlertService = Intent(applicationContext, AlertService::class.java)
        bindService(intentAlertService, alertServiceConnection, Context.BIND_AUTO_CREATE)

        // init/bind presenter
        presenter.bind(this)

        renderStopState()
    }

    override fun onDestroy() {
        unbindService(alertServiceConnection)
        presenter.unbind()
        super.onDestroy()
    }

    fun render(stateEnum: StateEnum) {
        when (stateEnum) {
            StateEnum.StopState -> renderStopState()
            StateEnum.InitState -> renderInitState()
            StateEnum.StartState -> renderStartState()
            StateEnum.ErrorState -> renderErrorState()
            StateEnum.AlertState -> renderAlertState()
        }
    }

    private fun renderStopState() {

        text_state.setText(R.string.stop_state)
        btn_stop.visibility = View.INVISIBLE
        btn_start.visibility = View.VISIBLE
    }

    private fun renderStartState() {
        text_state.setText(R.string.start_state)
        btn_stop.visibility = View.VISIBLE
        btn_start.visibility = View.INVISIBLE
    }

    private fun renderInitState() {
        text_state.setText(R.string.init_state)
        btn_stop.visibility = View.INVISIBLE
        btn_start.visibility = View.INVISIBLE

    }

    private fun renderErrorState() {
        text_state.setText(R.string.error_state)
        btn_stop.visibility = View.VISIBLE
        btn_start.visibility = View.INVISIBLE
    }

    private fun renderAlertState() {
        // build alert dialog
        text_state.setText(R.string.alert_state)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Alert received")
        builder.setMessage("Close alert dialog ?")
        builder.setPositiveButton("OK") { dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun stopEventIntent() = btn_stop.clicks()

    fun startEventIntent() = btn_start.clicks()
}