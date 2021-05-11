package com.example.rehabili_sample1.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowCounting;
import com.example.rehabili_sample1.ui.arm.wrist.WristCounting;

public class Set extends AppCompatActivity {
    TextView exerciseType;
    Button start;
    EditText editText;
    Spinner spinner;
    public String goalNumber;
    public String level;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elbow_set);

        //이전 Activity에서 받아온 값으로 type을 설정
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        exerciseType = (TextView) findViewById(R.id.exerciseType);
        exerciseType.setText(type);
        start = (Button) findViewById(R.id.buttonStart);
        editText = (EditText) findViewById(R.id.goalNumber);
        spinner = (Spinner) findViewById(R.id.level);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalNumber = editText.getText().toString();

                if (goalNumber.equals("") || Integer.parseInt(goalNumber) == 0) {
                    Toast.makeText(Set.this, "목표 횟수를 입력하세요", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(goalNumber) >= 50) {
                    Toast.makeText(Set.this, "50회 이하의 값을 입력하세요", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // type에 따라 Counting으로 값을 넘겨줌
                    if (type.equals("팔 구부렸다 펴기")) {
                        Intent intent = new Intent(Set.this, ElbowCounting.class);
                        intent.putExtra("type", type);
                        intent.putExtra("goalNumber", goalNumber);
                        intent.putExtra("level", level);
                        startActivity(intent);
                    } else if (type.equals("손목 돌리기")) {
                        Intent intent = new Intent(Set.this, WristCounting.class);
                        intent.putExtra("type", type);
                        intent.putExtra("goalNumber", goalNumber);
                        intent.putExtra("level", level);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
