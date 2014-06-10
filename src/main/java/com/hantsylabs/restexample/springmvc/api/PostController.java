package com.hantsylabs.restexample.springmvc.api;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hantsylabs.restexample.springmvc.Constants;
import com.hantsylabs.restexample.springmvc.model.Comment;
import com.hantsylabs.restexample.springmvc.model.Post;
import com.hantsylabs.restexample.springmvc.repository.CommentRepository;
import com.hantsylabs.restexample.springmvc.repository.PostRepository;

@RestController
@RequestMapping(value = Constants.URI_API)
public class PostController {
	private static final Logger log = LoggerFactory
			.getLogger(PostController.class);

	@Inject
	private PostRepository postRepository;

	@Inject
	private CommentRepository commentRepository;

	@RequestMapping(value = "/posts", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Post>> getAllPosts() {
		if (log.isDebugEnabled()) {
			log.debug("get all postsinfo");
		}

		Sort createdOnDesc = new Sort(Direction.DESC, "createdOn");
		List<Post> posts = postRepository.findAll(createdOnDesc);

		if (log.isDebugEnabled()) {
			log.debug("get posts size @" + posts.size());
		}

		return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
	}

	@RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
		if (log.isDebugEnabled()) {
			log.debug("get postsinfo by id @" + id);
		}

		Post post = postRepository.findOne(id);

		if (log.isDebugEnabled()) {
			log.debug("get post @" + post);
		}

		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}

	@RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Comment>> getCommentsOfPost(
			@PathVariable("id") Long id) {
		if (log.isDebugEnabled()) {
			log.debug("get comments of post@" + id);
		}

		List<Comment> commentsOfPost = commentRepository.findByPostId(id);

		if (log.isDebugEnabled()) {
			log.debug("get post @" + commentsOfPost.size());
		}

		return new ResponseEntity<List<Comment>>(commentsOfPost, HttpStatus.OK);
	}

	@RequestMapping(value = "/posts", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AlertMessage> createPost(@RequestBody Post post) {
		if (log.isDebugEnabled()) {
			log.debug("create a new post");
		}

		Post saved = postRepository.save(post);

		if (log.isDebugEnabled()) {
			log.debug("saved post id is @" + saved.getId());
		}

		return new ResponseEntity<AlertMessage>(
				AlertMessage.success("Post created successfully!"),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<AlertMessage> deletePost(@PathVariable("id") Long id) {
		if (log.isDebugEnabled()) {
			log.debug("delete post by id @" + id);
		}

		postRepository.delete(id);

		return new ResponseEntity<>(AlertMessage.success("Post was deleted successfully!"), HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> createCommentOfPost(
			@PathVariable("id") Long id, @RequestBody Comment comment) {
		if (log.isDebugEnabled()) {
			log.debug("new comment of post@" + id);
		}

		Post post = postRepository.findOne(id);

		comment.setPost(post);

		Comment saved = commentRepository.save(comment);

		if (log.isDebugEnabled()) {
			log.debug("saved comment @" + saved.getId());
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
