package com.example.statemachine.model

sealed class State {
    data class StopState(val data: String) : State()
    data class InitState(val data: String) : State()
    data class StartState(val data: String) : State()
    data class ErrorState(val data: String) : State()
    data class AlertState(val data: String) : State()
    object firstState : State()
}