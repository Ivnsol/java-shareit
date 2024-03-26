package ru.practicum.shareit.booking.enumClass;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.State;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    void testEnumValues() {
        State[] states = State.values();

        assertEquals(8, states.length);
        assertEquals(State.ALL, states[0]);
        assertEquals(State.CURRENT, states[1]);
        assertEquals(State.PAST, states[2]);
        assertEquals(State.FUTURE, states[3]);
        assertEquals(State.WAITING, states[4]);
        assertEquals(State.APPROVED, states[5]);
        assertEquals(State.REJECTED, states[6]);
        assertEquals(State.CANCELED, states[7]);
    }
}
