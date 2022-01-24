package com.community.epub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import nl.siegmann.epublib.domain.Book;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {

    ArrayList<BookItem> books;
    ItemClicked context;

    public interface ItemClicked{
        void onItemClicked(int position);
    }

    public BooksAdapter(ArrayList<BookItem> books, Context context) {
        this.books = books;
        this.context = (ItemClicked) context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BooksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull BooksAdapter.BooksViewHolder holder, int position) {
//        Book book = books.get(position);
//        try {
//            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
//                    .getInputStream());
//            holder.cover_image.setImageBitmap(coverImage);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        holder.book_name.setText(book.getTitle());

        BookItem book = books.get(position);
        try {
            Bitmap coverImage = BitmapFactory.decodeStream(book.book.getCoverImage()
                    .getInputStream());
            holder.cover_image.setImageBitmap(coverImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.book_name.setText(book.book.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {
        final private TextView book_name;
        final private ImageView cover_image;
        public BooksViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.book_name);
            cover_image = itemView.findViewById(R.id.cover_image);
        }
    }
}
