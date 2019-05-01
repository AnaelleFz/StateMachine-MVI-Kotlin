package com.example.statemachine.model

import com.example.statemachine.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class MainPresenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivity
    private lateinit var stateMachine: StateMachine


    fun bind(view: MainActivity) {
        this.view = view
        stateMachine = StateMachine()
        compositeDisposable.add(observeStopAction())
        compositeDisposable.add(observeStartAction())
        compositeDisposable.add(observeStateChange())
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun observeStopAction() = view.stopEventIntent()
        .doOnNext { stateMachine.onEventReceived(Event.STOP) }
        .subscribe()

    private fun observeStartAction() = view.startEventIntent()
        .doOnNext { stateMachine.onEventReceived(Event.START) }
        .subscribe()

    private fun observeStateChange() = stateMachine.publishStateChange
        .doOnNext { state -> view.render(state) }
        .subscribe()

}
