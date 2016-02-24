package org.zero.collection.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 自定义下载进度条
 * Created by Zero on 2016/2/18.
 */
public class DownloadProgressBar extends ProgressBar {
    private String strProgress;
    private Paint mPaint;

    public DownloadProgressBar(Context context) {
        super(context);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(40);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        mPaint.getTextBounds(strProgress, 0, strProgress.length(), rect);
        int x = getWidth() / 2 - rect.centerX();
        int y = getHeight() / 2 - rect.centerY();
        canvas.drawText(strProgress, x, y, mPaint);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        strProgress = progress * 100 / this.getMax() + "%";
    }

    public DownloadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public DownloadProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


}
