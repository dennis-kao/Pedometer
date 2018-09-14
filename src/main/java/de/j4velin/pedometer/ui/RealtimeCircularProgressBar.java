package de.j4velin.pedometer.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

import de.j4velin.pedometer.util.Logger;

/**
 * Real-time android view component that can be used to show a round progress bar.
 * It can be customized with size, stroke size, colors and text etc.
 *
 * Progress change is displayed to the user with as little delay as possible,
 * since there are no animations and UI is drawn separately from the main Android UI thread.
 *
 * Written by:
 * Dennis Kao, June 2018
 *      github.com/dennis-kao
 *
 * Code sources:
 * Kristoffer Matsson, http://kmdev.se, for Canvas, circle and text code
 *      https://github.com/korre/android-circular-progress-bar/blob/master/app/src/main/java/se/kmdev/circularprogressbar/CircularProgressBar.java
 *
 * Chryssa Aliferi, for Thread and SurfaceHolder code
 *      https://examples.javacodegeeks.com/android/core/ui/surfaceview/android-surfaceview-example/
 */
public class RealtimeCircularProgressBar extends SurfaceView implements Runnable {

    //  SurfaceHolder
    private SurfaceHolder surfaceHolder;

    //  Distances and measurements
    private int mViewWidth;
    private int mViewHeight;
    private final float mStartAngle = -90;      // Always start from top (default is: "3 o'clock on a watch.")
    private float mMaxSweepAngle = 360;         // Max degrees to sweep = full circle
    private int mStrokeWidth = 40;              // Width of outline
    private int mMaxProgress = 100;             // Max progress to use
    private int circleGap = 10;
    private int unitTextGap = 120;

    private String unitText = "steps";

    //  Text drawing options
    private boolean mDrawText = true;           // Set to true if progress text should be drawn
    private boolean mRoundedCorners = true;     // Set to true if rounded corners should be applied to outline ends

    //  Colors
    private int mProgressColor = Color.parseColor("#ff5555");   // Outline color
    private int mTextColor = Color.BLACK;       // Progress text color
    private int backgroundCircleColor = Color.parseColor("#B3d3d3d3");  // 70% transparent grey

    //  Font(s)
    private Typeface font;

    //  mPaint is reused to setup all drawn elements on canvas
    private Paint backgroundPaint, progressTextPaint, unitTextPaint, progressBarPaint;                       // Allocate paint outside onDraw to avoid unnecessary object creation

    //  Thread logic
    private Thread thread = null;
    AtomicBoolean running = new AtomicBoolean(false);

    //  Progress data
    private int count = 0;
    private float progress = 0;

    private int backgroundColor;

    public RealtimeCircularProgressBar(Context context) {
        this(context, null);
        init();
    }

    public RealtimeCircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public RealtimeCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        initMeasurments();
        surfaceHolder = getHolder();

        progressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        font = Typeface.createFromAsset(getContext().getAssets(), "font/robotocondensed_regular.ttf");

        this.setZOrderOnTop(true);  //  has to be true for viewPager to animate
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        backgroundColor = Color.parseColor("#fffafafa"); //  background color of AppTheme light

        progressBarPaint.setColor(mProgressColor);
        progressBarPaint.setStrokeWidth(mStrokeWidth - circleGap);
        progressBarPaint.setAntiAlias(true);
        progressBarPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        progressBarPaint.setStyle(Paint.Style.STROKE);

        backgroundPaint.setColor(backgroundCircleColor);
        backgroundPaint.setStrokeWidth(mStrokeWidth);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);

        progressTextPaint.setTextAlign(Paint.Align.CENTER);
        progressTextPaint.setStrokeWidth(0);
        progressTextPaint.setColor(mTextColor);
        progressTextPaint.setTypeface(font);
        progressTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        unitTextPaint.setTextAlign(Paint.Align.CENTER);
        unitTextPaint.setStrokeWidth(0);
        unitTextPaint.setColor(mTextColor);
        unitTextPaint.setTypeface(font);
        unitTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void startDrawing() {

        if (running.compareAndSet(false, true)) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopDrawing() {

        if (running.compareAndSet(true, false)) {

            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        while(running.get()) {

            if(!surfaceHolder.getSurface().isValid())
                continue;

            Canvas canvas = surfaceHolder.lockCanvas();
            onDraw(canvas);
            //Logger.log("Drawing!" + Integer.toString(count));
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        initMeasurments();
        super.onDraw(canvas);

        canvas.drawColor(backgroundColor);

        drawBackgroundArc(canvas);
        drawProgressArc(canvas);

        if (mDrawText) {
            drawText(canvas);
            drawUnitText(canvas);
        }
    }

    private void initMeasurments() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    private void drawProgressArc(Canvas canvas) {

        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = mStrokeWidth / 2f;
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        canvas.drawArc(outerOval, mStartAngle, calcSweepAngleFromProgress(progress), false, progressBarPaint);
    }

    private void drawBackgroundArc(Canvas canvas){

        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = mStrokeWidth / 2f;
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        canvas.drawArc(outerOval, mStartAngle, mMaxSweepAngle, false, backgroundPaint);
    }

    private void drawText(Canvas canvas) {
        progressTextPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 5f);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((progressTextPaint.descent() + progressTextPaint.ascent()) / 2)) ;

        canvas.drawText(Integer.toString(count), xPos, yPos, progressTextPaint);
    }

    private void drawUnitText(Canvas canvas) {
        unitTextPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 15f);

        // Center text
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((unitTextPaint.descent() + unitTextPaint.ascent()) / 2)) + unitTextGap;
        canvas.drawText(unitText, xPos, yPos, unitTextPaint);
    }

    private float calcSweepAngleFromProgress(float progress) {
        return (mMaxSweepAngle / mMaxProgress) * progress;
    }

    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * mMaxProgress) / mMaxSweepAngle);
    }

    public void setProgress(float prog, int c) {
        count = c;
        progress = prog;
    }

    public void setProgress(float prog) {
        progress = prog;
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        progressBarPaint.setColor(color);
    }

    public void setProgressWidth(int width) {
        mStrokeWidth = width;
        progressTextPaint.setStrokeWidth(width);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        progressTextPaint.setColor(color);
    }

    public void showProgressText(boolean show) {
        mDrawText = show;
    }

    public void setCircleGap(int g) {
        circleGap = g;
        progressBarPaint.setStrokeWidth(mStrokeWidth - circleGap);
    }

    public void setNumText(int n) {
        count = n;
        invalidate();
    }

    public void setUnitTextGap(int unitTextGap) {
        this.unitTextGap = unitTextGap;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    public void useRoundedCorners(boolean roundedCorners) {
        mRoundedCorners = roundedCorners;
    }

    public void setBackgroundCircleColor(int color) {
        backgroundColor = color;
        backgroundPaint.setColor(backgroundCircleColor);
    }
}