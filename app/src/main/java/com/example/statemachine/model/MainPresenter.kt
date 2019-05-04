package com.example.statemachine.model

import com.example.statemachine.model.statemachine.EventEnum
import com.example.statemachine.model.statemachine.StateEnum
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
        stateMachine = initStateMachine()
        compositeDisposable.add(
            collectedallEvents()
                .subscribe { e -> retrieveNewState(e) }
        )
        compositeDisposable.add(
            collectedallEvents()
                .flatMap { event -> retrieveNewState(event) }
                .doAfterNext { state -> view.render(state) }
                .subscribe { state -> System.out.println(state) }
        )
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun collectedallEvents(): Observable<EventEnum> {
        return (view.stopEventIntent().map { EventEnum.STOP })
            .mergeWith(view.alertEventItent().map { EventEnum.ALERT })
            .mergeWith(view.startEventIntent().map { EventEnum.START })
            .mergeWith(view.startEventIntent().map { EventEnum.START })
            .mergeWith(view.errorEventIntent().map { EventEnum.ERROR })
            .mergeWith(view.closeEventItent().map { EventEnum.CLOSE })
    }

    private fun retrieveNewState(event: EventEnum): Observable<StateEnum> {
        return Observable.fromCallable {
            stateMachine.onEvent(event)
        }
    }

    // todo move this in an other place
    private fun initStateMachine(): StateMachine {
        val stateMachine = buildStateMachine(initialStateName = StateEnum.StopState) {
            addState(name = StateEnum.AlertState) {

                addStep(name = "Alert to Error", event = EventEnum.ERROR, finalState = StateEnum.ErrorState)

                addStep(name = "Alter to Start", event = EventEnum.CLOSE, finalState = StateEnum.StartState)
            }

            addState(name = StateEnum.ErrorState) {

                addStep(name = "Error to Stop", event = EventEnum.STOP, finalState = StateEnum.StopState)
            }

            addState(name = StateEnum.InitState) {

                addStep(
                    name = "Init to Start",
                    event = EventEnum.START_AND_TIMER_ENDS,
                    finalState = StateEnum.StartState
                )

                addStep(name = "Init to Error", event = EventEnum.ERROR, finalState = StateEnum.ErrorState)

            }

            addState(name = StateEnum.StartState) {

                addStep(name = "Start to Alert", event = EventEnum.ALERT, finalState = StateEnum.AlertState)

                addStep(name = "Start to Error", event = EventEnum.ERROR, finalState = StateEnum.ErrorState)

                addStep(name = "Start to Stop", event = EventEnum.STOP, finalState = StateEnum.StopState)
            }

            addState(name = StateEnum.StopState) {

                addStep(name = "Stop to Init", event = EventEnum.START, finalState = StateEnum.InitState)

                addStep(name = "Stop to Error", event = EventEnum.ERROR, finalState = StateEnum.ErrorState)
            }
        }

        stateMachine.initStateMachine()

        return stateMachine
    }

    fun buildStateMachine(initialStateName: StateEnum, init: StateMachine.() -> Unit): StateMachine {
        val stateMachine = StateMachine(initialStateName)

        stateMachine.init()

        return stateMachine
    }
}
