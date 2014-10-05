package com.goodshop.app;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

public class EditTextOnFocusChangeListener implements OnFocusChangeListener{
	
	@Override
    public void onFocusChange(View v, boolean hasFocus){

        if(!hasFocus) {
            InputMethodManager imm =  (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}