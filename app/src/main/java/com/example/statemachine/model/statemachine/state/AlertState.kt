package com.example.statemachine.model.statemachine.state

import android.util.Log
import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.State
import io.reactivex.subjects.PublishSubject

class AlertState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    private val alertStack = mutableListOf<Event>()

    private var isError = false

    override fun onEventReceived(event: Event) {
        when (event) {
            Event.ERROR -> isError = true
            Event.ALERT -> alertStack.add(event)
            Event.CLOSE -> triggerNextState()
            else -> Log.w("Start State", "Unexpected event")
        }
    }

    private fun triggerNextState() {
        when {
            isError -> nextStatePublishSubject.onNext(ErrorState())
            alertStack.isEmpty() -> nextStatePublishSubject.onNext(StartState())
            else -> {
                alertStack.removeAt(alertStack.size - 1)
                nextStatePublishSubject.onNext(AlertState())
            }
        }
    }
}