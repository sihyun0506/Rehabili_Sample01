package com.example.rehabili_sample1.ui.dashboard;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rehabili_sample1.MainActivity;
import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.ArmActivity;

public class DashboardFragment extends Fragment {
    String shared = "file";
    private DashboardViewModel dashboardViewModel;
    Button editerButton ,removeButton;
    String id, name, age, weight, height;
    View root;
    TextView textView1, textView2, textView3, textView4, textView5;

    public static final int REQUEST_CODE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, 0);
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

        if(id != "")textView1.setText(id);
        if(name != "")textView2.setText(name);
        if(age != "")textView3.setText(age);
        if(weight != "")textView4.setText(weight);
        if(height != "")textView5.setText(height);


        editerButton = root.findViewById(R.id.edit_user);
        editerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserEditActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        removeButton = root.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();

            }
        });






        return root;

    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("재활기록 삭제");
        builder.setMessage("재활기록을 삭제하시겠습니까? 복구되지 않습니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, 0);
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

            if(id != "")textView1.setText(id);
            if(name != "")textView2.setText(name);
            if(age != "")textView3.setText(age);
            if(weight != "")textView4.setText(weight);
            if(height != "")textView5.setText(height);
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