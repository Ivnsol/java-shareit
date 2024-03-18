package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private Date created;
}
