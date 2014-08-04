package com.zj.storemanag.view.custom_autotv;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

public class MyAutoTextView extends AutoCompleteTextView{
	
	  public MyAutoTextView(Context context) {
	    super(context);
	  }

	  public MyAutoTextView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }

	  public MyAutoTextView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  }

	  @Override
	  public boolean enoughToFilter() {
	    return true;
	  }

	  @Override
	  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
	    super.onFocusChanged(focused, direction, previouslyFocusedRect);

	    performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
	  }

}
