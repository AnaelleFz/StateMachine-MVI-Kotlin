package com.example.statemachine.model

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {

    val eventPublishSubject = PublishSubject.create<EventEnum>()

    var lastEvent: EventEnum? = null

    fun passEvent(event: EventEnum) {
        eventPublishSubject.onNext(event)
        lastEvent = event
    }

    fun getEvents(): Observable<EventEnum> {
        return eventPublishSubject
    }
}