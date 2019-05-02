package com.example.statemachine.model.statemachine

import io.reactivex.subjects.PublishSubject

class StateMachine {

    val publishStateChange = PublishSubject.create<State>()

    fun onEventReceived(event: Event) {
        when (event) {
            Event.START -> onStartEventReceived()
            Event.STOP -> onStopEventReceived()
        }
    }

    fun onStopEventReceived() {
        val newState = State.StopState("StopState")
        publishStateChange.onNext(newState)
    }

    fun onStartEventReceived() {
        val newState = State.StartState("StartState")
        publishStateChange.onNext(newState)

    }
}