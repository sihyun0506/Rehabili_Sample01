package com.example.rehabili_sample1.ui.notifications;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rehabili_sample1.DbOpenHelper;
import com.example.rehabili_sample1.R;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    String name = "Guest01";

    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    /* DB로 짜자... shared preference로 짜려고 한건 미친 짓이었다.
    // count는 intent 받을 것
//    String[] historyString = new String[count];

    // MainActivity에서 shared로 선언해주는게 맞을듯
    // 정보저장용 ArrayList
    ArrayList<History> historyList = new ArrayList<>();

    // History에서 정보를 받아옴
    for(int i = 0; i < historyList.size(); i++){
        History history = historyList.get(i);
    }
    // (출력을 위해) 받아온 정보를 문자열의 배열로 저장
    String historyString;

    // ExerciseType 입력
    if(History get(getExcerciseType()) == 1){
        historyS = historyS + "팔꿈치 구부리기";
    }else if(){

    }else{

    }
    // ExerciseLevel 입력

    // ExerciseTimes 입력


    // 문자열 출력용 ArrayList
    ArrayList<String> historyString = new ArrayList<>();


     */
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        TextView userName = (TextView) root.findViewById(R.id.userName);
        userName.setText(name + "님의 재활 기록입니다.");
        Button removeButton = (Button) root.findViewById(R.id.remove);
        removeButton.setOnClickListener(this);

        Button datetimeSortButton = (Button) root.findViewById(R.id.datetimeSortButton);
        datetimeSortButton.setOnClickListener(this);
        Button typeSortButton = (Button) root.findViewById(R.id.typeSortButton);
        typeSortButton.setOnClickListener(this);
        Button levelSortButton = (Button) root.findViewById(R.id.levelSortButton);
        levelSortButton.setOnClickListener(this);
        Button timesSortButton = (Button) root.findViewById(R.id.timesSortButton);
        timesSortButton.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        ListView listView = (ListView) root.findViewById(R.id.historyList);
        listView.setAdapter(arrayAdapter);

        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase("datetime");

        return root;
    }

    public void showDatabase(String sort) {
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while (iCursor.moveToNext()) {
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempDatetime = iCursor.getString(iCursor.getColumnIndex("datetime"));
            tempDatetime = setTextLength(tempDatetime, 10);
            String tempType = iCursor.getString(iCursor.getColumnIndex("type"));
            tempType = setTextLength(tempType, 10);
            String tempLevel = iCursor.getString(iCursor.getColumnIndex("level"));
            tempLevel = setTextLength(tempLevel, 10);
            String tempTimes = iCursor.getString(iCursor.getColumnIndex("times"));
            tempTimes = setTextLength(tempTimes, 10);

            String Result = tempDatetime + tempType + tempLevel + tempTimes;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length) {
        if (text.length() < length) {
            int gap = length - text.length();
            for (int i = 0; i < gap; i++) {
                text = text + " ";
            }
        }
        return text;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datetimeSortButton:
                showDatabase("datetime");
                break;

            case R.id.typeSortButton:
                showDatabase("type");
                break;

            case R.id.levelSortButton:
                showDatabase("level");
                break;

            case R.id.timesSortButton:
                showDatabase("times");
                break;

            case R.id.remove:
                show();
        }
    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("재활기록 삭제");
        builder.setMessage("재활기록을 삭제하시겠습니까? 복구되지 않습니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "아니오", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        builder.show();
    }

}