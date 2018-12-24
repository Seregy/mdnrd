package com.seregy77.mdnrd.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommentRequest {
    private long id;
    @JsonProperty("parent_id")
    private Long parentId;
    private String text;
    private long userId;
    private List<CommentRequest> children;
}
