package com.example.statemachine.model

import io.reactivex.subjects.PublishSubject

class EventBus {

    private val publishEventBus = PublishSubject.create<Event>()

    fun sendEvent(event: Event) {
        publishEventBus.onNext(event)
    }

    /* todo
    fun toObservable() : Observable<Event>{
        return publishEventBus.
    }
    */
}