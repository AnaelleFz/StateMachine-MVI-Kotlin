package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.StateEnum

class StateMachine(private val initialStateName: StateEnum) {
    private lateinit var currentState: State

    private val stateList = mutableListOf<State>()

    fun initStateMachine() {
        currentState = getStateByName(initialStateName)
    }

    fun addState(name: StateEnum, init: State.() -> Unit) {
        val state = State(name)
        state.init()
        stateList.add(state)
    }

    /**
     * When event is received update state
     */
    fun onEvent(eventEnum: EventEnum): StateEnum {
        val edge = currentState.getStepForEvent(eventEnum)
        if (edge != null) {
            currentState = getStateByName(edge.finalState)
        }
        return currentState.stateName
    }

    fun getStateByName(name: StateEnum): State {
        val result = stateList.firstOrNull { it.stateName == name }
            ?: throw NoSuchElementException(name.toString())

        return result
    }

}


fun buildStateMachine(initialStateName: StateEnum, init: StateMachine.() -> Unit): StateMachine {
    val stateMachine = StateMachine(initialStateName)

    stateMachine.init()

    return stateMachine
}

fun createStateMachine(): StateMachine {
    val stateMachine = buildStateMachine(initialStateName = StateEnum.StopState) {
        addState(name = StateEnum.AlertState) {

            addStep(name = "Alert to Error", event = EventEnum.ERROR, finalState = StateEnum.ErrorState)

            addStep(name = "Alter to Start", event = EventEnum.CLOSE, finalState = StateEnum.StartState)
        }

        addState(name = StateEnum.ErrorState) {

            addStep(name = "Error to Stop", event = EventEnum.RESET, finalState = StateEnum.StopState)
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