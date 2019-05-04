package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum

data class Step(val name: String, val event: EventEnum, val finalState: StateEnum)
