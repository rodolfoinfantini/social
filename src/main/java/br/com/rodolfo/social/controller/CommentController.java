package br.com.rodolfo.social.controller;

import br.com.rodolfo.social.exception.NotFoundException;
import br.com.rodolfo.social.forms.CommentForm;
import br.com.rodolfo.social.model.Comment;
import br.com.rodolfo.social.service.CommentService;
import br.com.rodolfo.social.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Comment> createComment(@RequestHeader("Authorization") String token, @PathVariable("postId") String postId, @RequestBody CommentForm commentForm) throws NotFoundException {
        postService.get(postId);
        Comment comment = commentService.create(token, commentForm.getMessage());
        postService.comment(postId, comment);
        comment.hidePasswords();
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/reply/{commentId}")
    public ResponseEntity<Comment> replyComment(@RequestHeader("Authorization") String token, @PathVariable("commentId") String commentId, @RequestBody CommentForm commentForm) throws NotFoundException {
        Comment comment = commentService.reply(token, commentId, commentForm.getMessage());
        comment.hidePasswords();
        return ResponseEntity.ok(comment);
    }
}