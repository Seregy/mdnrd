package com.seregy77.mdnrd.socialnetwork.service;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.seregy77.mdnrd.socialnetwork.domain.CommentEntity;
import com.seregy77.mdnrd.socialnetwork.domain.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ODatabaseSession databaseSession;
    private final PublicationService publicationService;
    private final UserService userService;

    public boolean addComment(long publicationId, CommentRequest request) {
        Optional<OVertex> publicationOptional = publicationService.getPublicationById(publicationId)
                .flatMap(OElement::asVertex);

        if (!publicationOptional.isPresent()) {
            return false;
        }

        OVertex publication = publicationOptional.get();
        List<OElement> comments = publication.getProperty("comments");
        Optional<OElement> newCommentOptional = createComment(request.getId(), request.getText(), request.getUserId(), request.getChildren());

        if (!newCommentOptional.isPresent()) {
            return false;
        }

        OElement newComment = newCommentOptional.get();

        Long parentId = request.getParentId();

        if (parentId == null) {
            comments.add(newComment);
            publication.save();
            return true;
        }

        boolean added = addCommentToParent(comments, parentId, newComment);
        if (added) {
            publication.save();
        }
        return added;
    }

    private boolean addCommentToParent(List<OElement> comments, Long parentId, OElement newComment) {
        Optional<OElement> parentOptional = getParentComment(comments, parentId);

        if (!parentOptional.isPresent()) {
            return true;
        }
        OElement parent = parentOptional.get();
        List<OElement> children = parent.getProperty("children");
        children.add(newComment);
        return false;
    }

    private Optional<OElement> getParentComment(List<OElement> comments, Long parentId) {
        List<CommentEntity> commentEntities = comments.stream()
                .map(CommentEntity::new)
                .collect(Collectors.toList());

        Optional<OElement> parent = commentEntities.stream()
                .filter(c -> c.getId() == parentId)
                .findFirst()
                .map(CommentEntity::getOElement);

        if (parent.isPresent()) {
            return parent;
        }

        return commentEntities.stream()
                .map(CommentEntity::getChildren)
                .flatMap(List::stream)
                .filter(c -> c.getId() == parentId)
                .findFirst()
                .map(CommentEntity::getOElement);
    }

    private Optional<OElement> createComment(long id, String text, long userId, List<?> children) {
        Optional<OVertex> userVertex = userService.getUserById(userId)
                .flatMap(OElement::asVertex);

        if (!userVertex.isPresent()) {
            return Optional.empty();
        }

        OElement comment = databaseSession.newElement("Comment");
        comment.setProperty("children", children);
        comment.setProperty("id", id);
        comment.setProperty("text", text);
        comment.setProperty("user", userVertex.get());
        comment.save();
        return Optional.of(comment);
    }
}
