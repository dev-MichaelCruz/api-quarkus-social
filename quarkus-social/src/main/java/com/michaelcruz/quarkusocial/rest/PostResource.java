package com.michaelcruz.quarkusocial.rest;

import com.michaelcruz.quarkusocial.domain.model.Post;
import com.michaelcruz.quarkusocial.domain.model.User;
import com.michaelcruz.quarkusocial.domain.repository.PostRepository;
import com.michaelcruz.quarkusocial.domain.repository.UserRepository;
import com.michaelcruz.quarkusocial.rest.dto.CreatePostRequest;
import com.michaelcruz.quarkusocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

        PanacheQuery<Post> query = postRepository
                .find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        var listPost = query.list();

        var postResponseList = listPost.stream()
                .map(PostResponse::fromEntity) //.map( post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }

}
