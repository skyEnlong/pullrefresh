package com.eileng.app.library.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eileng.app.library.http.ResJsonString;
import com.eileng.app.library.util.CLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enlong on 2017/1/20.
 */

public abstract  class BaseFragment extends Fragment implements View.OnClickListener,
        Subscriber<ResJsonString>, OnFragmentSelect{
    protected List<Subscription> subscription = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != subscription && subscription.size() > 0){
            for(Subscription s : subscription){
                s.cancel();
            }
            subscription.clear();
        }
    }

    public int getSelectStatusColor(){
        return Color.WHITE;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription.add(s);
        s.request(5);
    }

    @Override
    public void onNext(ResJsonString resJsonString) {

    }

    @Override
    public void onError(Throwable t) {
        CLog.e("enlong", "" + t.getMessage());
        showMsg(t.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public void showMsg(int id){
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }

    public void showMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFragmentSelect() {

    }
}
