package ru.practicum.shareit.request.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
class ItemRequestInDtoTest {

    @Autowired
    private JacksonTester<ItemRequestInDto> json;

    @Test
    void testItemRequestInDto() throws IOException {

        var requester = new User(
                3L,
                "UserName",
                "user3@email.ru"
        );

        var itemRequestInDto = new ItemRequestInDto(
                1L,
                "request text",
                requester,
                LocalDateTime.of(2022, Month.OCTOBER, 15, 00, 32, 22)
        );

        var res = json.write(itemRequestInDto);

        assertThat(res).hasJsonPath("$.id");
        assertThat(res).hasJsonPath("$.description");
        assertThat(res).hasJsonPath("$.requester");
        assertThat(res).hasJsonPath("$.created");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestInDto.getId().intValue());
        assertThat(res).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestInDto.getDescription());
        assertThat(res).extractingJsonPathNumberValue("$.requester.id")
                .isEqualTo(itemRequestInDto.getRequester().getId().intValue());
        assertThat(res).extractingJsonPathStringValue("$.requester.name")
                .isEqualTo(itemRequestInDto.getRequester().getName());
        assertThat(res).extractingJsonPathStringValue("$.requester.email")
                .isEqualTo(itemRequestInDto.getRequester().getEmail());
        assertThat(res).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestInDto.getCreated().toString());
    }

    @Test
    void testNoArgsConstructor() {
        ItemRequestInDto itemRequestInDto = new ItemRequestInDto();
        assertNull(itemRequestInDto.getId());
        assertNull(itemRequestInDto.getDescription());
        assertNull(itemRequestInDto.getRequester());
        assertNull(itemRequestInDto.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String description = "Test description";
        User requester = new User();
        LocalDateTime created = LocalDateTime.now();

        ItemRequestInDto itemRequestInDto = new ItemRequestInDto(id, description, requester, created);

        assertEquals(id, itemRequestInDto.getId());
        assertEquals(description, itemRequestInDto.getDescription());
        assertEquals(requester, itemRequestInDto.getRequester());
        assertEquals(created, itemRequestInDto.getCreated());
    }

    @Test
    void testSetterGetterMethods() {
        ItemRequestInDto itemRequestInDto = new ItemRequestInDto();

        Long id = 1L;
        String description = "Test description";
        User requester = new User();
        LocalDateTime created = LocalDateTime.now();

        itemRequestInDto.setId(id);
        itemRequestInDto.setDescription(description);
        itemRequestInDto.setRequester(requester);
        itemRequestInDto.setCreated(created);

        assertEquals(id, itemRequestInDto.getId());
        assertEquals(description, itemRequestInDto.getDescription());
        assertEquals(requester, itemRequestInDto.getRequester());
        assertEquals(created, itemRequestInDto.getCreated());
    }
}

