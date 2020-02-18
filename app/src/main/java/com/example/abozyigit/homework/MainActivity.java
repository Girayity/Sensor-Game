package com.example.abozyigit.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Oyna_Click(View view) {
        Switch switch1 = findViewById(R.id.switch1);
        Intent baslat = new Intent(this, Oyun.class);
        baslat.putExtra("zorluk", switch1.isChecked());
        startActivity(baslat);
    }

    public void Skorlar_Click(View view) {
        Intent skor = new Intent(this, Skorlar.class);
        startActivity(skor);
    }
}
