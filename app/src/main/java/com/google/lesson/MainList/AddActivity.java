package com.google.lesson.MainList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.lesson.MainActivity;
import com.google.lesson.R;

public class AddActivity extends Activity {
    private ImageView ivDeleteText;
    private EditText etSearch;
    private EditText et_search;
    private TextView tv_tip;
    private SearchListView listView;
    private TextView tv_clear;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);;
    private SQLiteDatabase db;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        initView();


        //back返回按钮事件
        Button back = (Button)findViewById(R.id.add_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });


        //点击按钮搜索
        Button btn_search = (Button)findViewById(R.id.add_btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                boolean hasData = hasData(et_search.getText().toString().trim());
                if (!hasData) {
                    insertData(et_search.getText().toString().trim());
                    queryData("");
                }
                // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                Toast.makeText(AddActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // 搜索框的键盘搜索键点击回调
//        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
//
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
//                    // 先隐藏键盘
//                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
//                    boolean hasData = hasData(et_search.getText().toString().trim());
//                    if (!hasData) {
//                        insertData(et_search.getText().toString().trim());
//                        queryData("");
//                    }
//                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
//                    Toast.makeText(AddActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
//
//                }
//                return false;
//            }
//        });

        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tv_tip.setText("搜索历史");
                } else {
                    tv_tip.setText("搜索结果");
                }
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                et_search.setText(name);
                Toast.makeText(AddActivity.this, name, Toast.LENGTH_SHORT).show();
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        });

        // 插入数据，便于测试，否则第一次进入没有数据怎么测试呀？
//        Date date = new Date();
//        long time = date.getTime();
//        insertData("Leo" + time);

        // 第一次进入查询所有的历史记录
        queryData("");




        //文字一键删除操作
        ivDeleteText = (ImageView) findViewById(R.id.add_iv_DeleteText);
        etSearch = (EditText) findViewById(R.id.add_et_search);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.add_et_search);
        tv_tip = (TextView) findViewById(R.id.add_tv_tip);
        listView = (SearchListView)findViewById(R.id.add_lv_listView);
        tv_clear = (TextView) findViewById(R.id.add_tv_clear);

        // 调整EditText左边的搜索按钮的大小
//        Drawable drawable = getResources().getDrawable(R.drawable.search);
//        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
//        et_search.setCompoundDrawables(drawable, null, null, null);// 只放左边
    }



}

