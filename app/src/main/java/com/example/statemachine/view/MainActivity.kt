package com.example.statemachine.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.statemachine.R
import com.example.statemachine.model.MainPresenter
import com.example.statemachine.model.State
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
    }

    private fun renderStartState(dataState: State.StartState) {
        text_state.setText(R.string.start_state)
    }

    private fun renderInitState(dataState: State.InitState) {
        text_state.setText(R.string.init_state)
    }

    private fun renderErrorState(dataState: State.ErrorState) {
        text_state.setText(R.string.error_state)
    }

    private fun renderAlertState(dataState: State.AlertState) {
        text_state.setText(R.string.alert_state)
    }

    fun displayStopIntent() = btn_stop.clicks()

    fun displayStartIntent() = btn_start.clicks()

    fun displayErrorIntent() = btn_error.clicks()

    fun displayAlertIntent() = btn_alert.clicks()

    fun displayInitIntent() = btn_init.clicks()

}
