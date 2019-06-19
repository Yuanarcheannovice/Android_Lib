package com.archeanx.lib.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.archeanx.lib.widget.R;


public class CommentInputDialog extends AlertDialog {
    /**
     * 评论数据
     */
    private static final String COMMENT_DATA = "comment_data";
    private EditText mInputEditText;
    /**
     * 留言内容
     */
    private String mCommentStr;
    private TextView mSubmit;

    private OnCommentSubmitListener mOnCommentSubmitListener;

    public CommentInputDialog setOnCommentSubmitListener(OnCommentSubmitListener onCommentSubmitListener) {
        mOnCommentSubmitListener = onCommentSubmitListener;
        return this;
    }

    public CommentInputDialog(@NonNull Context context) {
        this(context, R.style.lw_EdittextAlertDialog);
    }

    public CommentInputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.lw_dialog_comment_input, null);
        setView(dialogView);

        mInputEditText = dialogView.findViewById(R.id.dci_et);
        mSubmit = dialogView.findViewById(R.id.dci_submit);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCommentStr = s.toString();
                if (TextUtils.isEmpty(mCommentStr)) {
                    mSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.lw_txt_color_8));
                    mSubmit.setEnabled(false);
                } else {
                    mSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.lw_txt_color_1));
                    mSubmit.setEnabled(true);
                }
            }
        };
        mInputEditText.removeTextChangedListener(textWatcher);
        mInputEditText.addTextChangedListener(textWatcher);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCommentSubmitListener == null) {
                    return;
                }
                hideKeyBord();
                dismiss();
                mOnCommentSubmitListener.onSubmit(mCommentStr);
                mInputEditText.setText("");
            }
        });
    }

    public void addFilters(InputFilter... filters) {
        if (mInputEditText != null) {
            mInputEditText.setFilters(filters);
        }
    }

    /**
     * @param isEnable 是否允许用户输入表情
     */
    public CommentInputDialog setEnabledEmoji(boolean isEnable) {
        if (!isEnable) {
            addFilters(EMOJI_FILTER);
        }
        return this;
    }

    /**
     * 禁用 用户输入表情 拦截器
     * 取用(https://skyacer.github.io/2017/09/12/Android-Disable-Emoji-in-Edittext/)
     * emoji使用unicode区域，代码点范围从u+1f604到u+1f539。
     * <p>
     * 和character.suregatereserved范围在emoji的范围内。
     */
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    private void hideKeyBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mInputEditText.getWindowToken(), 0);
    }

    @Override
    public void show() {
        super.show();
        if (getWindow() == null) {
            return;
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //弹出软键盘
        InputMethodManager imm = (InputMethodManager) mInputEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        // 必须放到显示对话框下面，否则显示不出效果
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putString(COMMENT_DATA, mCommentStr);
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final String commentStr = savedInstanceState.getString(COMMENT_DATA);
        mInputEditText.setText(commentStr);
    }


    public interface OnCommentSubmitListener {
        void onSubmit(String commentStr);
    }
}
