package com.eileng.app.library.share;

import java.io.Serializable;

/**
 * Created by enlong on 2017/3/23.
 */

public class ShareBean implements Serializable {

    public String title;
    public String text;
    public String link;

    public String imgUrl;
    public String imgPath;

    public int shareType;
}
