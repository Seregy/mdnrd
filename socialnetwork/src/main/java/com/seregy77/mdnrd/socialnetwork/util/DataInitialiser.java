package com.seregy77.mdnrd.socialnetwork.util;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInitialiser {
    private final ODatabaseSession databaseSession;
    private OVertex user1;
    private OVertex user2;
    private OVertex user3;
    private OVertex user4;
    private OVertex user5;

    private OVertex animals;
    private OVertex wild;

    public DataInitialiser(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;

        initCategory();
        initUser();
        initPublication();
    }

    private void initCategory() {
        animals = createCategory("Animals", "Photos of wild and domestic animals", 1);
        wild = createCategory("Wild animals", "Photos of wild animals only", 2);
        wild.addEdge(animals, "IsChild");

        OVertex tigers = createCategory("Tigers", "Photos of wild tigers only", 3);
        tigers.addEdge(wild, "IsChild");

        createCategory("Other", "Miscellaneous photos", 4);
    }

    private void initUser() {
        user1 = createUser("pass1", LocalDateTime.now(), "surn1", "name1", 1, "email1", "username1", "image".getBytes());
        user2 = createUser("pass2", LocalDateTime.now(), "surn2", "name2", 2, "email2", "username2", "image2".getBytes());
        user1.addEdge(user2, "Subscribes");

        user3 = createUser("pass3", LocalDateTime.now(), "surn3", "name3", 3, "email3", "username3", "image3".getBytes());
        user2.addEdge(user3, "Subscribes");

        user4 = createUser("pass4", LocalDateTime.now(), "surn4", "name4", 4, "email4", "username4", "image4".getBytes());
        user3.addEdge(user4, "Blocks");

        user5 = createUser("pass5", LocalDateTime.now(), "surn5", "name5", 5, "email5", "username5", "image5".getBytes());
        user5.addEdge(user4, "Blocks");
    }

    private void initPublication() {
        Map<String, String> goldenMetadata = new HashMap<>();
        goldenMetadata.put("File Size", "268,384 bytes (262.1 KB)");
        goldenMetadata.put("Dimensions", "1024 x 765 pixels");
        goldenMetadata.put("Camera", "Nickon");
        goldenMetadata.put("model", "D3100");
        OVertex golden = createPublication("Some golden retrievers", 1,
                LocalDateTime.of(2018, 5, 24, 18, 50, 55),
                "golden image".getBytes(),
                Collections.emptyList());
        OVertex goldenInfo = createImageInfo(goldenMetadata);
        golden.addEdge(goldenInfo, "Contains");
        user2.addEdge(golden, "Creates");
        golden.addEdge(animals, "BelongsTo");
        user2.save();
        golden.save();

        Map<String, String> eagleMetadata = new HashMap<>();
        eagleMetadata.put("Make", "Apple");
        eagleMetadata.put("model", "iPhone 4");
        eagleMetadata.put("GPS Coordinates", "43.74017, 7.43");
        OVertex eagle = createPublication("Majestic spotted eagle", 2,
                LocalDateTime.of(2018, 11, 5, 22, 50, 0),
                "eagle image".getBytes(),
                Collections.singletonList(createComment(Collections.singletonList(createComment(Collections.emptyList(), 2, "some child comment", user1)), 1, "some root comment", user2)));
        OVertex eagleInfo = createImageInfo(eagleMetadata);
        eagle.addEdge(eagleInfo, "Contains");
        user5.addEdge(eagle, "Creates");
        eagle.addEdge(wild, "BelongsTo");
        user5.save();
        eagle.save();
    }

    private OVertex createCategory(String name, String description, long id) {
        OVertex category = databaseSession.newVertex("Category");
        category.setProperty("name", name);
        category.setProperty("description", description);
        category.setProperty("id", id);
        category.save();
        return category;
    }

    private OVertex createPublication(String description, long id, LocalDateTime timestamp, byte[] image, List<Object> comments) {
        OVertex publication = databaseSession.newVertex("Publication");
        publication.setProperty("comments", comments);
        publication.setProperty("description", description);
        publication.setProperty("id", id);
        publication.setProperty("image", image);
        publication.setProperty("timestamp", Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant()));
        publication.save();
        return publication;
    }

    private OVertex createImageInfo(Map<String, String> metadata) {
        OVertex imageInfo = databaseSession.newVertex("Image_Info");

        for (Map.Entry<String, String> property : metadata.entrySet()) {
            imageInfo.setProperty(property.getKey(), property.getValue());
        }
        imageInfo.save();
        return imageInfo;
    }

    private OElement createComment(List<Object> children, long id, String text, OElement user) {
        OElement comment = databaseSession.newElement("Comment");
        comment.setProperty("children", children);
        comment.setProperty("id", id);
        comment.setProperty("text", text);
        comment.setProperty("user", user);
        comment.save();
        return comment;
    }

    private OVertex createUser(String password, LocalDateTime registrationDate, String surname, String name, long id, String email, String username, byte[] image) {
        OVertex user = databaseSession.newVertex("User");
        user.setProperty("password", password);
        user.setProperty("registration_date", Date.from(registrationDate.atZone(ZoneId.systemDefault()).toInstant()));
        user.setProperty("surname", surname);
        user.setProperty("name", name);
        user.setProperty("id", id);
        user.setProperty("email", email);
        user.setProperty("username", username);
        user.setProperty("image", image);
        user.save();
        return user;
    }
}
