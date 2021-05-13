package com.example.rehabili_sample1.ui.history;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rehabili_sample1.DbOpenHelper;
import com.example.rehabili_sample1.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    String name;
    // BD 오름/ 내림 차순 정렬을 위한 값
    Boolean sign1 = true, sign2 = true, sign3 = true, sign4 = true;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("file", 0);
        name = sharedPreferences.getString("name", "");
        if (name.equals("")) {
            name = "재활이";
        }
        String yourHistory = getString(R.string.yourHistory);

        TextView userName = (TextView) root.findViewById(R.id.userName);
        userName.setText(name + yourHistory);

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

    // DB 오름차순 정렬 후 출력
    public void showDatabase(String sort) {
        Cursor iCursor = mDbOpenHelper.sortColumnUp(sort);
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

            String Result = tempDatetime + "  " + tempType + " " + tempLevel + " " + tempTimes;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    // DB 내림차순 정렬 후 출력
    public void showDatabaseDown(String sort) {
        Cursor iCursor = mDbOpenHelper.sortColumnDown(sort);
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

            String Result = tempDatetime + "  " + tempType + " " + tempLevel + " " + tempTimes;
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
                if (sign1)
                    showDatabase("datetime");
                else
                    showDatabaseDown("datetime");
                sign1 = !sign1;
                break;

            case R.id.typeSortButton:
                if (sign2)
                    showDatabase("type");
                else
                    showDatabaseDown("type");
                sign2 = !sign2;
                break;

            case R.id.levelSortButton:
                if (sign3)
                    showDatabase("level");
                else
                    showDatabaseDown("level");
                sign3 = !sign3;
                break;

            case R.id.timesSortButton:
                if (sign4)
                    showDatabase("times");
                else
                    showDatabaseDown("times");
                sign4 = !sign4;
                break;

            case R.id.remove:
                show();
//                showDatabase("datetime");
        }
    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.remove_history));
        builder.setMessage(getString(R.string.remove_really));
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDbOpenHelper.deleteAllColumns();
                        Toast.makeText(getActivity(), getString(R.string.removed), Toast.LENGTH_SHORT).show();
                        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.navigation_history);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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