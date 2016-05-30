package com.guoruby.callblock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ROLE_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        String role = sharedPreferences.getString(MainActivity.ROLE_KEY, "");
        if (!"".equals(role)) {
            List<String> listData = new ArrayList<>();
            String[] strings = role.split(";");
            Collections.addAll(listData, strings);

            StringBuilder stringBuffer = new StringBuilder();
            for (String roleKey : listData) {
                stringBuffer.append(roleKey).append(";");
            }

            message = stringBuffer.append(message).toString();
        }
        editor.putString(MainActivity.ROLE_KEY, message);

//        editor.apply();
        editor.commit();//提交修改
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // Set the text view as the activity layout
        setContentView(textView);
    }

}
