package com.example.rehabili_sample1.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rehabili_sample1.MainActivity;
import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.ArmActivity;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowActivity;
import com.example.rehabili_sample1.ui.arm.grip.GripActivity;
import com.example.rehabili_sample1.ui.arm.wrist.WristActivity;

public class HomeFragment extends Fragment {

    String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("file", 0);
        name = sharedPreferences.getString("name", "");
        if (name.equals("")) {
            name = "재활이";
        }

        TextView userName = (TextView) root.findViewById(R.id.userNameHome);
        String welcome = getString(R.string.welcome);
        userName.setText(name + welcome);

        Button wristButton = (Button) root.findViewById(R.id.wrist);
        Button gripButton = (Button) root.findViewById(R.id.grip);
        Button elbowButton = (Button) root.findViewById(R.id.elbow);
        Button wristButton2 = (Button) root.findViewById(R.id.wrist2);
        Button gripButton2 = (Button) root.findViewById(R.id.grip2);
        Button elbowButton2 = (Button) root.findViewById(R.id.elbow2);


        wristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WristActivity.class);
                startActivity(intent);
            }
        });
        gripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GripActivity.class);
                startActivity(intent);
            }
        });
        elbowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ElbowActivity.class);
                startActivity(intent);
            }
        });
        wristButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WristActivity.class);
                startActivity(intent);
            }
        });
        gripButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GripActivity.class);
                startActivity(intent);
            }
        });
        elbowButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ElbowActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}