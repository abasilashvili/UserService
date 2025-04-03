package com.abasilashvili.user_service.dto.event;

import com.abasilashvili.user_service.entity.event.EventStatus;
import com.abasilashvili.user_service.entity.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilterDto {

    private String titlePattern;
    private String descriptionPattern;
    private String locationPattern;
    private int maxAttendees;
    private EventType type;
    private EventStatus status;
}
