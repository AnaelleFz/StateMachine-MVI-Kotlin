package com.example.statemachine.model

import com.example.statemachine.model.statemachine.EventEnum
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {

    val eventPublishSubject = PublishSubject.create<EventEnum>()

    fun passEvent(event: EventEnum) {
        eventPublishSubject.onNext(event)
    }

    fun observeEvent(): Observable<EventEnum> {
        return eventPublishSubject
    }
}