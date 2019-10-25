package com.hamzaahmedkhan.circulartimerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * The type Circular timer view.
 */
public class CircularTimerView extends View {

    private Paint progressBarPaint;
    private Paint progressBarBackgroundPaint;
    private Paint backgroundPaint;
    private Paint textPaint;

    private float mRadius;
    private RectF mArcBounds = new RectF();

    /**
     * The Draw upto.
     */
    float drawUpto = 0;

    /**
     * Instantiates a new Circular timer view.
     *
     * @param context the context
     */
    public CircularTimerView(Context context) {
        super(context);

        // create the Paint and set its color

    }

    private int progressColor;
    private int progressBackgroundColor;
    private int backgroundColor;
    private float strokeWidthDimension;
    private float backgroundWidth;
    private boolean roundedCorners;
    private float maxValue;

    private int progressTextColor = Color.BLACK;
    private float textSize = 18;
    private String text = "";
    private String suffix = "";
    private String prefix = "";
    private Boolean isClockwise = true;
    private int startingAngle = 270;

    /**
     * The Def style attr.
     */
    int defStyleAttr;

    private CircularTimerListener circularTimerListener;
    private CountDownTimer countDownTimer;

    /**
     * Instantiates a new Circular timer view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CircularTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defStyleAttr = defStyleAttr;
        initPaints(context, attrs);
    }

    /**
     * Instantiates a new Circular timer view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircularTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaints(context, attrs);
    }

    private void initPaints(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularTimerView, defStyleAttr, 0);

        progressColor = ta.getColor(R.styleable.CircularTimerView_progressColor, Color.BLUE);
        backgroundColor = ta.getColor(R.styleable.CircularTimerView_backgroundColor, Color.GRAY);
        progressBackgroundColor = ta.getColor(R.styleable.CircularTimerView_progressBackgroundColor, Color.GRAY);

        strokeWidthDimension = ta.getFloat(R.styleable.CircularTimerView_strokeWidthDimension, 10);
        backgroundWidth = ta.getFloat(R.styleable.CircularTimerView_backgroundWidth, 10);
        roundedCorners = ta.getBoolean(R.styleable.CircularTimerView_roundedCorners, false);
        maxValue = ta.getFloat(R.styleable.CircularTimerView_maxValue, 100);
        progressTextColor = ta.getColor(R.styleable.CircularTimerView_progressTextColor, Color.BLACK);
        textSize = ta.getDimension(R.styleable.CircularTimerView_textSize, 18);
        suffix = ta.getString(R.styleable.CircularTimerView_suffix);
        prefix = ta.getString(R.styleable.CircularTimerView_prefix);
        text = ta.getString(R.styleable.CircularTimerView_progressText);
        isClockwise = ta.getBoolean(R.styleable.CircularTimerView_isClockwise, true);
        startingAngle = ta.getInt(R.styleable.CircularTimerView_startingPoint, 270);

        progressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBarPaint.setStyle(Paint.Style.FILL);
        progressBarPaint.setColor(progressColor);
        progressBarPaint.setStyle(Paint.Style.STROKE);
        progressBarPaint.setStrokeWidth(strokeWidthDimension * getResources().getDisplayMetrics().density);
        if (roundedCorners) {
            progressBarPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            progressBarPaint.setStrokeCap(Paint.Cap.BUTT);
        }
        String pc = String.format("#%06X", (0xFFFFFF & progressColor));
        progressBarPaint.setColor(Color.parseColor(pc));

        progressBarBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBarBackgroundPaint.setStyle(Paint.Style.FILL);
        progressBarBackgroundPaint.setColor(progressBackgroundColor);
        progressBarBackgroundPaint.setStyle(Paint.Style.STROKE);
        progressBarBackgroundPaint.setStrokeWidth(backgroundWidth * getResources().getDisplayMetrics().density);
        progressBarBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);
        String bc = String.format("#%06X", (0xFFFFFF & progressBackgroundColor));
        progressBarBackgroundPaint.setColor(Color.parseColor(bc));



        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);
        String bcfill = String.format("#%06X", (0xFFFFFF & backgroundColor));
        backgroundPaint.setColor(Color.parseColor(bcfill));

        ta.recycle();

        textPaint = new TextPaint();
        textPaint.setColor(progressTextColor);
        String c = String.format("#%06X", (0xFFFFFF & progressTextColor));
        textPaint.setColor(Color.parseColor(c));
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        //paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mouthInset = mRadius / 3;
        canvas.drawCircle(mRadius, mRadius, mouthInset * 2, backgroundPaint);

        mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
        canvas.drawArc(mArcBounds, 0f, 360f, false, progressBarBackgroundPaint);

        if (isClockwise) {
            canvas.drawArc(mArcBounds, startingAngle, (drawUpto / getMaxValue() * 360), false, progressBarPaint);
        } else {
            canvas.drawArc(mArcBounds, startingAngle, (drawUpto / getMaxValue() * -360), false, progressBarPaint);
        }

        if (TextUtils.isEmpty(suffix)) {
            suffix = "";
        }

        if (TextUtils.isEmpty(prefix)) {
            prefix = "";
        }

        String drawnText = prefix + text + suffix;

        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(drawnText, (getWidth() - textPaint.measureText(drawnText)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }




    }

    @Override
    protected void onDetachedFromWindow() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        super.onDetachedFromWindow();


    }

    /**
     * Sets progress.
     *
     * @param f the f
     */
    public void setProgress(float f) {
        drawUpto = f;
        invalidate();
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public float getProgress() {
        return drawUpto;
    }

    /**
     * Gets progress percentage.
     *
     * @return the progress percentage
     */
    public float getProgressPercentage() {
        return drawUpto / getMaxValue() * 100;
    }

    /**
     * Sets progress color.
     *
     * @param color the color
     */
    public void setProgressColor(int color) {
        progressColor = color;
        progressBarPaint.setColor(color);
        invalidate();
    }

    /**
     * Sets progress color.
     *
     * @param color the color
     */
    public void setProgressColor(String color) {
        progressBarPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }

    /**
     * Sets background color.
     *
     * @param color the color
     */
    public void setBackgroundColor(String color) {
        backgroundPaint.setColor(Color.parseColor(color));
        invalidate();
    }


    /**
     * Sets progress background color.
     *
     * @param color the color
     */
    public void setProgressBackgroundColor(int color) {
        progressBackgroundColor = color;
        progressBarBackgroundPaint.setColor(color);
        invalidate();
    }

    /**
     * Sets progress background color.
     *
     * @param color the color
     */
    public void setProgressBackgroundColor(String color) {
        progressBarBackgroundPaint.setColor(Color.parseColor(color));
        invalidate();
    }


    /**
     * Gets max value.
     *
     * @return the max value
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * Sets max value.
     *
     * @param max the max
     */
    public void setMaxValue(float max) {
        maxValue = max;
        invalidate();
    }

    /**
     * Sets stroke width dimension.
     *
     * @param width the width
     */
    public void setStrokeWidthDimension(float width) {
        strokeWidthDimension = width;
        invalidate();
    }

    /**
     * Gets stroke width dimension.
     *
     * @return the stroke width dimension
     */
    public float getStrokeWidthDimension() {
        return strokeWidthDimension;
    }

    /**
     * Sets background width.
     *
     * @param width the width
     */
    public void setBackgroundWidth(float width) {
        backgroundWidth = width;
        invalidate();
    }

    /**
     * Gets background width.
     *
     * @return the background width
     */
    public float getBackgroundWidth() {
        return backgroundWidth;
    }

    /**
     * Sets text.
     *
     * @param progressText the progress text
     */
    public void setText(String progressText) {
        text = progressText;
        invalidate();
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text color.
     *
     * @param color the color
     */
    public void setTextColor(int color) {
        progressTextColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    /**
     * Sets text color.
     *
     * @param color the color
     */
    public void setTextColor(String color) {
        textPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    /**
     * Gets text color.
     *
     * @return the text color
     */
    public int getTextColor() {
        return progressTextColor;
    }

    /**
     * Sets suffix.
     *
     * @param suffix the suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        invalidate();
    }

    /**
     * Gets suffix.
     *
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets prefix.
     *
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        invalidate();
    }

    /**
     * Gets clockwise.
     *
     * @return the clockwise
     */
    public Boolean getClockwise() {
        return isClockwise;
    }

    /**
     * Sets clockwise.
     *
     * @param clockwise the clockwise
     */
    public void setClockwise(Boolean clockwise) {
        isClockwise = clockwise;
        invalidate();
    }

    /**
     * Gets starting angle.
     *
     * @return the starting angle
     */
    public int getStartingAngle() {
        return startingAngle;
    }

    /**
     * Sets starting angle.
     *
     * @param startingAngle 270 for Top                      0 for Right                      90 for Bottom                      180 for Left
     */
    public void setStartingAngle(int startingAngle) {
        this.startingAngle = startingAngle;
        invalidate();
    }


    /**
     * Use this method to initialize Timer, default interval time is 1second, you can use other method to define interval
     *
     * @param circularTimerListener Pass your listener to listen ticks and provide data and to listen finish call
     * @param time                  time in long, e.g 1,2,3,4 or any long digit
     * @param timeFormatEnum        Format to define whether the given long time number is milli, second, minute, hour or day
     */
    public void setCircularTimerListener(final CircularTimerListener circularTimerListener, long time, TimeFormatEnum timeFormatEnum) {
        this.circularTimerListener = circularTimerListener;

        long timeInMillis = 0;
        long intervalDuration = 1000;

        switch (timeFormatEnum) {
            case MILLIS:
                timeInMillis = time;
                break;
            case SECONDS:
                timeInMillis = time * 1000;
                break;
            case MINUTES:
                timeInMillis = time * 1000 * 60;
                break;
            case HOUR:
                timeInMillis = time * 1000 * 60 * 60;
                break;
            case DAY:
                timeInMillis = time * 1000 * 60 * 60 * 24;
                break;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        final long maxTime = timeInMillis;
        countDownTimer = new CountDownTimer(maxTime, intervalDuration) {
            @Override
            public void onTick(long l) {

                double percentTimeCompleted = ((maxTime - l) / (double) maxTime);
                drawUpto = (float) (maxValue * percentTimeCompleted);
                text = circularTimerListener.updateDataOnTick(l);
                invalidate();
            }

            @Override
            public void onFinish() {
                double percentTimeCompleted = 1;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                circularTimerListener.onTimerFinished();
                invalidate();
            }
        };

    }


    /**
     * Use this method to initialize Timer, default interval time is 1second, you can use other method to define interval
     *
     * @param circularTimerListener Pass your listener to listen ticks and provide data and to listen finish call
     * @param time                  time in long, e.g 1,2,3,4 or any long digit
     * @param timeFormatEnum        Format to define whether the given long time number is milli, second, minute, hour or day
     * @param timeinterval          the timeinterval
     */
    public void setCircularTimerListener(final CircularTimerListener circularTimerListener, long time, TimeFormatEnum timeFormatEnum, long timeinterval) {
        this.circularTimerListener = circularTimerListener;

        long timeInMillis = 0;

        switch (timeFormatEnum) {
            case MILLIS:
                timeInMillis = time;
                break;
            case SECONDS:
                timeInMillis = time * 1000;
                break;
            case MINUTES:
                timeInMillis = time * 1000 * 60;
                break;
            case HOUR:
                timeInMillis = time * 1000 * 60 * 60;
                break;
            case DAY:
                timeInMillis = time * 1000 * 60 * 60 * 24;
                break;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }


        final long maxTime = timeInMillis;

        countDownTimer = new CountDownTimer(maxTime, timeinterval) {
            @Override
            public void onTick(long l) {

                double percentTimeCompleted = ((maxTime - l) / (double) maxTime);
                drawUpto = (float) (maxValue * percentTimeCompleted);
                text = circularTimerListener.updateDataOnTick(l);
                invalidate();
            }

            @Override
            public void onFinish() {
                double percentTimeCompleted = 1;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                text = circularTimerListener.updateDataOnTick(0);
                circularTimerListener.onTimerFinished();
                invalidate();
            }
        };

    }


    /**
     * Start timer boolean.
     *
     * @return the boolean
     */
    public boolean startTimer() {
        if (countDownTimer == null) {
            return false;
        } else {
            countDownTimer.start();
            return true;
        }
    }


}

