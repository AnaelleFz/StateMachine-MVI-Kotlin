package com.example.statemachine.model.statemachine

import android.annotation.SuppressLint
import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.state.StopState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class StateMachine {

    // todo make it private
    var currentState: State = StopState()

    private val currentStateBeSubject = BehaviorSubject.create<State>()

    init {
        initStateObservation()
    }

    // todo make it private
    fun setState(state: State) {
        currentState = state
        initStateObservation()
    }

    @SuppressLint("CheckResult")
    private fun initStateObservation() {
        currentStateBeSubject.onNext(currentState)
        currentState.observeNextState()
            .subscribe { nextState ->
                setState(nextState)
            }
    }

    /**
     * Only input of state machine
     */
    fun onEvent(event: Event) {
        currentState.onEventReceived(event)
    }

    /**
     * To observe current state outside of state machine
     */
    fun observeCurrentState(): Observable<State> {
        return currentStateBeSubject
    }

}