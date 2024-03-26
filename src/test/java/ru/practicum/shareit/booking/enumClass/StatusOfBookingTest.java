package ru.practicum.shareit.booking.enumClass;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.State;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusOfBookingTest {

    @Test
    void testStateEnumValues() {
        assertEquals(8, State.values().length);

        assertEquals(State.ALL, State.valueOf("ALL"));
        assertEquals(State.CURRENT, State.valueOf("CURRENT"));
        assertEquals(State.PAST, State.valueOf("PAST"));
        assertEquals(State.FUTURE, State.valueOf("FUTURE"));
        assertEquals(State.WAITING, State.valueOf("WAITING"));
        assertEquals(State.APPROVED, State.valueOf("APPROVED"));
        assertEquals(State.REJECTED, State.valueOf("REJECTED"));
        assertEquals(State.CANCELED, State.valueOf("CANCELED"));
    }
}
