package com.seregy77.mdnrd.socialnetwork.service;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.seregy77.mdnrd.socialnetwork.domain.PublicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublicationService {
    private final ODatabaseSession databaseSession;
    private final UserService userService;
    private final CategoryService categoryService;

    public boolean createPublication(PublicationRequest publicationRequest) {
        databaseSession.activateOnCurrentThread();

        OVertex publication = databaseSession.newVertex("Publication");
        publication.setProperty("comments", publicationRequest.getComments());
        publication.setProperty("description", publicationRequest.getDescription());
        publication.setProperty("id", publicationRequest.getId());
        publication.setProperty("image", publicationRequest.getImage());
        publication.setProperty("timestamp", publicationRequest.getTimestamp());


        Optional<OVertex> user = userService.getUserById(publicationRequest.getUserId())
                .flatMap(OElement::asVertex);
        if (user.isPresent()) {
            OVertex actualUser = user.get();
            OEdge edge = actualUser.addEdge(publication, "Creates");
            edge.save();
            actualUser.save();
        }

        Optional<OVertex> category = categoryService.getCategoryById(publicationRequest.getCategoryId())
                .flatMap(OElement::asVertex);
        if (category.isPresent()) {
            OVertex actualCategory = category.get();
            OEdge edge = publication.addEdge(actualCategory, "BelongsTo");
            edge.save();
            actualCategory.save();
        }

        publication.save();
        return true;
    }

    public List<OElement> getPublications() {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM Publication";
        try (OResultSet rs = databaseSession.query(statement)) {
            List<OElement> results = new ArrayList<>();
            while (rs.hasNext()) {
                rs.next()
                        .getElement()
                        .ifPresent(results::add);
            }

            return results;
        }
    }

    public List<OElement> getRecommendedPublications(long userId) {
        databaseSession.activateOnCurrentThread();

        String statement = "MATCH {class: User, where: (id = ?)}" +
                                   " -Subscribes-> {}" +
                                   " -Creates-> {as: publication}" +
                                   " RETURN publication.id, publication.description, publication.image";
        try (OResultSet rs = databaseSession.query(statement, userId)) {
            List<OElement> results = new ArrayList<>();
            while (rs.hasNext()) {
                OElement element = rs.next()
                                           .toElement();
                results.add(element);
            }

            return results;
        }
    }

    public List<OElement> getPublicationsByCategory(long categoryId) {
        databaseSession.activateOnCurrentThread();

        String statement = "MATCH {Class: Category, where: (id = ?)}" +
                ".in('BelongsTo'){as: publication}" +
                " RETURN publication.id, publication.description, publication.image";
        try (OResultSet rs = databaseSession.query(statement, categoryId)) {
            List<OElement> results = new ArrayList<>();
            while (rs.hasNext()) {
                OElement element = rs.next()
                        .toElement();
                results.add(element);
            }

            return results;
        }
    }

    public Optional<OElement> getPublicationById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM Publication WHERE id = ?";

        try (OResultSet rs = databaseSession.query(statement, id)) {
            if (rs.hasNext()) {
                return rs.next().getElement();
            }
        }

        return Optional.empty();
    }

    public Optional<OElement> getPublicationInfoById(long publicationId) {
        databaseSession.activateOnCurrentThread();

        String statement = "MATCH {class: Publication, as: publication, where: (id = ?)}" +
                ".out('Contains')" +
                "{as: info}" +
                " RETURN info.Camera, info.Make, info.model";
        try (OResultSet rs = databaseSession.query(statement, publicationId)) {
            if (rs.hasNext()) {
                OElement element = rs.next().toElement();
                return Optional.of(element);
            }
        }

        return Optional.empty();
    }

    public List<OElement> getPublicationCommentsById(long publicationId) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT expand(comments) FROM Publication WHERE id = ?";
        List<OElement> results = new ArrayList<>();
        try (OResultSet rs = databaseSession.query(statement, publicationId)) {
            while (rs.hasNext()) {
                OResult result = rs.next();
                OElement element = result.toElement();
                results.add(element);
            }
        }

        return results;
    }

    public boolean deletePublicationById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "DELETE VERTEX Publication WHERE id = ?";

        try (OResultSet rs = databaseSession.command(statement, id)) {
            if (rs.hasNext()) {
                return true;
            }
        }

        return false;
    }
}
