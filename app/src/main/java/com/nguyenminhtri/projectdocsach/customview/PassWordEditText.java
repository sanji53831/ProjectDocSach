package com.nguyenminhtri.projectdocsach.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.nguyenminhtri.projectdocsach.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("AppCompatCustomView")
public class PassWordEditText extends EditText {

    Drawable eye, eyeStrike;
    Boolean visible = false;
    Boolean validate = false;
    String PATTERN_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50})";
    Pattern pattern;
    Matcher matcher;

    public PassWordEditText(Context context) {
        super(context);
        init(null);
    }

    public PassWordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PassWordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PassWordEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PassWordEditText, 0, 0);
            visible = array.getBoolean(R.styleable.PassWordEditText_visible, false);
            validate = array.getBoolean(R.styleable.PassWordEditText_validate, false);
        }
        eye = ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_black_24dp).mutate();
        eyeStrike = ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_black_24dp).mutate();


//        if (this.validate) {
//            pattern = Pattern.compile(PATTERN_REGEX);
//            setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus == false) {
//                        TextInputLayout textInputLayout = (TextInputLayout) v.getParent().getParent();
//                        String chuoi = getText().toString();
//                        if (chuoi.equals("")) {
//                            textInputLayout.setErrorEnabled(true);
//                            textInputLayout.setError("Bạn chưa điền vào mục này");
//                        }
//                        else {
//                            matcher = pattern.matcher(chuoi);
//                            if (!matcher.matches()) {
//                                textInputLayout.setErrorEnabled(true);
//                                textInputLayout.setError("Mật khẩu không hợp lệ");
//                            } else {
//                                textInputLayout.setErrorEnabled(false);
//                                textInputLayout.setError("");
//                            }
//                        }
//                    }
//                }
//            });
//        }

        setting();
    }

    private void setting() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = !visible ? eyeStrike : eye;
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && (getRight() - getCompoundDrawables()[2].getBounds().width() <= event.getX())) {
            visible = !visible;
            setting();
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
