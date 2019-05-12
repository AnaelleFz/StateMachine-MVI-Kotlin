package com.example.statemachine.model

sealed class Event {
    class Stop : Event()
    class Start : Event()
    class Error(val desc: String) : Event()
    class Reset : Event()
    class Alert(val desc: String, val delayInSecond: Long) : Event()
    class Close : Event()
    class StartAndTimerEnds : Event()
}