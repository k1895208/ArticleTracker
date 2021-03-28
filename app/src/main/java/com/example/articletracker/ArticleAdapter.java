package com.example.articletracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class ArticleAdapter extends ListAdapter<Article, ArticleAdapter.ArticleHolder> {
    private onItemClickListener listener;

    public ArticleAdapter() {
        super(DIFF_CALLBACK);

    }

    private static final DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getURL().equals(newItem.getURL()) &&
                    oldItem.getText().equals(newItem.getText());
        }
    };
    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewURL.setText(currentNote.getURL());
    }


    public Article getArticleAt(int position){
        return getItem(position);
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewURL;
        public ArticleHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewURL = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Article article);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;

    }
}
























