package com.example.typicode;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitConfig {

    @GET("posts")
    Call<List<Post>> getAllPosts();

    @POST("posts")
    Call<Post> createPost(@Body Post newPost);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Path("id") String postId, @Body Post newPost);

    @DELETE("posts/{id}")
    Call<Post> deletePost(@Path("id") String postId);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getAllComments(@Path("id") String postId);
}
