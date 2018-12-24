package com.seregy77.mdnrd.socialnetwork.service;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.seregy77.mdnrd.socialnetwork.domain.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ODatabaseSession databaseSession;

    public boolean createUser(UserRequest userRequest) {
        databaseSession.activateOnCurrentThread();

        OVertex user = databaseSession.newVertex("User");
        user.setProperty("password", userRequest.getPassword());
        user.setProperty("registration_date", userRequest.getRegistrationDate());
        user.setProperty("surname", userRequest.getSurname());
        user.setProperty("name", userRequest.getName());
        user.setProperty("id", userRequest.getId());
        user.setProperty("email", userRequest.getEmail());
        user.setProperty("username", userRequest.getUsername());
        user.setProperty("image", userRequest.getImage());
        user.save();

        return true;
    }

    public List<OElement> getUsers() {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM User";
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

    public Optional<OElement> getUserByUsername(String username) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM User WHERE username = ?";

        try (OResultSet rs = databaseSession.query(statement, username)) {
            if (rs.hasNext()) {
                return rs.next().getElement();
            }
        }

        return Optional.empty();
    }

    public Optional<OElement> getUserById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM User WHERE id = ?";

        try (OResultSet rs = databaseSession.query(statement, id)) {
            if (rs.hasNext()) {
                return rs.next().getElement();
            }
        }

        return Optional.empty();
    }

    public boolean deleteUserById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "DELETE VERTEX User WHERE id = ?";

        try (OResultSet rs = databaseSession.command(statement, id)) {
            if (rs.hasNext()) {
                return true;
            }
        }

        return false;
    }

    public List<OElement> getRecommendedUsers(long userId) {
        databaseSession.activateOnCurrentThread();

        String statement = "MATCH {class: User, where: (id = ?)}" +
                " -Subscribes-> {as: user, where: ($depth > 0), while: ($depth < 3)}" +
                " RETURN user.id, user.username, user.image";
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

    public boolean subscribeToUser(long subsriberId, long subscribedId) {
        return addEdgeBetweenUsers(subsriberId, subscribedId, "Subscribes");
    }

    public boolean blockUser(long blockerId, long blockedId) {
        return addEdgeBetweenUsers(blockerId, blockedId, "Blocks");
    }

    private boolean addEdgeBetweenUsers(long user1Id, long user2Id, String edgeType) {
        Optional<OElement> user1 = getUserById(user1Id);
        Optional<OElement> user2 = getUserById(user2Id);

        if (!user1.isPresent() || !user2.isPresent()) {
            return false;
        }

        Optional<OVertex> user1VertexOptional = user1.get().asVertex();
        Optional<OVertex> user2VertexOptional = user2.get().asVertex();

        if (!user1VertexOptional.isPresent() || !user2VertexOptional.isPresent()) {
            return false;
        }

        OVertex user1Vertex = user1VertexOptional.get();
        OVertex user2Vertex = user2VertexOptional.get();

        user1Vertex.addEdge(user2Vertex, edgeType);
        user1Vertex.save();
        user2Vertex.save();
        return true;
    }
}
