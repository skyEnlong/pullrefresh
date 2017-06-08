package com.eileng.app.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.eileng.app.library.http.ResJsonString;
import com.eileng.app.library.util.CLog;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.FlowableSubscriber;

/**
 * Created by enlong on 2017/1/20.
 */

public class BaseActivity extends AppCompatActivity
        implements FlowableSubscriber<ResJsonString> {
    protected List<Subscription> subscription = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != subscription && subscription.size() > 0) {
            for (Subscription s : subscription) {
                s.cancel();
            }
            subscription.clear();
            subscription = null;
        }
    }

    @Override
    public void onSubscribe(Subscription s) {

        this.subscription.add(s);
        s.request(20);

    }

    @Override
    public void onNext(ResJsonString o) {
        ////to do update
    }

    @Override
    public void onError(Throwable t) {
        showMsg(t.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public void showMsg(String str) {
        CLog.i("enlong", "err class:" + this.getClass().getName());
        if (TextUtils.isEmpty(str)) return;
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showMsg(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void hideSoftInput() {

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).

                hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
