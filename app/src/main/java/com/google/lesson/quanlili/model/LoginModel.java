package com.google.lesson.quanlili.model;

import com.google.lesson.quanlili.interfaces.OnReqLoginInfoListener;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public interface LoginModel {
  void  reqLoginInfo(String account, String pwd, OnReqLoginInfoListener listener);
}
