package com.example.statemachine.model.statemachine

import com.example.statemachine.model.Event
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface State {

    val nextStatePublishSubject: PublishSubject<State>

    fun onEventReceived(event: Event)

    fun observeNextState(): Observable<State> {
        return nextStatePublishSubject
    }
}