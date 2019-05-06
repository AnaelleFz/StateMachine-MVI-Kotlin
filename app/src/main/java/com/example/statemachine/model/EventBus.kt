package com.example.statemachine.model

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {

    val eventPublishSubject = PublishSubject.create<EventEnum>()

    fun passEvent(event: EventEnum) {
        eventPublishSubject.onNext(event)
    }

    fun getEvents(): Observable<EventEnum> {
        return eventPublishSubject
    }
}