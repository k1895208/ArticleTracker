package com.example.articletracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_ARTICLE_REQUEST = 1;
    public static final int EDIT_ARTICLE_REQUEST = 2;

    private ArticleViewModel articleViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddArticle = findViewById(R.id.button_add_article);
        buttonAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditArticleActivity.class);
                startActivityForResult(intent, ADD_ARTICLE_REQUEST);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ArticleAdapter adapter = new ArticleAdapter();
        recyclerView.setAdapter(adapter);

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        articleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                String size = Integer.toString(articles.size());
                Toast.makeText(MainActivity.this, "size: " + size, Toast.LENGTH_SHORT).show();
                adapter.submitList(articles);
            }
        });

        //Deletes article if you swipe item.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                articleViewModel.deleteArticle(adapter.getArticleAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Article deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ArticleAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(MainActivity.this, AddEditArticleActivity.class);
                intent.putExtra(AddEditArticleActivity.EXTRA_ID, article.getId());
                intent.putExtra(AddEditArticleActivity.EXTRA_TITLE, article.getTitle());
                intent.putExtra(AddEditArticleActivity.EXTRA_URL, article.getURL());
                intent.putExtra(AddEditArticleActivity.EXTRA_TEXT, article.getText());
                startActivityForResult(intent, EDIT_ARTICLE_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ARTICLE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditArticleActivity.EXTRA_TITLE);
            String URL = data.getStringExtra(AddEditArticleActivity.EXTRA_URL);
            String text = data.getStringExtra(AddEditArticleActivity.EXTRA_TEXT);

            Article article = new Article(title, URL, text);
            articleViewModel.insertArticle(article);

            Toast.makeText(this, "Article saved", Toast.LENGTH_SHORT).show();
            ;
        } else if(requestCode == EDIT_ARTICLE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditArticleActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(this, "Article can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditArticleActivity.EXTRA_TITLE);
            String URL = data.getStringExtra(AddEditArticleActivity.EXTRA_URL);
            String text = data.getStringExtra(AddEditArticleActivity.EXTRA_TEXT);

            Article article = new Article(title, URL, text);
            article.setId(id);
            articleViewModel.updateArticle(article);

            Toast.makeText(this, "Article updated", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Article not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_articles:
                articleViewModel.deleteAllArticles();
                Toast.makeText(this, "All articles deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.refresh_articles:
                articleViewModel.scrapWebArticles(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}









































