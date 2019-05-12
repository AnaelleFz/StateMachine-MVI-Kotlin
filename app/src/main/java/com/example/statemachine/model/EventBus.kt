package com.example.statemachine.model

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {

    val eventPublishSubject = PublishSubject.create<Event>()

    fun passEvent(event: Event) {
        eventPublishSubject.onNext(event)
    }

    fun getEvents(): Observable<Event> {
        return eventPublishSubject
    }
}