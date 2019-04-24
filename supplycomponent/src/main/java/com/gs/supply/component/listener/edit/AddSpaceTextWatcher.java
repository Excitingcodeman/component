package com.gs.supply.component.listener.edit;

import android.support.annotation.StringDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author husky
 * create on 2019/4/12-11:08
 */
public class AddSpaceTextWatcher implements TextWatcher {

    /**
     * 默认类型
     */
    public static final String defaultType = "defaultType";
    /**
     * 银行卡类型
     */
    public static final String bankCardNumberType = "bankCardNumberType";
    /**
     * 手机号类型
     */
    public static final String mobilePhoneNumberType = "mobilePhoneNumberType";
    /**
     * 身份证类型
     */
    public static final String IDCardNumberType = "IDCardNumberType";

    @StringDef({defaultType, bankCardNumberType, mobilePhoneNumberType, IDCardNumberType})
    public @interface SpaceType {
    }

    private String spaceType;

    /**
     * text改变之前的长度
     */
    private int beforeTextLength = 0;
    private int onTextLength = 0;
    private boolean isChanged = false;
    private StringBuffer buffer = new StringBuffer();
    /**
     * 改变之前text空格数量
     */
    int spaceNumberA = 0;
    private EditText editText;
    /**
     * text最大长度限制
     */
    private int maxLength;

    /**
     * 记录光标的位置
     */
    private int location = 0;
    /**
     * 是否是主动设置text
     */
    private boolean isSetText = false;


    protected ImageView iv_delete;

    public AddSpaceTextWatcher(ImageView iv_delete, EditText editText, int maxLength) {
        this.iv_delete = iv_delete;
        this.editText = editText;
        this.maxLength = maxLength;
        if (editText == null) {
            new NullPointerException("editText is null");
        }
        spaceType = defaultType;
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                maxLength)});
        editText.addTextChangedListener(this);
    }

    public AddSpaceTextWatcher(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
        if (editText == null) {
            new NullPointerException("editText is null");
        }
        spaceType = defaultType;
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                maxLength)});
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        if (null != s) {
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            spaceNumberA = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    spaceNumberA++;
                }
            }
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null != s) {
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || onTextLength > maxLength
                    || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;

            String contents = s.toString();
            int length = contents.length();
            if (null!=iv_delete){
                if (length > 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            location = editText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) {
                // 删掉所有空格
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int spaceNumberB = 0;
            while (index < buffer.length()) {
                // 插入所有空格
                spaceNumberB = insertSpace(index, spaceNumberB);
                index++;
            }

            String str = buffer.toString();

            // 下面是计算光位置的
            if (spaceNumberB > spaceNumberA) {
                location += (spaceNumberB - spaceNumberA);
                spaceNumberA = spaceNumberB;
            }
            if (isSetText) {
                location = str.length();
                isSetText = false;
            } else if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }
            updateContext(s, str);
            isChanged = false;
        }
    }

    /**
     * 更新编辑框中的内容
     *
     * @param editable
     * @param values
     */
    private void updateContext(Editable editable, String values) {
        if (spaceType == IDCardNumberType) {
            editable.replace(0, editable.length(), values);
        } else {
            editText.setText(values);
            try {
                editText.setSelection(location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据类型插入空格
     *
     * @param index
     * @param spaceNumberAfter
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int insertSpace(int index, int spaceNumberAfter) {
        switch (spaceType) {
            case defaultType:
                // 相隔四位空格
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case bankCardNumberType:
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case mobilePhoneNumberType:
                if (index == 3
                        || ((index > 7) && ((index - 3) % (4 * spaceNumberAfter) == spaceNumberAfter))) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case IDCardNumberType:
                if (index == 6
                        || ((index > 10) && ((index - 6) % (4 * spaceNumberAfter) == spaceNumberAfter))) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            default:
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
        }
        return spaceNumberAfter;
    }

    /***
     * 计算需要的空格数
     *
     * @return 返回添加空格后的字符串长度
     * @see [类、类#方法、类#成员]
     */
    private int computeSpaceCount(CharSequence charSequence) {
        buffer.delete(0, buffer.length());
        buffer.append(charSequence.toString());
        int index = 0;
        int spaceNumberB = 0;
        while (index < buffer.length()) {
            // 插入所有空格
            spaceNumberB = insertSpace(index, spaceNumberB);
            index++;
        }
        buffer.delete(0, buffer.length());
        return index;
    }

    /**
     * 设置空格类型
     *
     * @param spaceType
     */
    public void setSpaceType(@SpaceType String spaceType) {
        this.spaceType = spaceType;
    }

    /**
     * 设置输入字符
     *
     * @param charSequence
     * @return 返回设置成功失败
     * @see [类、类#方法、类#成员]
     */
    public boolean setText(CharSequence charSequence) {
        if (editText != null && !TextUtils.isEmpty(charSequence) && computeSpaceCount(charSequence) <= maxLength) {
            isSetText = true;
            editText.removeTextChangedListener(this);
            editText.setText(charSequence);
            editText.addTextChangedListener(this);
            return true;
        }
        return false;
    }

    /**
     * 得到输入的字符串去空格后的字符串
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getTextNotSpace() {
        if (editText != null) {
            return delSpace(editText.getText().toString());
        }
        return null;
    }

    /**
     * 得到输入的字符串去空格后的长度
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int getLenghtNotSpace() {
        if (editText != null) {
            return getTextNotSpace().length();
        }
        return 0;
    }

    /**
     * 得到空格数量
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int getSpaceCount() {
        return spaceNumberA;
    }

    /**
     * 去掉字符空格，换行符等
     *
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }


}
