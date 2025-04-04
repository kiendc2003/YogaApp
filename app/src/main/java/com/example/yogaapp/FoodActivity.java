package com.example.yogaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FoodActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        String[] tstory = getResources().getStringArray(R.array.title_story);
        final String[] dstrory = getResources().getStringArray(R.array.details_story);

        listView = findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.rowtxt, tstory);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String t = dstrory[position];
                Intent intent = new Intent(FoodActivity.this, FoodActivityDetail.class);
                intent.putExtra("story", t);
                startActivity(intent);
            }
        });

    }

    public void foodGoBack(View view) {
        Intent intent = new Intent(FoodActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}