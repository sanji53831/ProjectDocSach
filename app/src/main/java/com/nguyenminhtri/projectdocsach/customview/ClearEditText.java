package com.nguyenminhtri.projectdocsach.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.nguyenminhtri.projectdocsach.R;

@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText {
    Drawable clear, nonClear;
    Boolean visible = false;

    public ClearEditText(Context context) {
        super(context);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        clear = ContextCompat.getDrawable(getContext(), R.drawable.ic_close_black_24dp).mutate();
        nonClear = ContextCompat.getDrawable(getContext(), android.R.drawable.screen_background_light_transparent).mutate();
        setting();
    }

    private void setting() {
        setInputType(InputType.TYPE_CLASS_TEXT);
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = visible ? clear : nonClear;
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().isEmpty() == false) {
            visible = true;
            setting();
        } else {
            visible = false;
            setting();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && (event.getX() >= getRight() - getCompoundDrawables()[2].getBounds().width())) {
            setText("");
        }
        return super.onTouchEvent(event);
    }
}
