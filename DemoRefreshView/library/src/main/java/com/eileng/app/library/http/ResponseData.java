package com.eileng.app.library.http;

import java.util.ArrayList;

/**
 * Created by enlong on 2017/1/22.
 */

public class ResponseData<T> {
    public String msg;
    public int status;
    public ArrayList<T> data;

}
