package com.seregy77.mdnrd.socialnetwork.controller;

import com.orientechnologies.orient.core.record.OElement;
import com.seregy77.mdnrd.socialnetwork.domain.CategoryRequest;
import com.seregy77.mdnrd.socialnetwork.domain.Json;
import com.seregy77.mdnrd.socialnetwork.domain.PublicationRequest;
import com.seregy77.mdnrd.socialnetwork.service.CategoryService;
import com.seregy77.mdnrd.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public boolean createUser(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    public ResponseEntity<List<Json>> getAllCategories() {
        List<OElement> categories = categoryService.getCategories();

        return ResponseEntity.ok(Json.fromOElements(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Json> getCategoryById(@PathVariable("id") long id) {
        Optional<OElement> category = categoryService.getCategoryById(id);

        return ResponseEntity.of(category.map(Json::fromOElement));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Json> findCategoryByName(@PathVariable("name") String name) {
        Optional<OElement> user = categoryService.getCategoryByName(name);

        return ResponseEntity.of(user.map(Json::fromOElement));
    }

    @DeleteMapping("/{id}")
    public boolean deleteCategoryById(@PathVariable("id") long id) {
        return categoryService.deleteCategoryById(id);
    }
}