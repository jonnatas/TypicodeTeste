package com.example.typicode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonSalvar;

    private ListView listViewPosts;
    private ArrayAdapter<Post> postArrayAdapter;
    private List<Post> posts;

    private Retrofit retrofit;
    private RetrofitConfig retrofitConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitConfig = retrofit.create(RetrofitConfig.class);

        editTextTitle = (EditText) findViewById(R.id.editTextTitleId);
        editTextBody = (EditText) findViewById(R.id.editTextBodyId);
        buttonSalvar = (Button) findViewById(R.id.buttonSalvarId);
        listViewPosts = (ListView) findViewById(R.id.listViewPostsId);

        getAllPosts();
        salvarPost();
        irParaPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllPosts();
    }

    private void irParaPost() {
        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Post post = posts.get(position);
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra("postId", post.getId().toString());
                intent.putExtra("title", post.getTitle());
                intent.putExtra("userID", post.getUserId().toString());
                intent.putExtra("body", post.getBody());

                startActivity(intent);
            }
        });
    }

    private void salvarPost() {
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = editTextTitle.getText().toString();
                final String body = editTextBody.getText().toString();
                if (body.isEmpty() || title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo vazio", Toast.LENGTH_SHORT).show();
                    return;
                }
                Post newPost = new Post(title, body, 3);
                Map<String, String> fields = new HashMap<>();
                fields.put("userId", "3");
                fields.put("title", title);
                fields.put("body", body);

                Call<Post> call = retrofitConfig.createPost(fields);

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Falha,code:" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Post postResult = response.body();
                        posts.add(0, postResult);
                        postArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Erro:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getAllPosts() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "3");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        Call<List<Post>> call = retrofitConfig.getUsersQueryMap(parameters);
//        Call<List<Post>> call = retrofitConfig.getUsersPosts(new Integer[]{3, 4}, "id", "desc");
//        Call<List<Post>> call = retrofitConfig.getPostByQuery(3, "id", "desc");
//        Call<List<Post>> call = retrofitConfig.getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Falha,code:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                posts = response.body();
                postArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, posts);
                listViewPosts.setAdapter(postArrayAdapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
