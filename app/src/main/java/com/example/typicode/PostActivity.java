package com.example.typicode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonAlterar;
    private Button buttonDeletarPost;

    private ListView listViewComments;
    private List<Comment> comments;
    private ArrayAdapter<Comment> commentsAdapter;

    private Retrofit retrofit;
    private RetrofitConfig retrofitConfig;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        extras = getIntent().getExtras();

        retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitConfig = retrofit.create(RetrofitConfig.class);

        editTextBody = findViewById(R.id.editTextBodyDetailId);
        editTextTitle = findViewById(R.id.editTextTitleDetailId);
        buttonAlterar = findViewById(R.id.buttonConfirmarSalvarPostId);
        buttonDeletarPost = findViewById(R.id.buttonDeletarPostId);
        listViewComments = findViewById(R.id.listViewCommentsId);

        editTextTitle.setText(extras.getString("title"));
        editTextBody.setText(extras.getString("body"));

        alterarDados();
        deletarDados();
        carregarComments();
    }

    private void carregarComments() {
        String postId = extras.getString("postId");
        Call<List<Comment>> call = retrofitConfig.getAllComments(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Falha:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                comments = response.body();
                commentsAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, comments);
                listViewComments.setAdapter(commentsAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletarDados() {
        buttonDeletarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = extras.getString("postId");
                Call<Post> call = retrofitConfig.deletePost(postId);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Falha:" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void alterarDados() {
        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            String title = extras.getString("title");
            String body = extras.getString("body");

            @Override
            public void onClick(View view) {
                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo vazio", Toast.LENGTH_SHORT).show();
                }
                Post newPost = new Post(title, body);
                String postId = extras.getString("postId");
                Call<Post> call = retrofitConfig.updatePost(postId, newPost);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Falha:" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
