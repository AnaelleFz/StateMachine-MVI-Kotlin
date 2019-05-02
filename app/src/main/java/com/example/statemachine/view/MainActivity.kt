package com.example.statemachine.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.statemachine.R
import com.example.statemachine.model.MainPresenter
import com.example.statemachine.model.statemachine.State
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.unbind()
        super.onDestroy()
    }

    fun render(state: State) {
        when (state) {
            is State.StopState -> renderStopState(state)
            is State.InitState -> renderInitState(state)
            is State.StartState -> renderStartState(state)
            is State.ErrorState -> renderErrorState(state)
            is State.AlertState -> renderAlertState(state)
        }
    }

    private fun renderStopState(dataState: State.StopState) {
        text_state.setText(R.string.stop_state)
        btn_alert.visibility = View.INVISIBLE
        btn_stop.visibility = View.INVISIBLE
        btn_start.visibility = View.VISIBLE
        btn_error.visibility = View.VISIBLE
        btn_close.visibility = View.INVISIBLE
    }

    private fun renderStartState(dataState: State.StartState) {
        text_state.setText(R.string.start_state)
        btn_alert.visibility = View.VISIBLE
        btn_stop.visibility = View.VISIBLE
        btn_start.visibility = View.INVISIBLE
        btn_error.visibility = View.VISIBLE
        btn_close.visibility = View.INVISIBLE
    }

    private fun renderInitState(dataState: State.InitState) {
        text_state.setText(R.string.init_state)
        btn_alert.visibility = View.INVISIBLE
        btn_stop.visibility = View.INVISIBLE
        btn_start.visibility = View.INVISIBLE
        btn_error.visibility = View.VISIBLE
        btn_close.visibility = View.INVISIBLE
    }

    private fun renderErrorState(dataState: State.ErrorState) {
        text_state.setText(R.string.error_state)
        btn_alert.visibility = View.INVISIBLE
        btn_stop.visibility = View.VISIBLE
        btn_start.visibility = View.INVISIBLE
        btn_error.visibility = View.INVISIBLE
        btn_close.visibility = View.INVISIBLE
    }

    private fun renderAlertState(dataState: State.AlertState) {
        text_state.setText(R.string.alert_state)
        text_state.setText(R.string.error_state)
        btn_alert.visibility = View.INVISIBLE
        btn_stop.visibility = View.INVISIBLE
        btn_start.visibility = View.INVISIBLE
        btn_error.visibility = View.INVISIBLE
        btn_close.visibility = View.INVISIBLE
    }

    fun stopEventIntent() = btn_stop.clicks()

    fun startEventIntent() = btn_start.clicks()

    fun errorEventIntent() = btn_error.clicks()

    fun alertEventItent() = btn_alert.clicks()

    fun closeEventItent() = btn_close.clicks()

}
