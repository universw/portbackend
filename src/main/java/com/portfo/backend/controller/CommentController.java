package com.portfo.backend.controller;

import com.portfo.backend.model.Comment;
import com.portfo.backend.repository.CommentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository repository;

    // Constructor injection (clean and testable)
    public CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    // Get all comments, sorted newest first
    @GetMapping
    public List<Comment> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    // Create a new comment
    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        comment.setTimestamp(LocalDateTime.now());
        return repository.save(comment);
    }

    // Delete a comment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Admin replies to a comment by ID
    @PatchMapping("/{id}/reply")
    public ResponseEntity<Comment> addAdminReply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repository.findById(id)
                .map(comment -> {
                    comment.setAdminReply(body.get("reply"));
                    return ResponseEntity.ok(repository.save(comment));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}