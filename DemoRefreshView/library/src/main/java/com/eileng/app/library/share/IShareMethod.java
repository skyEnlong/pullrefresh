package com.eileng.app.library.share;

/**
 * Created by enlong on 2017/3/23.
 */

public interface IShareMethod {
    int SHARE_TYPE_TEXT = 0;
    int SHARE_TYPE_PIC = 1;
    int SHARE_TYPE_URL = 2;

    public void share(ShareBean shareBean);

}
