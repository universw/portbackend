package com.portfo.backend.controller;

import com.portfo.backend.model.Comment;
import com.portfo.backend.repository.CommentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentRepository repository;

    // Constructor injection (modern and testable)
    public CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    // Get all comments, newest first
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
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Admin replies to a comment by ID
    @PatchMapping("/{id}/reply")
    public Comment addAdminReply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Comment comment = repository.findById(id).orElseThrow();
        comment.setAdminReply(body.get("reply"));
        return repository.save(comment);
    }
}