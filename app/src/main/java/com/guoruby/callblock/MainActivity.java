package com.guoruby.callblock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.guoruby.callblock.BLOCKROLE";
    public final static String ROLE_KEY = "role_key";
    public final static String ROLE_DATA = "role";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(ROLE_DATA, Context.MODE_PRIVATE);
        String role = sharedPreferences.getString(ROLE_KEY, "");
        final List<String> listData = new ArrayList<>();
        if (!"".equals(role)) {
            String[] strings = role.split(";");
            Collections.addAll(listData, strings);
        }

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.block_role_listView);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.simple_text, R.id.textView, listData);
        listView.setAdapter(listAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        listData.remove(position);
                        listAdapter.notifyDataSetChanged();
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        StringBuilder stringBuffer = new StringBuilder();
                        for (String role : listData) {
                            stringBuffer.append(role).append(";");
                        }
                        if (stringBuffer.length() > 0) {
                            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
                        }
                        editor.putString(ROLE_KEY, stringBuffer.toString());
                        editor.commit();//提交修改
                        Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    public void save(View view) {
        view.getId();
        Intent intent = new Intent(this, BlockRoleActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_block_role);
        if (editText != null) {
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

