package com.example.rehabili_sample1.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rehabili_sample1.MainActivity;
import com.example.rehabili_sample1.R;

public class Finish extends AppCompatActivity {

    // Counting에서 받아올 값
    String type, goalNumber;
    private TextView exerciseType;
    private TextView goalNumber1, goalNumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        exerciseType = findViewById(R.id.exerciseType);
        goalNumber1 = findViewById(R.id.goalNumber1);
        goalNumber2 = findViewById(R.id.goalNumber2);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.raw.firework).into(imageView);

        // Counting에서 값 받아옴
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        exerciseType.setText(type);
        goalNumber = intent.getStringExtra("goalNumber");
        goalNumber1.setText(goalNumber);
        goalNumber2.setText(goalNumber);

        Button restartButton = (Button) findViewById(R.id.restartButton);
        Button homeButton = (Button) findViewById(R.id.homeButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Finish.this, Set.class);
                intent.putExtra("type",type);
                startActivity(intent);
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Finish.this, MainActivity.class);
                startActivityForResult(intent, Activity.RESULT_OK);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}