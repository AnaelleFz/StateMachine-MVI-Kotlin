package com.example.statemachine.model

import android.os.Handler
import android.util.Log
import com.example.statemachine.model.statemachine.StateMachine
import com.example.statemachine.model.statemachine.createStateMachine
import com.example.statemachine.view.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter {

    private val TAG = "MainPresenter"
    private val errorCollectEvent = "An error occurred trying to collect events"
    private val errorRetrieveNextState = "An error occurred trying to retrieve next state"

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: MainActivity
    private lateinit var stateMachine: StateMachine
    private val eventBus = EventBus()

    /**
     * When mainPresenter is binding with view :
     *  - Initialize State Machine
     *  - Start to observe and collect all ui event
     */
    fun bind(view: MainActivity) {
        this.view = view
        stateMachine = createStateMachine()

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { event -> sendStartTimerEvent(event) }
                .flatMap { event -> sendErrorAndCloseEvent(event) }
                .flatMap { event -> retrieveNextState(event) }
                .doAfterNext { state -> view.render(state) }
                .subscribe(
                    { state -> System.out.println(state) },
                    { t ->
                        Log.e(TAG, errorRetrieveNextState, t)
                    }
                )
        )
    }

    /**
     * If event is EventEnum.START
     * Then pass EventEnum.START_AND_TIMER_ENDS to eventBus
     * after 3 seconds delay.
     */
    fun sendStartTimerEvent(event: EventEnum) {
        if (event == EventEnum.START) {
            Handler().postDelayed({
                eventBus.passEvent(EventEnum.START_AND_TIMER_ENDS)
            }, 3000)

        }
    }

    /**
     * If event is EventEnum.CLOSE and the last event set is EventEnum.Error
     * Then transform EventEnum.CLOSE in EventEnum.ERROR_AND_CLOSE
     */
    fun sendErrorAndCloseEvent(event: EventEnum): Observable<EventEnum> {
        if (event == EventEnum.CLOSE && eventBus.lastEvent == EventEnum.ERROR) {
            return Observable.just(EventEnum.ERROR_AND_CLOSE)
        }
        return Observable.just(event)
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
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
    }

    private fun retrieveNextState(event: EventEnum): Observable<StateEnum> {
        return Observable.fromCallable {
            stateMachine.onEvent(event)
        }
    }
}
