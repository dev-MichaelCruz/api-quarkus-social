package com.michaelcruz.quarkusocial.rest;

import com.michaelcruz.quarkusocial.domain.model.User;
import com.michaelcruz.quarkusocial.domain.repository.UserRepository;
import com.michaelcruz.quarkusocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setup() {

        //Default user
        var user = new User();
        user.setName("Fulano");
        user.setAge(32);
        userRepository.persist(user);
        userId = user.getId();

        //Follower
        var follower = new User();
        follower.setName("Ciclano");
        follower.setAge(31);
        userRepository.persist(follower);
        followerId = follower.getId();
    }

    @Test
    @DisplayName("should return 409 when followerId is equal to userId")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You Can't Follow Yourself"));
    }

    @Test
    @DisplayName("should return 404 when userId doesn't exists")
    public void userNotFoundTest() {

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var nonexistentUserId = 0;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", nonexistentUserId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should follow a user")
    public void followAUserTest() {
        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}