package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(User userId);

    void deleteById(long itemId) throws IllegalAccessException;

    @Query("SELECT i FROM Item i WHERE" +
            " (LOWER(i.name) LIKE %:string% OR LOWER(i.description) LIKE %:string%)" +
            " AND i.available = true")
    List<Item> findByNameOrDescriptionContainingIgnoreCase(String string);
}