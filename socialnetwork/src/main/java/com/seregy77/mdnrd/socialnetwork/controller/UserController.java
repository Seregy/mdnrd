package com.seregy77.mdnrd.socialnetwork.controller;

import com.orientechnologies.orient.core.record.OElement;
import com.seregy77.mdnrd.socialnetwork.domain.Json;
import com.seregy77.mdnrd.socialnetwork.domain.UserRequest;
import com.seregy77.mdnrd.socialnetwork.service.PublicationService;
import com.seregy77.mdnrd.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PublicationService publicationService;

    @PostMapping
    public boolean createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    public ResponseEntity<List<Json>> getAllUsers() {
        List<OElement> users = userService.getUsers();

        return ResponseEntity.ok(Json.fromOElements(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Json> getUserById(@PathVariable("id") long id) {
        Optional<OElement> user = userService.getUserById(id);

        return ResponseEntity.of(user.map(Json::fromOElement));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Json> findUserByName(@PathVariable("name") String name) {
        Optional<OElement> user = userService.getUserByUsername(name);

        return ResponseEntity.of(user.map(Json::fromOElement));
    }

    @DeleteMapping("/{id}")
    public boolean deleteUserById(@PathVariable("id") long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/{id}/recommendations/publications")
    public ResponseEntity<List<Json>> getRecommendedPublications(@PathVariable("id") long id) {
        List<OElement> users = publicationService.getRecommendedPublications(id);

        return ResponseEntity.ok(Json.fromOElements(users));
    }

    @GetMapping("/{id}/recommendations/users")
    public ResponseEntity<List<Json>> getRecommendedUsers(@PathVariable("id") long id) {
        List<OElement> users = userService.getRecommendedUsers(id);

        return ResponseEntity.ok(Json.fromOElements(users));
    }

    @PutMapping("/{id}/subscribe/{another-id}")
    public boolean subscribeToUser(@PathVariable("id") long id,
                                                          @PathVariable("another-id") long anotherId) {
        return userService.subscribeToUser(id, anotherId);
    }

    @PutMapping("/{id}/block/{another-id}")
    public boolean blockUser(@PathVariable("id") long id,
                                       @PathVariable("another-id") long anotherId) {
        return userService.blockUser(id, anotherId);
    }
}
