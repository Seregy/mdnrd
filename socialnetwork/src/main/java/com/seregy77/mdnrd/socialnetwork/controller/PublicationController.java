package com.seregy77.mdnrd.socialnetwork.controller;

import com.orientechnologies.orient.core.record.OElement;
import com.seregy77.mdnrd.socialnetwork.domain.Json;
import com.seregy77.mdnrd.socialnetwork.domain.PublicationRequest;
import com.seregy77.mdnrd.socialnetwork.domain.UserRequest;
import com.seregy77.mdnrd.socialnetwork.service.PublicationService;
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
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationService publicationService;

    @PostMapping("/publications")
    public boolean createUser(@RequestBody PublicationRequest request) {
        return publicationService.createPublication(request);
    }

    @GetMapping("/publications")
    public ResponseEntity<List<Json>> getAllPublications() {
        List<OElement> publications = publicationService.getPublications();

        return ResponseEntity.ok(Json.fromOElements(publications));
    }

    @GetMapping("/publications/{id}")
    public ResponseEntity<Json> getPublicationById(@PathVariable("id") long id) {
        Optional<OElement> publication = publicationService.getPublicationById(id);

        return ResponseEntity.of(publication.map(Json::fromOElement));
    }

    @GetMapping("/publications/{id}/info")
    public ResponseEntity<Json> getPublicationInfoById(@PathVariable("id") long id) {
        Optional<OElement> publication = publicationService.getPublicationInfoById(id);

        return ResponseEntity.of(publication.map(Json::fromOElement));
    }

    @GetMapping("/publications/{id}/comments")
    public ResponseEntity<List<Json>> getPublicationCommentsById(@PathVariable("id") long id) {
        List<OElement> comments = publicationService.getPublicationCommentsById(id);

        return ResponseEntity.ok(Json.fromOElements(comments));
    }

    @GetMapping("categories/{category-id}/publications")
    public ResponseEntity<List<Json>> getPublicationsByCategoryId(@PathVariable("category-id") long categoryId) {
        List<OElement> publications = publicationService.getPublicationsByCategory(categoryId);

        return ResponseEntity.ok(Json.fromOElements(publications));
    }

    @DeleteMapping("/publications/{id}")
    public boolean deletePublicationById(@PathVariable("id") long id) {
        return publicationService.deletePublicationById(id);
    }
}
