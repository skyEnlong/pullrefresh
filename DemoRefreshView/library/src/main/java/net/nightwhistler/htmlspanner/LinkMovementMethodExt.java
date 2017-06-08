package net.nightwhistler.htmlspanner;


import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class LinkMovementMethodExt extends LinkMovementMethod {

    private static LinkMovementMethodExt instance;
    private Class spanClass = ImageSpan.class;
    private ImageSpanClickListener clickListener;

    public interface ImageSpanClickListener {
        public void onClickImage(String source);
    }

    public synchronized static LinkMovementMethodExt instance() {
        if (null == instance) {
            instance = new LinkMovementMethodExt();
        }
        return instance;
    }

    public void setImgClickListener(ImageSpanClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public void setSpanClass(Class spanClass) {
        this.spanClass = spanClass;
    }

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x1 = (int) event.getX();
                y1 = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                x1 = 0;
                y1 = 0;

                break;
            case MotionEvent.ACTION_UP:
                x2 = (int) event.getX();
                y2 = (int) event.getY();

                if (Math.pow(Math.abs(x1 - x2), 2) +
                        Math.pow(Math.abs(y1 - y2) ,2) < 200) {

                    x2 -= widget.getTotalPaddingLeft();
                    y2 -= widget.getTotalPaddingTop();

                    x2 += widget.getScrollX();
                    y2 += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y2);
                    int off = layout.getOffsetForHorizontal(line, x2);

                    Object[] spans = buffer.getSpans(off, off, spanClass);
                    if (spans.length != 0) {
                        if (spans[0] instanceof ImageSpan) {
                            Selection.setSelection(buffer,
                                    buffer.getSpanStart(spans[0]),
                                    buffer.getSpanEnd(spans[0]));

                            if (null != clickListener) {
                                clickListener.onClickImage(((ImageSpan) spans[0]).getSource());
                            }
                        }
                        return true;
                    }
                }
                break;
            default:
                break;
        }


        //return false;
        return super.onTouchEvent(widget, buffer, event);


    }


    public boolean canSelectArbitrarily() {
        return true;
    }

    public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode,
                           KeyEvent event) {
        return false;
    }
}
