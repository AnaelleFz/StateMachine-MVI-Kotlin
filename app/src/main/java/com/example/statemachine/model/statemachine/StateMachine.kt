package com.example.statemachine.model.statemachine

import android.annotation.SuppressLint
import com.example.statemachine.model.EventEnum
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class StateMachine {

    //todo make it private
    lateinit var currentState: State

    private val currentStatePublishSubject = PublishSubject.create<State>()

    @SuppressLint("CheckResult")
    // todo make it private
    fun setState(state: State) {
        currentState = state
        currentStatePublishSubject.onNext(currentState)
        currentState.observeNextState().subscribe { nextState ->
            setState(nextState)
        }
    }

    /**
     * Only input of state machine
     */
    fun onEvent(eventEnum: EventEnum) {
        currentState.onEventReceived(eventEnum)
    }

    /**
     * To observe current state outside of state machine
     */
    fun observeCurrentState(): Observable<State> {
        return currentStatePublishSubject
    }

}