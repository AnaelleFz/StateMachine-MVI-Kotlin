package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.StateEnum

class State(val stateName: StateEnum) {

    private val stepList = mutableListOf<Step>()

    fun addStep(name: String, event: EventEnum, finalState: StateEnum) {
        val step = Step(name, event, finalState)
        stepList.add(step)
    }

    fun getStepForEvent(eventEnum: EventEnum): Step {
        return stepList.first { it.event.equals(eventEnum) }
    }
}