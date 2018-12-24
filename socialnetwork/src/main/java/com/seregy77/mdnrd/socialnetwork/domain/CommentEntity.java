package com.seregy77.mdnrd.socialnetwork.domain;

import com.orientechnologies.orient.core.record.OElement;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentEntity {
    private long id;
    private String text;
    private OElement user;
    private List<CommentEntity> children;
    private OElement oElement;

    public CommentEntity(OElement commentElement) {
        this.id = commentElement.getProperty("id");
        this.text = commentElement.getProperty("text");
        this.user = commentElement.getProperty("user");
        List<OElement> rawChildren = commentElement.getProperty("children");
        this.children = rawChildren.stream()
                .map(CommentEntity::new)
                .collect(Collectors.toList());
        this.oElement = commentElement;
    }
}
