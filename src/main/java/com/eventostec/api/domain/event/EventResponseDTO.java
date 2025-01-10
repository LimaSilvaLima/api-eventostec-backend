package com.eventostec.api.domain.event;

import java.sql.Date;
import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, Date date, String city, String state, Boolean remote, String eventUrl, String imgUrl) {

    public EventResponseDTO(UUID id2, String title2, String description2, java.util.Date date2, String string,
            String string2, Boolean remote2, String eventUrl2, String imgUrl2) {
        //TODO Auto-generated constructor stub
    }

    
}
