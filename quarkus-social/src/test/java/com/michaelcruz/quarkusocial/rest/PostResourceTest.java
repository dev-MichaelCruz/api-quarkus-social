package com.michaelcruz.quarkusocial.rest;

import com.michaelcruz.quarkusocial.domain.model.Follower;
import com.michaelcruz.quarkusocial.domain.model.Post;
import com.michaelcruz.quarkusocial.domain.model.User;
import com.michaelcruz.quarkusocial.domain.repository.FollowerRepository;
import com.michaelcruz.quarkusocial.domain.repository.PostRepository;
import com.michaelcruz.quarkusocial.domain.repository.UserRepository;
import com.michaelcruz.quarkusocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    //Deve ser criado um usuário antes de realizar os testes para não ocasionar erro
    @BeforeEach
    @Transactional
    public void setUp() {

        //Default user
        var user = new User();
        user.setName("Fulano");
        user.setAge(32);
        userRepository.persist(user);
        userId = user.getId();

        //Create a post for user
        Post post = new Post();
        post.setText("Hello, this is a user post");
        post.setUser(user);
        postRepository.persist(post);

        //User that follow no one
        var userNotFollower = new User();
        userNotFollower.setName("Ciclano");
        userNotFollower.setAge(33);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //User Follower
        var userFollower = new User();
        userFollower.setName("Beltrano");
        userFollower.setAge(33);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

    }


    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", userId)
        .when()
                .post()
        .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when try to make a post for an nonexistent user")
    public void postForAnNonexistentUserTest() {

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var nonexistentUserId = 0;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", nonexistentUserId)
        .when()
                .post()
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when user doesn't exists")
    public void listPostFollowerHeaderNotSendTest() {
        var nonexistentUserId = 0;

        given()
                .pathParam("userId", nonexistentUserId)
        .when()
                .get()
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostUserNotFoundTest() {

        given()
                .pathParam("userId", userId)
        .when()
                .get()
        .then()
                .statusCode(400)
                .body(Matchers.is("You Forgot The Header FollowerId"));

    }

    @Test
    @DisplayName("should return 404 when follower doesn't exists")
    public void listPostFollowerNotFoundTest() {

        var nonexistentFollowerId = 0;

        given()
                .pathParam("userId", userId)
                .header("followerId", nonexistentFollowerId)
        .when()
                .get()
        .then()
                .statusCode(404)
                .body(Matchers.is("Nonexistent FollowerId"));
    }

    @Test
    @DisplayName("should return 403 when follower isn't a follower")
    public void listPostNotAFollowerTest() {


        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
        .when()
                .get()
        .then()
                .statusCode(403)
                .body(Matchers.is("You Can't See This Posts"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostsTest() {
        given()
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }
}