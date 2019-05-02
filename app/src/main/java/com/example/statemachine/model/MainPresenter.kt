package com.example.statemachine.model

import com.example.statemachine.model.statemachine.Event
import com.example.statemachine.model.statemachine.StateMachine
import com.example.statemachine.view.MainActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class MainPresenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivity
    private lateinit var stateMachine: StateMachine

    fun bind(view: MainActivity) {
        this.view = view
        stateMachine = StateMachine()
        compositeDisposable.add(
            allEvents().subscribe { e -> printEvent(e) }
        )
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun printEvent(event: Event) {
        System.out.println(event)
    }

    private fun allEvents(): Observable<Event> {
        return (view.stopEventIntent().map { Event.STOP })
            .mergeWith(view.alertEventItent().map { Event.ALERT })
            .mergeWith(view.startEventIntent().map { Event.START })
            .mergeWith(view.startEventIntent().map { Event.START })
            .mergeWith(view.errorEventIntent().map { Event.ERROR })
            .mergeWith(view.closeEventItent().map { Event.CLOSE })
    }


}
