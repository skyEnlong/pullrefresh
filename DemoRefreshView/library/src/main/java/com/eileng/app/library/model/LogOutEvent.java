package com.eileng.app.library.model;

import java.io.Serializable;

/**
 * Created by enlong on 2017/3/9.
 */

public class LogOutEvent implements Serializable{
    private static final long serialVersionUID = 111L;
    public static final int TYPE_MANU= 1;
    public static final int TYPE_TOKEN= 2;
    public int logOutType= TYPE_MANU;

}
