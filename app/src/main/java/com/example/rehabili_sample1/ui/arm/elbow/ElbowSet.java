package com.example.rehabili_sample1.ui.arm.elbow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.ArmActivity;

public class ElbowSet extends AppCompatActivity {
    Button start;
    EditText editText;
    Spinner spinner;
    public String goalNumber;
    public String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elbow_set);
        start = (Button)findViewById(R.id.buttonStart);
        editText = (EditText)findViewById(R.id.goalNumber);
        spinner = (Spinner)findViewById(R.id.level);
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

                if (goalNumber.equals("") ||Integer.parseInt(goalNumber) == 0){
                    Toast.makeText(ElbowSet.this, "올바른 값을 입력하세요" , android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(goalNumber) > 50){
                    Toast.makeText(ElbowSet.this, "50회 미만의 값을 입력하세요" , android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(ElbowSet.this, ElbowCounting.class);
                    intent.putExtra("goalNumber",goalNumber);
                    intent.putExtra("level",level);
                    startActivity(intent);

                }
            }
        });





    }
}
