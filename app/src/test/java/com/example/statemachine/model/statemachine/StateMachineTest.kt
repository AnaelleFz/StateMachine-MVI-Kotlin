package com.example.statemachine.model.statemachine

import com.example.statemachine.model.Event
import com.example.statemachine.model.statemachine.state.*
import org.junit.Assert
import org.junit.Test

class StateMachineTest {

    // Alert State
    @Test
    fun `In alert if new alert is received state machine remains in alert after close event`() {
        val stateMachine = StateMachine()
        stateMachine.setState(AlertState("alert 0"))
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Close())
        Assert.assertTrue(stateMachine.currentState is AlertState)
    }

    @Test
    fun `In alert when all alter were closed then start state is the new state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(AlertState("alert 0"))
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Close())
        stateMachine.onEvent(Event.Close())
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    @Test
    fun `In alert if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(AlertState("alert 0"))
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Error("Error 0"))
        stateMachine.onEvent(Event.Close())
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    // Stop state

    @Test
    fun `In stop if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(Event.Error("Error 0"))
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In stop if start is received then next state is init state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(Event.Start())
        Assert.assertTrue(stateMachine.currentState is InitState)
    }

    @Test
    fun `In stop if other event (than start and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(Event.Reset())
        stateMachine.onEvent(Event.Stop())
        stateMachine.onEvent(Event.Close())
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.StartAndTimerEnds())
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    // Init State

    @Test
    fun `In init if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(Event.Error("Error 0"))
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In init if start_ is received then next state is init state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(Event.StartAndTimerEnds())
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    @Test
    fun `In init if other event (than start_and_timer_end and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(Event.Reset())
        stateMachine.onEvent(Event.Stop())
        stateMachine.onEvent(Event.Close())
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Start())
        Assert.assertTrue(stateMachine.currentState is InitState)
    }

    // Start State

    @Test
    fun `In start if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(Event.Error("Error 0"))
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In start if stop is received then next state is stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(Event.Stop())
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    @Test
    fun `In start if alert is received then next state is alert state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        Assert.assertTrue(stateMachine.currentState is AlertState)
    }


    @Test
    fun `In start if other event (than stop, alert and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(Event.Reset())
        stateMachine.onEvent(Event.Start())
        stateMachine.onEvent(Event.StartAndTimerEnds())
        stateMachine.onEvent(Event.Close())
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    // Error State

    @Test
    fun `In error if reset is received then next state is stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(ErrorState("Error 0"))
        stateMachine.onEvent(Event.Reset())
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    @Test
    fun `In error if other event (than reset) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(ErrorState("Error 0"))
        stateMachine.onEvent(Event.Start())
        stateMachine.onEvent(Event.StartAndTimerEnds())
        stateMachine.onEvent(Event.Alert("alert 1", 0))
        stateMachine.onEvent(Event.Close())
        stateMachine.onEvent(Event.Error("Error 1"))
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }


}
