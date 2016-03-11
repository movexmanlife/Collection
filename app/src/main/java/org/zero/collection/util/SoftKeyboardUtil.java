package org.zero.collection.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * android 软键盘工具类
 * Created by Zero on 2016/2/26.
 */
public class SoftKeyboardUtil {
    private static SoftKeyboardUtil softKeyboardUtil;

    private SoftKeyboardUtil() {
    }

    public static SoftKeyboardUtil getInstance() {
        if (softKeyboardUtil == null) {
            softKeyboardUtil = new SoftKeyboardUtil();
        }
        return softKeyboardUtil;
    }

    /**
     * 监听软键盘是否打开（判断根布局的可视区域与屏幕底部的距离减去根布局与屏幕底部本应有的距离的值，如果这个差大于键盘高度最小值，可以认定键盘弹起了。）
     * @param rootView 根布局
     */
    public boolean isKeyboardShown(View rootView) {
        final int softKeybordHeight = 100;
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        DisplayMetrics metrics = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - rect.bottom;
        return heightDiff > softKeybordHeight * metrics.density;
    }

    /**
     * 切换软键盘显示状态
     */
    public void toggleShowState(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param view 接收软键盘输入的视图，如edittext
     */
    public void hideSoftKeyboard(Context context,View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
