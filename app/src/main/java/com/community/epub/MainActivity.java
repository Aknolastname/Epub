package com.community.epub;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements BooksAdapter.ItemClicked{
    RecyclerView books_rec;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        books_rec = findViewById(R.id.books_rec);

        books_rec.hasFixedSize();
        BooksAdapter adapter = new BooksAdapter(SplashScreen.list, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        books_rec.setLayoutManager(layoutManager);
        books_rec.setAdapter(adapter);

    }

    @Override
    public void onItemClicked(int position) {
        Intent intent;
        intent = new Intent(this, BookActivity.class);
        intent.putExtra("BOOKCODE", position);
        intent.putExtra("BOOKNAME", SplashScreen.list.get(position).path);
//        intent.putExtra("transitionType", 0);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}