package com.google.lesson.quanlili.interfaces;

import com.google.lesson.quanlili.beans.LoginPeople;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public interface OnReqLoginInfoListener {
    void onSuccess(LoginPeople sBean);

    void onFailure(String msg, Exception e);

    void onFailure(String msg);
}
