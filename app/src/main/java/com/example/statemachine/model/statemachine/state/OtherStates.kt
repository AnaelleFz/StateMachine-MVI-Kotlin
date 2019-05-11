package com.example.statemachine.model.statemachine.state

import android.util.Log
import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.statemachine.State
import io.reactivex.subjects.PublishSubject

class StopState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: EventEnum) {
        when (event) {
            EventEnum.START -> nextStatePublishSubject.onNext(InitState())
            EventEnum.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Stop State", "Unexpected event")
        }
    }

}

class InitState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: EventEnum) {
        when (event) {
            EventEnum.START_AND_TIMER_ENDS -> nextStatePublishSubject.onNext(StartState())
            EventEnum.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Init State", "Unexpected event")
        }
    }

}

class StartState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: EventEnum) {
        when (event) {
            EventEnum.STOP -> nextStatePublishSubject.onNext(StopState())
            EventEnum.ALERT -> nextStatePublishSubject.onNext(AlertState())
            EventEnum.ERROR -> nextStatePublishSubject.onNext(ErrorState())
            else -> Log.w("Start State", "Unexpected event")
        }
    }

}

class ErrorState : State {

    override val nextStatePublishSubject = PublishSubject.create<State>()

    override fun onEventReceived(event: EventEnum) {
        when (event) {
            EventEnum.RESET -> nextStatePublishSubject.onNext(StopState())
            else -> Log.w("Error State", "Unexpected event")
        }
    }

}