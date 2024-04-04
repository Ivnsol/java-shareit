package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestControllerGateWay {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(HEADER_USER_ID) Long userId,
                                      @Validated @RequestBody ItemRequestInDto requestInDto) {
        return requestClient.add(userId, requestInDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getByOwner(@RequestHeader(HEADER_USER_ID) long userId) {
        return requestClient.getByOwner(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) long userId,
                                          @PathVariable long requestId) {
        return requestClient.getById(userId, requestId);
    }

    @SuppressWarnings("checkstyle:Regexp")
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(HEADER_USER_ID) long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                         Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10")
                                         Integer size) {

        return requestClient.getAll(userId, from, size);
    }
}