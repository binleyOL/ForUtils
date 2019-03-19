package com.wewins.custom.forutils.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wewins.custom.forutils.R;
import com.wewins.custom.forutils.log.BULog;
import com.wewins.custom.forutils.screen.Density;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/3/12.<b/>
 */
public class SingleInputPopupView extends PopupView implements PopupView.OnWindowChangedListener {

    public static final String NOT_CHANGE = "text not change";

    /** 标题*/
    private String sTitle = null;
    /** 左按键*/
    private String sLeft = null;
    /** 右按键*/
    private String sRight = null;
    /** 输入框内容*/
    private String sInput = null;
    /** 输入框内容*/
    private String sHint = null;

    TextView tvTitle;// 标题
    EditText edtInput = null;// 输入框
    View vBottom = null;// 底部透明区域
    Button btnLeft;// 左按键
    Button btnRight;// 右按键
    OnButtonClickListener listener = null;

    public SingleInputPopupView(Context context) {
        super(context);
        getTestString();
    }

    /** 初始数据*/
    private void getTestString() {
        this.sTitle = "你是我所有的梦";
        this.sInput = "写给了你的迷茫，写给我的轻狂";
        this.sLeft = "取消";
        this.sRight = "确定";
    }

    /** 重新设置edittext内的内容。如果想不改变内容，填入：NOT_CHANGE。*/
    public void set(String sInput, String sHint) {
        if(!NOT_CHANGE.equals(sInput)) this.sInput = sInput;
        if(!NOT_CHANGE.equals(sHint)) this.sHint = sHint;
    }

    /** 设置Pop的内容。如果想不改变内容，填入：NOT_CHANGE。*/
    public void set(Object title, Object input, Object inputHint, Object left, Object right) {
        String sTitle = getString(title);
        String sInput = getString(input);
        String sHint = getString(inputHint);
        String sLeft = getString(left);
        String sRight = getString(right);
        if(!NOT_CHANGE.equals(sTitle)) this.sTitle = sTitle;
        if(!NOT_CHANGE.equals(sInput)) this.sInput = sInput;
        if(!NOT_CHANGE.equals(sHint)) this.sHint = sHint;
        if(!NOT_CHANGE.equals(sLeft)) this.sLeft = sLeft;
        if(!NOT_CHANGE.equals(sRight)) this.sRight = sRight;
    }

    /** 设置按键点击监听*/
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void show() {
        create();
        addWindowChangedListener();
        showKeyboard(mContext);
        if(null != tvTitle) {
            String str = tvTitle.getText().toString();
            if(!str.equals(sTitle)) {
                tvTitle.setText(sTitle);
            }
        }
        if(null != edtInput) {
            //聚焦输入框，设置光标
            edtInput.requestFocus();
            String str = edtInput.getText().toString();
            if(!str.equals(sInput)) {
                edtInput.setText(sInput);
            }
            edtInput.setSelection(edtInput.length());
        }
        if(null != btnLeft) {
            String str = btnLeft.getText().toString();
            if(!str.equals(sLeft)) {
                btnLeft.setText(sLeft);
            }
        }
        if(null != btnRight) {
            String str = btnRight.getText().toString();
            if(!str.equals(sRight)) {
                btnRight.setText(sRight);
            }
        }
    }

    @Override
    public void dismiss() {
        startPopAmin(false, ANIM_TYPE_BLUR);
        edtInput.clearFocus();
        closeKeybord(mContext);
    }

    public void create() {
        // 构建一个点击外部
        if(mPopupWindow == null) {
            Density du = Density.getInstance(mContext);
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_one_input, null);
            contentView.setBackgroundColor(Color.TRANSPARENT);
            TextView tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
            edtInput = (EditText) contentView.findViewById(R.id.edtInput);//仅能输入3为数字，而且数字大小必须位于n-256之间
            btnLeft = (Button) contentView.findViewById(R.id.btnLeft);
            btnRight = (Button) contentView.findViewById(R.id.btnRight);
            vBottom = contentView.findViewById(R.id.vBottom);
            mPopupWindow = new PopupWindow(contentView, du.screenWidth, -2, true);
            mPopupWindow.setContentView(contentView);
            mPopupWindow.setOutsideTouchable(false);
            //			mPopupWindow.setTouchable(true);
            //			// 实例化一个ColorDrawable颜色为半透明
            //			ColorDrawable dw = new ColorDrawable(0x00000000);
            //			mPopupWindow.setBackgroundDrawable(dw);
            //设置动画
            mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style_downshow);

            tvTitle.setText(sTitle);
            edtInput.setText(sInput);
            btnLeft.setText(sLeft);
            btnRight.setText(sRight);

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    removeWindowChangedListener();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vBottom.getLayoutParams();
                    params.height = 0;
                    vBottom.setLayoutParams(params);
                    dismiss();
                }
            });

            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    if(null != listener) listener.onLeftButtonClicked();
                }
            });
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    if(null != listener) listener.onRightButtonClicked(edtInput.getText().toString());
                }
            });
//            tvAutoPut.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    edtInput.setText(default_value);
//                    edtInput.setSelection(edtInput.length());
//                }
//            });
            setOnWindowChangedListener(this);

            edtInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            edtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            BULog.v("创建PopWindow弹窗完成");
        }
        BULog.v("调用创建完成");
    }

    @Override
    public void onWindowChanged(int heightDifference) {
        resetLocationY(heightDifference);
    }

    private void resetLocationY(int y) {
        if(mPopupWindow == null)
            return;
        if(!mPopupWindow.isShowing()) {
            startPopAmin(true, ANIM_TYPE_BLUR);
            mPopupWindow.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vBottom.getLayoutParams();
        params.height = y;
        vBottom.setLayoutParams(params);
    }

    /** 左右按键点击动作的监听器*/
    public interface OnButtonClickListener {
        void onLeftButtonClicked();
        void onRightButtonClicked(String txt);
    }
}
