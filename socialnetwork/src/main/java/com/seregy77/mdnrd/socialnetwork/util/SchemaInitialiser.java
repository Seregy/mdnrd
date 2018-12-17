package com.seregy77.mdnrd.socialnetwork.util;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class SchemaInitialiser {
    private final ODatabaseSession databaseSession;

    public SchemaInitialiser(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;

        initCategory();
        initImageInfo();
        initPublication();
        initUser();
        initComment();
        initEdges();

        new DataInitialiser(databaseSession);
    }

    private void initCategory() {
        OClass category = getOrCreateVertex("Category");

        createProperty(category, "description", OType.STRING);
        createProperty(category, "id", OType.LONG);
        createProperty(category, "name", OType.STRING);
    }

    private void initImageInfo() {
        OClass imageInfo = getOrCreateVertex("Image_Info");
    }

    private void initPublication() {
        OClass publication = getOrCreateVertex("Publication");

        createProperty(publication, "comments", OType.EMBEDDEDLIST);
        createProperty(publication, "description", OType.STRING);
        createProperty(publication, "id", OType.LONG);
        createProperty(publication, "image", OType.BINARY);
        createProperty(publication, "timestamp", OType.DATETIME);
    }

    private void initUser() {
        OClass user = getOrCreateVertex("User");

        createProperty(user, "email", OType.STRING);
        createProperty(user, "id", OType.LONG);
        createProperty(user, "image", OType.BINARY);
        createProperty(user, "password", OType.STRING);
        createProperty(user, "registration_date", OType.DATETIME);
        createProperty(user, "username", OType.STRING);
    }

    private void initComment() {
        OClass comment = getOrCreateClass("Comment");

        createProperty(comment, "children", OType.EMBEDDEDLIST);
        createProperty(comment, "id", OType.LONG);
        createProperty(comment, "text", OType.STRING);
        createProperty(comment, "user", OType.LINK);
    }

    private void initEdges() {
        OClass belongsTo = getOrCreateEdge("BelongsTo");
        OClass blocks = getOrCreateEdge("Blocks");
        OClass contains = getOrCreateEdge("Contains");
        OClass creates = getOrCreateEdge("Creates");
        OClass dislikes = getOrCreateEdge("Dislikes");
        OClass likes = getOrCreateEdge("Likes");
        OClass isChild = getOrCreateEdge("IsChild");
        OClass subscribes = getOrCreateEdge("Subscribes");
    }

    private OClass getOrCreateVertex(String name) {
        OClass type = databaseSession.getClass(name);
        if (type != null) {
            return type;
        }

        return databaseSession.createVertexClass(name);
    }

    private OClass getOrCreateEdge(String name) {
        OClass type = databaseSession.getClass(name);
        if (type != null) {
            return type;
        }

        return databaseSession.createEdgeClass(name);
    }

    private OClass getOrCreateClass(String name) {
        return databaseSession.createClassIfNotExist(name);
    }

    private void createProperty(OClass oClass, String name, OType type) {
        if(oClass.getProperty(name) == null) {
            oClass.createProperty(name, type);
        }
    }
}
