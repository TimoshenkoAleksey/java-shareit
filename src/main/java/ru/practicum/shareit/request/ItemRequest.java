package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jfr.Timestamp;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private Timestamp created;
}
