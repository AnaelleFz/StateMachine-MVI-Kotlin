package com.example.statemachine.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
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

        motionLayout.setTransitionListener(animTransitionListener())

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
        motionLayout.transitionToEnd()
        text_state.setBackgroundColor(resources.getColor(R.color.colorStop))
        text_state.setText(R.string.stop_state)
        btn_stop.visibility = View.GONE
        btn_start.visibility = View.VISIBLE
        btn_reset.visibility = View.GONE
        btn_close.visibility = View.GONE
    }

    private fun renderStartState() {
        motionLayout.transitionToEnd()
        text_state.setText(R.string.start_state)
        text_state.setBackgroundColor(resources.getColor(R.color.colorStart))
        btn_start.visibility = View.GONE
        btn_reset.visibility = View.GONE
        btn_close.visibility = View.GONE
        btn_stop.visibility = View.VISIBLE
    }

    private fun renderInitState() {
        motionLayout.transitionToEnd()
        text_state.setBackgroundColor(resources.getColor(R.color.colorInit))
        text_state.setText(R.string.init_state)
        btn_stop.visibility = View.GONE
        btn_start.visibility = View.GONE
        btn_reset.visibility = View.GONE
        btn_close.visibility = View.GONE
    }

    private fun renderErrorState() {
        motionLayout.transitionToEnd()
        text_state.setText(R.string.error_state)
        text_state.setBackgroundColor(resources.getColor(R.color.colorError))
        btn_stop.visibility = View.GONE
        btn_start.visibility = View.GONE
        btn_reset.visibility = View.VISIBLE
        btn_close.visibility = View.GONE
    }

    private fun renderAlertState() {
        motionLayout.transitionToEnd()
        text_state.setText(R.string.alert_state)
        btn_stop.visibility = View.GONE
        btn_start.visibility = View.GONE
        btn_reset.visibility = View.GONE
        btn_close.visibility = View.VISIBLE
        text_state.setBackgroundColor(resources.getColor(R.color.colorAlert))
    }

    fun stopEventIntent() = btn_stop.clicks()

    fun startEventIntent() = btn_start.clicks()

    fun resetEventIntent() = btn_reset.clicks()

    fun closeEventItent() = btn_close.clicks()

    inner class animTransitionListener : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            // do nothing
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            // do nothing
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            // do nothing
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            motionLayout.transitionToStart()
        }

    }
}