package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.mapper.BookingCastomMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @InjectMocks
    BookingController bookingController;
    @MockBean
    BookingServiceImpl bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    AllBookingsAsList allBookingsAsList;
    BookingDto bookingDto;
    Booking booking;
    User user;
    UserDto userDto;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        BookingCastomMapper bcm = new BookingCastomMapper();
        userDto = new UserDto(1L, "userName", "user@email.ru");
        itemDto = new ItemDto(1L, "item1", "description1", true, null);

        user = new User(1L, "user name", "user@email.ru");
        item = new Item(1L, "item1", "description1", true, user, null);
        allBookingsAsList = new AllBookingsAsList(1L,
                LocalDateTime.of(2022, 10, 16, 21, 22, 22),
                LocalDateTime.of(2022, 10, 17, 21, 22, 22),
                StatusOfBooking.WAITING, item, user);

        bookingDto = new BookingDto(1L,
                LocalDateTime.of(2022, 10, 16, 21, 22, 22),
                LocalDateTime.of(2022, 10, 17, 21, 22, 22),
                StatusOfBooking.WAITING, itemDto, userDto);
        booking = bcm.bookingFromDto(bookingDto);
    }

    @Test
    void add() throws Exception {
        when(bookingService.create(anyLong(), any()))
                .thenReturn(bookingDto);


        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));

        verify(bookingService, times(1)).create(anyLong(), any());
    }

    @Test
    void approve() throws Exception {
        bookingDto.setStatus(StatusOfBooking.APPROVED);
        when(bookingService.book(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));

        verify(bookingService, times(1)).book(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getById() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/888")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));

        verify(bookingService, times(1)).get(anyLong(), anyLong());
    }


    @Test
    void getByOwner() throws Exception {
        when(bookingService.getAllBookingForOwner(anyLong(), any(), any()))
                .thenReturn(List.of(allBookingsAsList));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(allBookingsAsList.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(allBookingsAsList.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(allBookingsAsList.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(allBookingsAsList.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(allBookingsAsList.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(allBookingsAsList.getStatus().toString())));

        verify(bookingService, times(1)).getAllBookingForOwner(anyLong(), any(), any());
    }

    @Test
    void getByUser() throws Exception {
        when(bookingService.getAllForUserByState(anyLong(), any(), any()))
                .thenReturn(List.of(allBookingsAsList));

        mvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(allBookingsAsList.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(allBookingsAsList.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(allBookingsAsList.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(allBookingsAsList.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(allBookingsAsList.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(allBookingsAsList.getStatus().toString())));

        verify(bookingService, times(1)).getAllForUserByState(anyLong(), any(), any());
    }

    @Test
    void testGetAllBookingForOwner_InvalidFromValue() {
        long userId = 1L;
        String state = "ALL";
        int from = -1;
        int size = 10;

        assertThrows(IllegalStateException.class, () -> {
            bookingController.getAllBookingForOwner(userId, state, from, size);
        });
    }

    @Test
    void testGetAllForUserByState_InvalidFromValue() {
        // Arrange
        long userId = 1L;
        String state = "ALL";
        int from = -1;
        int size = 10;

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bookingController.getAllForUserByState(userId, state, from, size);
        });
    }
}