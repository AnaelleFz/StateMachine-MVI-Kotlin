package com.example.statemachine.model.statemachine

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

    fun onEvent(eventEnum: EventEnum): StateEnum {
        val edge = currentState.getStepForEvent(eventEnum)
        currentState = getStateByName(edge.finalState)
        return currentState.stateName
    }

    fun getStateByName(name: StateEnum): State {
        val result = stateList.firstOrNull { it.stateName == name }
            ?: throw NoSuchElementException(name.toString())

        return result
    }
}