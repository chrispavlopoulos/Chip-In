package com.teamwd.chipin.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.teamwd.chipin.R;

public class ChipInputField extends RelativeLayout {

    private Context context;

    public ChipInputField(Context context) {
        super(context);
        sharedConst(context);
    }

    public ChipInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConst(context);
    }

    public ChipInputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sharedConst(context);
    }

    private void sharedConst(Context context){
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.view_input_field, this);
    }
}
