package com.google.lesson.quanlili.model;

import com.google.lesson.quanlili.beans.LoginPeople;
import com.google.lesson.quanlili.common.Constants;
import com.google.lesson.quanlili.interfaces.OnReqLoginInfoListener;
import com.google.lesson.quanlili.loginu.LoginJsonUtils;
import com.google.lesson.quanlili.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class LoginModelImpl implements LoginModel {
    @Override
    public void reqLoginInfo(String account, String pwd, final OnReqLoginInfoListener listener) {
        OkHttpUtils.ResultCallback<String> reqLoginCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LoginPeople loginPeople = LoginJsonUtils.readJsonStuffBeans(
                        response, "status");
                if (loginPeople != null) {
                    listener.onSuccess(loginPeople);
                } else {
                    listener.onFailure("登录失败!请重新尝试");
                }

            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        List<OkHttpUtils.Param> params = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param param1 = new OkHttpUtils.Param("account", account);
        OkHttpUtils.Param param2 = new OkHttpUtils.Param("password", pwd);
        params.add(param1);
        params.add(param2);
        OkHttpUtils.post(Constants.B_LOGIN_URL, reqLoginCallback, params);
    }
    }

