package com.michaelcruz.quarkusocial.rest;

import com.michaelcruz.quarkusocial.domain.model.Post;
import com.michaelcruz.quarkusocial.domain.model.User;
import com.michaelcruz.quarkusocial.domain.repository.PostRepository;
import com.michaelcruz.quarkusocial.domain.repository.UserRepository;
import com.michaelcruz.quarkusocial.rest.dto.CreatePostRequest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {
        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId) {

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

}
