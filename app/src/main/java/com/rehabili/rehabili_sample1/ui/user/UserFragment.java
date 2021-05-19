package com.rehabili.rehabili_sample1.ui.user;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rehabili.rehabili_sample1.Appinfo;
import com.rehabili.rehabili_sample1.R;

public class UserFragment extends Fragment {
    String shared = "file";

    Button editerButton, appInfoButton;
    String id, name, age, weight, height;
    View root;
    TextView textView1, textView2, textView3, textView4, textView5;

    public static final int REQUEST_CODE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        root = inflater.inflate(R.layout.fragment_user, container, false);

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, 0);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if (id==null) {
//            editor.putString("id", "Guest01");
//            editor.commit();
//        }
//        if (name==null) {
//            editor.putString("name", "재활이");
//            editor.commit();
//        }

        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        age = sharedPreferences.getString("age", "");
        weight = sharedPreferences.getString("weight", "");
        height = sharedPreferences.getString("height", "");

        textView1 = root.findViewById(R.id.Id);
        textView2 = root.findViewById(R.id.Name);
        textView3 = root.findViewById(R.id.Age);
        textView4 = root.findViewById(R.id.Weight);
        textView5 = root.findViewById(R.id.Height);

        // if 문 삭제 필요
        if (id != "")
            textView1.setText("ID : " + id);
        if (name != "")
            textView2.setText(name);
        if (age != "")
            textView3.setText(getString(R.string.man) + age + getString(R.string.se));
        else
            textView3.setText("");
        if (weight != "")
            textView4.setText(weight + "kg");
        else
            textView4.setText("");
        if (height != "")
            textView5.setText(height + "cm");
        else
            textView5.setText("");

        editerButton = root.findViewById(R.id.edit_user);
        editerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        appInfoButton = root.findViewById(R.id.appInfo);
        appInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Appinfo.class);
                startActivity(intent);
            }
        });

        return root;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, 0);

//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            if (id==null) {
//                editor.putString("id", "Guest");
//                editor.commit();
//            }
//            if (name==null) {
//                editor.putString("name", "재활이");
//                editor.commit();
//            }

            id = sharedPreferences.getString("id", "");
            name = sharedPreferences.getString("name", "");
            age = sharedPreferences.getString("age", "");
            weight = sharedPreferences.getString("weight", "");
            height = sharedPreferences.getString("height", "");

            textView1 = root.findViewById(R.id.Id);
            textView2 = root.findViewById(R.id.Name);
            textView3 = root.findViewById(R.id.Age);
            textView4 = root.findViewById(R.id.Weight);
            textView5 = root.findViewById(R.id.Height);

            // 수정 필요. if문 삭제, 입력시 조건 부여.
            if (id != "")
                textView1.setText("ID : " + id);
            if (name != "")
                textView2.setText(name);
            if (age != "")
                textView3.setText(getString(R.string.man) + age + getString(R.string.se));
            else
                textView3.setText("");
            if (weight != "")
                textView4.setText(weight + "kg");
            else
                textView4.setText("");
            if (height != "")
                textView5.setText(height + "cm");
            else
                textView5.setText("");
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (allowRefresh)
//        {
//            allowRefresh = false;
//            lst_applist = db.load_apps();
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }
}