package com.nusiss.productservice.exception;

/**
 * 登录失败
 */
public class LoginFailedException extends BaseException{
    public LoginFailedException(String msg){
        super(msg);
    }
}
