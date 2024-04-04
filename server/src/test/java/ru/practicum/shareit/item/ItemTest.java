package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void testEqualsAndHashCode() {
        long id = 123;
        Item item1 = new Item(id, "Item1", "Description1", true, null, null);
        Item item2 = new Item(id, "Item2", "Description2", false, null, null);
        Item item3 = new Item(id + 1, "Item1", "Description1", true, null, null);

        assertEquals(item1, item1);
        assertEquals(item1, item2);
        assertNotEquals(item1, item3);

        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }
}
