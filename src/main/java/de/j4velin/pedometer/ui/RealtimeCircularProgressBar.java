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

import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

/**
 * Simple single android view component that can be used to showing a round progress bar.
 * It can be customized with size, stroke size, colors and text etc.
 * Progress change will be animated.
 * Created by Kristoffer, http://kmdev.se
 */
public class RealtimeCircularProgressBar extends SurfaceView implements Runnable {

    private int mViewWidth;
    private int mViewHeight;

    private final float mStartAngle = -90;      // Always start from top (default is: "3 o'clock on a watch.")
    private float mSweepAngle = 0;              // How long to sweep from mStartAngle
    private float mMaxSweepAngle = 360;         // Max degrees to sweep = full circle
    private int mStrokeWidth = 20;              // Width of outline
    private int mAnimationDuration = 400;       // Animation duration for progress change
    private int mMaxProgress = 100;             // Max progress to use
    private boolean mDrawText = true;           // Set to true if progress text should be drawn
    private boolean mRoundedCorners = true;     // Set to true if rounded corners should be applied to outline ends
    private int mProgressColor = Color.CYAN;   // Outline color
    private int mTextColor = Color.BLACK;       // Progress text color
    private int backgroundCircleColor = Color.LTGRAY;
    private int circleGap = 10;
    private Typeface font;
    private int num;
    private int progress = 0;

    private Paint mPaint, numPaint;                 // Allocate paint outside onDraw to avoid unnecessary object creation

    //  Runnable Variables
    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    volatile boolean running = false;

    public RealtimeCircularProgressBar(Context context) {
        this(context, null);
        init(context);
    }

    public RealtimeCircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }


    public RealtimeCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        initMeasurments();
        surfaceHolder = getHolder();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        font = Typeface.createFromAsset(context.getAssets(), "font/robotocondensed_regular.ttf");

        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public void onResumeSurfaceView() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseSurfaceView() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(running) {
//            long startTime = System.currentTimeMillis();

            if(!surfaceHolder.getSurface().isValid()) //  only update UI when steps text change
                continue;

            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear the previous frame
            draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);

//                long estimatedTime = System.currentTimeMillis() - startTime;
//                Logger.log("Time taken to update UI  (ms): " + Long.toString(estimatedTime));
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        initMeasurments();
        super.onDraw(canvas);

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

        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(outerOval, mStartAngle, mSweepAngle, false, mPaint);
    }

    private void drawBackgroundArc(Canvas canvas){

        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = mStrokeWidth / 2f;
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        mPaint.setColor(backgroundCircleColor);
        mPaint.setStrokeWidth(mStrokeWidth - circleGap);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(outerOval, mStartAngle, mMaxSweepAngle, false, mPaint);
    }

    private void drawText(Canvas canvas) {
        numPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 5f);
        numPaint.setTextAlign(Paint.Align.CENTER);
        numPaint.setStrokeWidth(0);
        numPaint.setColor(mTextColor);
        numPaint.setTypeface(font);
        numPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((numPaint.descent() + numPaint.ascent()) / 2)) ;

        canvas.drawText(Integer.toString(num), xPos, yPos, numPaint);
    }

    private void drawUnitText(Canvas canvas) {
        mPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 15f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTextColor);
        mPaint.setTypeface(font);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Center text
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2)) + 150; //  150 is the distance between the number the STEPS unit text

        canvas.drawText("STEPS", xPos, yPos, mPaint);
    }

    private float calcSweepAngleFromProgress(int progress) {
        return (mMaxSweepAngle / mMaxProgress) * progress;
    }

    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * mMaxProgress) / mMaxSweepAngle);
    }

    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    public void setProgress() {
        ValueAnimator animator = ValueAnimator.ofFloat(mSweepAngle, calcSweepAngleFromProgress(progress));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(mAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
            }
        });
        animator.start();
    }

    public void setProgress(int prog, int number) {
        num = number;
        progress = prog;
        setProgress();
    }

    public void setProgress(int prog) {
        progress = prog;
        setProgress();
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    public void setProgressWidth(int width) {
        mStrokeWidth = width;
        invalidate();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        invalidate();
    }

    public void showProgressText(boolean show) {
        mDrawText = show;
        invalidate();
    }

    public void setCircleGap(int g) {
        circleGap = g;
        invalidate();
    }

    public void setNumText(int n) {
        num = n;
        invalidate();
    }

    public void setmAnimationDuration(int d) {
        mAnimationDuration = d;
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    public void useRoundedCorners(boolean roundedCorners) {
        mRoundedCorners = roundedCorners;
        invalidate();
    }
}