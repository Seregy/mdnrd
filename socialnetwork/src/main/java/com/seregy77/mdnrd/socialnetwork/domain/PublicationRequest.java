package com.seregy77.mdnrd.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PublicationRequest {
    private List<Object> comments;
    private String description;
    private long id;
    private byte[] image;
    private Date timestamp;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("category_id")
    private long categoryId;
}
