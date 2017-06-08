package com.eileng.app.library.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by enlong on 2017/5/22.
 */

public class TelDialog extends Dialog {

    private Context mContext;
    private LinearLayout baseView;
    private Button btnMsg;
    private Button btnDial;
    private TextView tvTel;

    public TelDialog(Context context, int themeResId) {
        super(context, themeResId);
//        initDialog(mContext);
    }

    protected TelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
//        initDialog(mContext);
    }


//    public TelDialog(Context mContext) {
//        super(mContext, R.style.DialogMainFullScreen);
//
//
//        initDialog(mContext);
//    }
//
//    private void initDialog(final Context mContext) {
//
//        this.mContext = mContext;
//        setCanceledOnTouchOutside(true);
//
//
//        baseView = (LinearLayout) LayoutInflater.from(mContext).inflate(
//                R.layout.dialog_tel, null);
//        setContentView(baseView, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
////        getWindow().setWindowAnimations(
////                R.style.path_popwindow_anim_enterorout_window);
//        setWidthAndHeight(mContext.getResources().getDisplayMetrics().widthPixels,
//                0);
//        setGravity(Gravity.CENTER);
//
//        btnMsg = (Button) baseView.findViewById(R.id.btn_send_msg);
//        btnDial = (Button) baseView.findViewById(R.id.btn_dial);
//        tvTel = (TextView) baseView.findViewById(R.id.tv_tel);
//
//        btnMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                SystemUtil.launchSmsView(mContext, tvTel.getText().toString());
//
//            }
//        });
//        btnDial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                SystemUtil.lanchTelView(mContext, tvTel.getText().toString());
//
//            }
//        });
//    }

    public void showWithTel(String tel) {
        tvTel.setText(tel);
        show();
    }

    public void setGravity(int gravity) {
        Window win = getWindow();
        WindowManager.LayoutParams ll = win.getAttributes();
        ll.gravity = gravity;
        win.setAttributes(ll);
    }

    private void setWidthAndHeight(int width, int height) {
        Window win = getWindow();
        WindowManager.LayoutParams ll = win.getAttributes();
        if (0 != width) {

            ll.width = width;
        }

        if (0 != height) {

            ll.height = height;
        }
        win.setAttributes(ll);
    }


}
