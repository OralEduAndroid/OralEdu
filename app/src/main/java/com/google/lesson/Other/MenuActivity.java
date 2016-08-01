package com.google.lesson.Other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.lesson.Login.SecActivity;
import com.google.lesson.Login.ThirdActivity;
import com.google.lesson.MainActivity;
import com.google.lesson.R;
import com.google.lesson.quanlili.beans.LoginPeople;
import com.google.lesson.quanlili.loading.ShapeLoadingDialog;
import com.google.lesson.quanlili.loginpresenter.LoginPresenter;
import com.google.lesson.quanlili.loginpresenter.LoginPresenterImpl;
import com.google.lesson.quanlili.loginview.LoginCallback;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MenuActivity extends Activity implements View.OnClickListener,LoginCallback {
    private EditText username=null;
    private EditText password=null;
    private Button login;
    private Button back;
    private Button ljzhuce;
    private Button wjmima;
    private LoginPresenter loginPresenter;
    private ShapeLoadingDialog shapeLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        initViews();
        loginPresenter = new LoginPresenterImpl(this);
        shapeLoadingDialog = new ShapeLoadingDialog(this);
        shapeLoadingDialog.setLoadingText("加载中...");
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    login.setBackgroundColor(Color.parseColor("#d3d3d3"));
                else {
                    login.setBackgroundColor(Color.parseColor("#33ccff"));
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initViews(){
        username=(EditText)findViewById(R.id.main_et_number);
        password=(EditText)findViewById(R.id.main_et_password);
        ljzhuce=(Button)findViewById(R.id.main_btn_ljzhuce);
        back=(Button)findViewById(R.id.main_btn_back);
        wjmima=(Button)findViewById(R.id.main_btn_wjmima);
        login=(Button)findViewById(R.id.main_btn_login);
        login.setBackgroundColor(Color.parseColor("#d3d3d3"));
        login.setEnabled(false);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
        ljzhuce.setOnClickListener(this);
        wjmima.setOnClickListener(this);
    }
    @Override
    public void showProgress() {
        shapeLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        shapeLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String msg) {
        showToast(msg);
    }
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void movetointent() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showStuffInfo(LoginPeople stuffBean) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_login:
                loginPresenter.reqLoginInfo(username.getText().toString(), password
                        .getText().toString());
                break;
            case R.id.main_btn_back:
                MenuActivity.this.finish();
                break;
            case R.id.main_btn_ljzhuce:
                Intent intent=new Intent(MenuActivity.this,SecActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_wjmima:
                Intent intent2=new Intent(MenuActivity.this,ThirdActivity.class);
                startActivity(intent2);
                break;
            default:break;

        }
    }
}
