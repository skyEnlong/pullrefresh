package com.eileng.app.library.base;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;

/**
 * Created by enlong on 2017/1/22.
 */

public abstract class BaseDB {
    protected ContentResolver resolver;
    protected Uri uri;
    protected Context mContext;

    public BaseDB(Context mContext){
        this.resolver = mContext.getApplicationContext().getContentResolver();
        uri  = getUri();
        this.mContext = mContext;
    }

    abstract public Uri getUri();


    public void registerOberver(ContentObserver observer){
        resolver.registerContentObserver(uri, true,observer);
    }

    public void unRegisterOberver(ContentObserver observer){
        resolver.unregisterContentObserver(observer);
    }
}
