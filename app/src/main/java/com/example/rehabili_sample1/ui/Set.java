package com.example.rehabili_sample1.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowCounting;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowCountingRight;
import com.example.rehabili_sample1.ui.arm.wrist.WristCounting;
import com.example.rehabili_sample1.ui.arm.wrist.WristCountingRight;

public class Set extends AppCompatActivity {
    TextView exerciseType;
    Button start;
    EditText editText;
    Spinner spinner;
    RadioGroup rg;
    public String goalNumber;
    public String level;
    public String type;
    private String radioValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        //이전 Activity에서 받아온 값으로 type을 설정
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        exerciseType = (TextView) findViewById(R.id.exerciseType);
        exerciseType.setText(type);
        start = (Button) findViewById(R.id.buttonStart);
        editText = (EditText) findViewById(R.id.goalNumber);
        //radio로 left right 받아옴
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        //spinner로 level 받아옴
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
                RadioButton radio = (RadioButton) findViewById((rg.getCheckedRadioButtonId()));
                radioValue = radio.getText().toString();
                goalNumber = editText.getText().toString();

                if (goalNumber.equals("") || Integer.parseInt(goalNumber) == 0) {
                    Toast.makeText(Set.this, "목표 횟수를 입력하세요", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(goalNumber) >= 50) {
                    Toast.makeText(Set.this, "50회 이하의 값을 입력하세요", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // type과 좌 우에 따라 Counting으로 값을 넘겨줌
                    if (type.equals("팔 구부렸다 펴기")) {
                        Intent intent;
                        if (radioValue.equals("왼손")) {
                            intent = new Intent(Set.this, ElbowCounting.class);
                        } else {
                            intent = new Intent(Set.this, ElbowCountingRight.class);
                        }
                        intent.putExtra("type", type);
                        intent.putExtra("goalNumber", goalNumber);
                        intent.putExtra("level", level);
                        startActivity(intent);
                    } else if (type.equals("손목 돌리기")) {
                        Intent intent;
                        if (radioValue.equals("왼손")) {
                            intent = new Intent(Set.this, WristCounting.class);
                        } else {
                            intent = new Intent(Set.this, WristCountingRight.class);
                        }
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
