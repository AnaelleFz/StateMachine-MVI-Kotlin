package com.example.statemachine.model.statemachine.state

import android.util.Log
import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.State
import io.reactivex.subjects.PublishSubject

class StopState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            is Event.Start -> nextStatePublishSubject.onNext(InitState())
            is Event.Error -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Stop State", "Unexpected event")
        }
    }

}

class InitState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            is Event.StartAndTimerEnds -> nextStatePublishSubject.onNext(StartState())
            is Event.Error -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Init State", "Unexpected event")
        }
    }

}

class StartState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            is Event.Stop -> nextStatePublishSubject.onNext(StopState())
            is Event.Alert -> nextStatePublishSubject.onNext(AlertState(event.desc))
            is Event.Error -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Start State", "Unexpected event")
        }
    }

}

class ErrorState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: Event) {
        when (event) {
            is Event.Reset -> nextStatePublishSubject.onNext(StopState())
            else -> Log.w("Error State", "Unexpected event")
        }
    }

}