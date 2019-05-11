package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface State {

    val nextStatePublishSubject: PublishSubject<State>

    fun onEventReceived(event: EventEnum)

    fun observeNextState(): Observable<State> {
        return nextStatePublishSubject
    }
}