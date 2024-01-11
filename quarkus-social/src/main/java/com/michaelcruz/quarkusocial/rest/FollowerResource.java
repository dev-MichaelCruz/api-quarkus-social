package com.michaelcruz.quarkusocial.rest;

import com.michaelcruz.quarkusocial.domain.model.Follower;
import com.michaelcruz.quarkusocial.domain.model.User;
import com.michaelcruz.quarkusocial.domain.repository.FollowerRepository;
import com.michaelcruz.quarkusocial.domain.repository.UserRepository;
import com.michaelcruz.quarkusocial.rest.dto.FollowerRequest;
import com.michaelcruz.quarkusocial.rest.dto.FollowerResponse;
import com.michaelcruz.quarkusocial.rest.dto.FollowersPerUserResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest followerRequest) {

        if(userId.equals(followerRequest.getFollowerId())){
            return Response.status(Response.Status.CONFLICT).entity("You Can't Follow Yourself").build();
        }

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerRequest.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);

        if(!follows){
            var newFollower = new Follower();
            newFollower.setUser(user);
            newFollower.setFollower(follower);

            followerRepository.persist(newFollower);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

        List<Follower> list = followerRepository.findByUser(userId);

        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList = list.stream()
                .map(FollowerResponse::new)
                .toList();

        responseObject.setContent(followerList);

        return Response.ok(responseObject).build();
    }


    @DELETE
    @Transactional
    public Response unfollowUser(
            @PathParam("userId") Long userId,
            @QueryParam("followerId") Long followerId){

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);


        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
