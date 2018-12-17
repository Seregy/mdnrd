package com.seregy77.mdnrd.socialnetwork.service;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.seregy77.mdnrd.socialnetwork.domain.CategoryRequest;
import com.seregy77.mdnrd.socialnetwork.domain.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ODatabaseSession databaseSession;

    public boolean createCategory(CategoryRequest categoryRequest) {
        databaseSession.activateOnCurrentThread();

        OVertex category = databaseSession.newVertex("Category");
        category.setProperty("name", categoryRequest.getName());
        category.setProperty("description", categoryRequest.getDescription());
        category.setProperty("id", categoryRequest.getId());
        category.save();

        return true;
    }

    public List<OElement> getCategories() {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM Category";
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

    public Optional<OElement> getCategoryByName(String name) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM Category WHERE name = ?";

        try (OResultSet rs = databaseSession.query(statement, name)) {
            if (rs.hasNext()) {
                return rs.next().getElement();
            }
        }

        return Optional.empty();
    }

    public Optional<OElement> getCategoryById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "SELECT FROM Category WHERE id = ?";

        try (OResultSet rs = databaseSession.query(statement, id)) {
            if (rs.hasNext()) {
                return rs.next().getElement();
            }
        }

        return Optional.empty();
    }

    public boolean deleteCategoryById(long id) {
        databaseSession.activateOnCurrentThread();

        String statement = "DELETE VERTEX Category WHERE id = ?";

        try (OResultSet rs = databaseSession.command(statement, id)) {
            if (rs.hasNext()) {
                return true;
            }
        }

        return false;
    }
}