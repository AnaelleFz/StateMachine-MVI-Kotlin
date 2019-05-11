package com.example.statemachine.model.statemachine.state

import android.util.Log
import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.State
import io.reactivex.subjects.PublishSubject

class StopState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            Event.START -> nextStatePublishSubject.onNext(InitState())
            Event.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Stop State", "Unexpected event")
        }
    }

}

class InitState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            Event.START_AND_TIMER_ENDS -> nextStatePublishSubject.onNext(StartState())
            Event.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Init State", "Unexpected event")
        }
    }

}

class StartState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            Event.STOP -> nextStatePublishSubject.onNext(StopState())
            Event.ALERT -> nextStatePublishSubject.onNext(AlertState())
            Event.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Start State", "Unexpected event")
        }
    }

}

class ErrorState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            Event.RESET -> nextStatePublishSubject.onNext(StopState())
            else -> Log.w("Error State", "Unexpected event")
        }
    }

}