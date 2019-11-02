package com.teamwd.chipin.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamwd.chipin.R;

public class ChipButton extends RelativeLayout {

    private Context context;
    private TextView titleText;

    public ChipButton(Context context) {
        super(context);
        sharedConst(context);
    }

    public ChipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConst(context);
        setAttrs(attrs);
    }

    public ChipButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sharedConst(context);
        setAttrs(attrs);
    }

    private void sharedConst(Context context){
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.view_chip_button, this);
        titleText = findViewById(R.id.tv_title);
    }

    private void setAttrs(AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChipButton);

        String text = a.getString(R.styleable.ChipButton_text);
        setText(text);

        a.recycle();
    }

    public void setText(String text){
        if(titleText != null)
            titleText.setText(text);
    }
}
