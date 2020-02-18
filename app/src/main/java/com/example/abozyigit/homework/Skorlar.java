package com.example.abozyigit.homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Skorlar extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skorlar);

        listView = findViewById(R.id.listview);

        Database database= new Database(this);
        List<String> skorlar = database.verileriAl();

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                skorlar);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
