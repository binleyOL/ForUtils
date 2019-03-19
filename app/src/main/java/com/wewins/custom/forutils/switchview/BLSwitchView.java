package com.wewins.custom.forutils.switchview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以设置SwitchView的Enable属性
 * </br>
 * Created by Binley on 2018/3/23.
 */

public class BLSwitchView extends SwitchView {

    public BLSwitchView(Context context) {
        super(context, null);
    }

    public BLSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled) {
            setColor(colorPrimary, colorPrimaryDark);
        } else {
            setColor(colorOff, colorOffDark);
        }
    }
}
