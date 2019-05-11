package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.statemachine.state.*
import org.junit.Assert
import org.junit.Test

class StateMachineTest {

    // Alert State

    @Test
    fun `send unexpected event to stateMachine`() {
        val stateMachine = createStateMachine()

        // state machine is on stop state
        val stateResult = stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        assertEquals(StateEnum.NoState, stateResult)

        stateMachine.setState(StateEnum.AlertState)
        val stateResult2 = stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        assertEquals(StateEnum.NoState, stateResult2)
    }

    @Test
    fun `test normal behavior`() {
        val stateMachine = createStateMachine()

        // STOP -> INIT
        val stateResult1 = stateMachine.onEvent(EventEnum.START)
        assertEquals(StateEnum.InitState, stateResult1)

        // INIT -> START
        val stateResult2 = stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        assertEquals(StateEnum.StartState, stateResult2)

        // START -> ALERT
        val stateResult3 = stateMachine.onEvent(EventEnum.ALERT)
        assertEquals(StateEnum.AlertState, stateResult3)

        // ALERT -> START
        val stateResult4 = stateMachine.onEvent(EventEnum.CLOSE)
        assertEquals(StateEnum.StartState, stateResult4)

        // START -> ERROR
        val stateResult5 = stateMachine.onEvent(EventEnum.ERROR)
        assertEquals(StateEnum.ErrorState, stateResult5)

        // ERROR -> STOP
        val stateResult6 = stateMachine.onEvent(EventEnum.RESET)
        assertEquals(StateEnum.StopState, stateResult6)

    }

    @Test
    fun `In alert when all alter were closed then start state is the new state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(AlertState())
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.CLOSE)
        stateMachine.onEvent(EventEnum.CLOSE)
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    @Test
    fun `In alert if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(AlertState())
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.ERROR)
        stateMachine.onEvent(EventEnum.CLOSE)
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    // Stop state

    @Test
    fun `In stop if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(EventEnum.ERROR)
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In stop if start is received then next state is init state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(EventEnum.START)
        Assert.assertTrue(stateMachine.currentState is InitState)
    }

    @Test
    fun `In stop if other event (than start and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StopState())
        stateMachine.onEvent(EventEnum.RESET)
        stateMachine.onEvent(EventEnum.STOP)
        stateMachine.onEvent(EventEnum.CLOSE)
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        stateMachine.onEvent(EventEnum.ERROR_AND_CLOSE)
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    // Init State

    @Test
    fun `In init if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(EventEnum.ERROR)
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In init if start_ is received then next state is init state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    @Test
    fun `In init if other event (than start_and_timer_end and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(InitState())
        stateMachine.onEvent(EventEnum.RESET)
        stateMachine.onEvent(EventEnum.STOP)
        stateMachine.onEvent(EventEnum.CLOSE)
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.START)
        stateMachine.onEvent(EventEnum.ERROR_AND_CLOSE)
        Assert.assertTrue(stateMachine.currentState is InitState)
    }

    // Start State

    @Test
    fun `In start if error is received then next state is error state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(EventEnum.ERROR)
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }

    @Test
    fun `In start if stop is received then next state is stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(EventEnum.STOP)
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    @Test
    fun `In start if alert is received then next state is alert state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(EventEnum.ALERT)
        Assert.assertTrue(stateMachine.currentState is AlertState)
    }


    @Test
    fun `In start if other event (than stop, alert and error) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(StartState())
        stateMachine.onEvent(EventEnum.RESET)
        stateMachine.onEvent(EventEnum.START)
        stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        stateMachine.onEvent(EventEnum.CLOSE)
        stateMachine.onEvent(EventEnum.ERROR_AND_CLOSE)
        Assert.assertTrue(stateMachine.currentState is StartState)
    }

    // Error State

    @Test
    fun `In error if reset is received then next state is stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(ErrorState())
        stateMachine.onEvent(EventEnum.RESET)
        Assert.assertTrue(stateMachine.currentState is StopState)
    }

    @Test
    fun `In error if other event (than reset) is received then SM remains in stop state`() {
        val stateMachine = StateMachine()
        stateMachine.setState(ErrorState())
        stateMachine.onEvent(EventEnum.START)
        stateMachine.onEvent(EventEnum.START_AND_TIMER_ENDS)
        stateMachine.onEvent(EventEnum.ALERT)
        stateMachine.onEvent(EventEnum.CLOSE)
        stateMachine.onEvent(EventEnum.ERROR_AND_CLOSE)
        stateMachine.onEvent(EventEnum.ERROR)
        Assert.assertTrue(stateMachine.currentState is ErrorState)
    }


}
