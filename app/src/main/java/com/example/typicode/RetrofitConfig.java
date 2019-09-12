package com.example.typicode;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitConfig {

    @GET("posts")
    Call<List<Post>> getAllPosts();

    @GET("posts")
    Call<List<Post>> getPostByQuery(
            @Query("userId") int userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getUsersPosts(
            @Query("userID") Integer[] users,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getUsersQueryMap(@QueryMap Map<String, String> parameters);

    @POST("posts")
    Call<Post> createPost(@Body Post newPost);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Path("id") String postId, @Body Post newPost);

    @DELETE("posts/{id}")
    Call<Post> deletePost(@Path("id") String postId);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getAllComments(@Path("id") String postId);

}
