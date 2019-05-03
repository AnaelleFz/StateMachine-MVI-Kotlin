package com.example.statemachine.model.statemachine

data class Step(val name: String, val event: EventEnum, val finalState: StateEnum)
