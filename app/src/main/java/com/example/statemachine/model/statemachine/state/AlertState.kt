package com.example.statemachine.model.statemachine.state

import android.util.Log
import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.State
import io.reactivex.subjects.PublishSubject

class AlertState(val desc: String) : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    private val alertStack = mutableListOf<Event.Alert>()

    private var isError = false

    override fun onEventReceived(event: Event) {
        when (event) {
            is Event.Error -> isError = true
            is Event.Alert -> alertStack.add(event)
            is Event.Close -> triggerNextState()
            else -> Log.w("Start State", "Unexpected event")
        }
    }

    private fun triggerNextState() {
        when {
            isError -> nextStatePublishSubject.onNext(ErrorState())
            alertStack.isEmpty() -> nextStatePublishSubject.onNext(StartState())
            else -> {
                nextStatePublishSubject.onNext(
                    AlertState(alertStack[alertStack.size - 1].desc)
                )
                alertStack.removeAt(alertStack.size - 1)
            }
        }
    }
}