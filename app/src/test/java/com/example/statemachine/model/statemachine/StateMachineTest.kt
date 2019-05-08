package com.example.statemachine.model.statemachine

import com.example.statemachine.model.EventEnum
import com.example.statemachine.model.StateEnum
import org.junit.Assert.assertEquals
import org.junit.Test

class StateMachineTest {

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
    fun `test error event reception`() {
        val stateMachine = createStateMachine()

        // STOP -> ERROR
        val stateResult1 = stateMachine.onEvent(EventEnum.ERROR)
        assertEquals(StateEnum.ErrorState, stateResult1)

        // INIT -> ERROR
        stateMachine.setState(StateEnum.InitState)
        val stateResult2 = stateMachine.onEvent(EventEnum.ERROR)
        assertEquals(StateEnum.ErrorState, stateResult2)

        // ALERT -> ERROR
        stateMachine.setState(StateEnum.AlertState)
        val stateResult3 = stateMachine.onEvent(EventEnum.ERROR_AND_CLOSE)
        assertEquals(StateEnum.ErrorState, stateResult3)

    }
}
