package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    Item item1;
    Item item2;
    User user1;
    User user2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "user1", "user1@mail.ru"));
        user2 = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        item1 = itemRepository.save(new Item(1L, "item1", "description1", true, user1, null));
        item2 = itemRepository.save(new Item(2L, "item2", "description2", true, user2, null));
    }

    @Test
    public void testFindAllByOwner() {
        User user = userRepository.save(new User(null, "testUser", "test@example.com"));
        Item item1 = itemRepository.save(new Item(1L, "Item1", "Description1", true, user, null));
        Item item2 = itemRepository.save(new Item(2L, "Item2", "Description2", true, user, null));

        List<Item> items = itemRepository.findAllByOwner(user);

        assertEquals(2, items.size());
        assertEquals("Item1", items.get(0).getName());
        assertEquals("Description1", items.get(0).getDescription());
        assertEquals("Item2", items.get(1).getName());
        assertEquals("Description2", items.get(1).getDescription());
    }

    @Test
    void testFindByNameOrDescriptionContainingIgnoreCase() {
        User user = userRepository.save(new User(null, "testUser", "test@example.com"));
        Item item1 = itemRepository.save(new Item(1L, "item1", "description1", true, user, null));
        Item item2 = itemRepository.save(new Item(2L, "item2", "description2", true, user, null));
        Item item3 = itemRepository.save(new Item(3L, "Other", "other description", true, user, null));

        List<Item> items = itemRepository.findByNameOrDescriptionContainingIgnoreCase("item");

        assertEquals("item1", items.get(0).getName());
        assertEquals("description1", items.get(0).getDescription());
        assertEquals("item2", items.get(1).getName());
        assertEquals("description2", items.get(1).getDescription());
    }

    @Test
    void testDeleteById_Success() throws IllegalAccessException {
        List<Item> itemsBefore = itemRepository.findAll();
        assertEquals(2, itemsBefore.size());

        Long itemIdToDelete = itemsBefore.get(0).getId();

        itemRepository.deleteById(itemIdToDelete);

        List<Item> itemsAfter = itemRepository.findAll();
        assertEquals(1, itemsAfter.size());
    }
}