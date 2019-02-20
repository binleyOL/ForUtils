package com.wewins.custom.forutils.file;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/19.<b/>
 */
public class TextWatcherForFileName implements TextWatcher {

    String digits = "/\\:*?<>|\"\n\t";
    private EditText editText = null;

    public TextWatcherForFileName(EditText et) {
        editText = et;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = editText.getText().toString();
        String str = stringFilter(editable.toString());
        if(!editable.equals(str)){
            editText.setText(str);
            editText.setSelection(str.length()); //光标置后
        }
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    public void afterTextChanged(Editable s) {}

    /**
     * 过滤掉特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]"; // 要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
