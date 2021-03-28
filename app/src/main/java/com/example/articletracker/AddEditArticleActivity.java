package com.example.articletracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditArticleActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.articletracker.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.articletracker.EXTRA_TITLE";
    public static final String EXTRA_URL =
            "com.example.articletracker.EXTRA_URL";
    public static final String EXTRA_TEXT =
            "com.example.articletracker.EXTRA_TEXT";

    private EditText editTextTitle;
    private EditText editTextURL;
    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextURL = findViewById(R.id.edit_text_URL);
        editTextText = findViewById(R.id.edit_text_text);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Article");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextURL.setText(intent.getStringExtra(EXTRA_URL));
            editTextText.setText(intent.getStringExtra(EXTRA_TEXT));
        }else {
            setTitle("Add Article");
        }
    }

    private void saveArticle(){
        String title = editTextTitle.getText().toString();
        String URL = editTextURL.getText().toString();
        String Text = editTextText.getText().toString();

        if (title.trim().isEmpty() || URL.trim().isEmpty() || Text.trim().isEmpty()){
            Toast.makeText(this, "Please insert all information", Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_URL, URL);
        data.putExtra(EXTRA_TEXT, Text);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_article:
                saveArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}