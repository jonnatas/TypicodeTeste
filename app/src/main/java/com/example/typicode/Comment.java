package com.example.typicode;

import androidx.annotation.NonNull;

public class Comment {
    private String postId;
    private String id;
    private String email;
    private String name;
    private String body;

    public Comment(String postId, String id, String email, String name, String body) {
        this.postId = postId;
        this.email = email;
        this.name = name;
        this.body = body;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @NonNull
    @Override
    public String toString() {
        return getEmail() + "\n\n" + getBody();
    }
}
