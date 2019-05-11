package com.example.statemachine.model.statemachine

import android.annotation.SuppressLint
import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.statemachine.state.StopState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class StateMachine {

    // todo make it private
    var currentState: State = StopState()

    private val currentStateBeSubject = BehaviorSubject.create<State>()

    init {
        currentStateBeSubject.onNext(currentState)
    }

    // todo make it private
    @SuppressLint("CheckResult")
    fun setState(state: State) {
        currentState = state
        currentStateBeSubject.onNext(currentState)
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
        return currentStateBeSubject
    }

}