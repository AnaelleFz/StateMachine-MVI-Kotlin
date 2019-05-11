package com.example.statemachine.model

import android.os.Handler
import android.util.Log
import com.example.statemachine.model.statemachine.StateMachine
import com.example.statemachine.model.statemachine.state.StopState
import com.example.statemachine.view.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter {

    private val TAG = "MainPresenter"
    private val errorCollectEvent = "An error occurred trying to collect events"

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivity
    private val stateMachine = StateMachine()
    private val eventBus = EventBus()

    /**
     * When mainPresenter is binding with view :
     *  - Initialize State Machine
     *  - Start to observe and collect all ui event
     */
    fun bind(view: MainActivity) {
        this.view = view
        stateMachine.setState(StopState())

        // Events reception :
        compositeDisposable.add(
            collectAllEvents()
                .subscribe({ event -> eventBus.passEvent(event) },
                    { t ->
                        Log.e(TAG, errorCollectEvent, t)
                    })
        )

        // Events consumption :
        compositeDisposable.add(
            eventBus.getEvents()
                .doOnNext { event -> sendStartTimerEvent(event) }
                .doOnNext { event -> stateMachine.onEvent(event) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

        compositeDisposable.add(
            stateMachine.observeCurrentState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state -> view.render(state) }
        )
    }

    /**
     * When mainPresenter is unbind, do some actions :
     * - Dispose compositeDisposable
     */
    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    /**
     * If event is EventEnum.START
     * Then pass EventEnum.START_AND_TIMER_ENDS to eventBus
     * after 3 seconds delay.
     */
    private fun sendStartTimerEvent(event: EventEnum) {
        if (event == EventEnum.START) {
            Handler().postDelayed({
                eventBus.passEvent(EventEnum.START_AND_TIMER_ENDS)
            }, 3000)

        }
    }

    /**
     * Merge UI events and events from alertService
     */
    private fun collectAllEvents(): Observable<EventEnum> {
        return collectAllUiEvents()
            .mergeWith(view.alertServiceObserver)
    }

    /**
     * Collect all events from UI in one observable
     */
    private fun collectAllUiEvents(): Observable<EventEnum> {
        return (view.stopEventIntent().map { EventEnum.STOP })
            .mergeWith(view.startEventIntent().map { EventEnum.START })
            .mergeWith(view.startEventIntent().map { EventEnum.START })
            .mergeWith(view.resetEventIntent().map { EventEnum.RESET })
            .mergeWith(view.closeEventItent().map { EventEnum.CLOSE })

    }
}
