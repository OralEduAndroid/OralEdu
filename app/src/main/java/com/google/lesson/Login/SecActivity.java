package com.google.lesson.Login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.lesson.Other.MenuActivity;
import com.google.lesson.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;

public class SecActivity extends Activity implements View.OnClickListener {
    private EditText phone;
    private EditText cord;
    private EditText password;
    private Button getCord;
    private Button back;
    private Button zhuce;
    private TextView login;
    private String iPhone;
    private String iPassword;
    private String iCord;
    private TimeCount time;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_sec);
        init();

        SMSSDK.initSDK(this, "14b94cda79db0", "68bb646b370a4c0889967f56afa8353c");

        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }
    private void init() {
        phone = (EditText) findViewById(R.id.sec_et_phonenumber);
        cord = (EditText) findViewById(R.id.sec_et_cord);
        password=(EditText)findViewById(R.id.sec_et_password);
        getCord = (Button) findViewById(R.id.sec_btn_getcord);
        zhuce = (Button) findViewById(R.id.sec_btn_zhuce);
        back=(Button)findViewById(R.id.sec_btn_back);
        login=(TextView)findViewById(R.id.sec_tv_login);
        getCord.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        back.setOnClickListener(this);
        time=new TimeCount(60000,1000,getCord);
        zhuce.setBackgroundColor(Color.parseColor("#d3d3d3"));
        zhuce.setEnabled(false);
        login.setOnClickListener(this);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    zhuce.setBackgroundColor(Color.parseColor("#d3d3d3"));
                else
                {zhuce.setBackgroundColor(Color.parseColor("#33ccff"));
                    zhuce.setEnabled(true);}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sec_btn_getcord:
                iPhone = phone.getText().toString().trim();
                iPassword=password.getText().toString().trim();
                if(!iPhone.isEmpty()&&!iPassword.isEmpty()){
                    if(iPhone.length()==11&&iPassword.length()>=6&&iPassword.length()<=20){
                        SMSSDK.getVerificationCode("86",iPhone);
                        cord.requestFocus();
                        time.start();
                    }else if(iPhone.length()<11){
                        Toast.makeText(SecActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        phone.requestFocus();
                    }
                    else if (iPassword.length()<6||iPassword.length()>20)
                        Toast.makeText(getApplicationContext(),"密码必须在6~20位之间",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SecActivity.this, "请输入您的电话号码或密码", Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                }
                break;

            case R.id.sec_btn_zhuce:
                if(!TextUtils.isEmpty(cord.getText().toString().trim())){
                    if(cord.getText().toString().trim().length()==4){
                        iCord = cord.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                    }
                    else{
                        Toast.makeText(SecActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        cord.requestFocus();
                    }
                }else{
                    Toast.makeText(SecActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    cord.requestFocus();
                }
                break;
            case R.id.sec_btn_back:
                Intent intent=new Intent(SecActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.sec_tv_login:
                Intent intent2=new Intent(SecActivity.this,MenuActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    Toast.makeText(getApplicationContext(), "注册成功，请登录", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }
                else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES)
                {
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    Toast.makeText(SecActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(SecActivity.this, "smssdk_network_error");
                    Toast.makeText(SecActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    if (resId > 0) {
                        Toast.makeText(SecActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
