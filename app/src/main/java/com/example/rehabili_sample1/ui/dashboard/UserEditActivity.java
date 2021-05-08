package com.example.rehabili_sample1.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rehabili_sample1.MainActivity;
import com.example.rehabili_sample1.R;

import java.util.Set;

public class UserEditActivity extends AppCompatActivity {

    EditText editId,editName,editAge,editWeight,editHeight;
    String id, name, age, weight, height;
    Button editDataButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        editDataButton = (Button)findViewById(R.id.edit_userdata);
        editDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editId = findViewById(R.id.editId);
                editName = findViewById(R.id.editName);
                editAge = findViewById(R.id.editAge);
                editHeight = findViewById(R.id.editHeight);
                editWeight = findViewById(R.id.editWeight);

                id = editId.getText().toString();
                name = editName.getText().toString();
                age = editAge.getText().toString();
                weight = editWeight.getText().toString();
                height = editHeight.getText().toString();
                if(id.equals("")||name.equals("")){
                    Toast.makeText(UserEditActivity.this, "ID와 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("sign",true);
                setResult(RESULT_OK,intent);
                SharedPreferences sharedPreferences = getSharedPreferences("file", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id",id);
                editor.putString("name",name);
                editor.putString("age",age);
                editor.putString("height",height);
                editor.putString("weight",weight);
                editor.commit();
                finish();
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        SharedPreferences sharedPreferences = getSharedPreferences("file", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("id",id);
//        editor.putString("name",name);
//        editor.putString("age",age);
//        editor.putString("height",height);
//        editor.putString("weight",weight);
//        editor.commit();
//        super.onDestroy();
//
//    }

}