package com.qi.david.spinwheel.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.qi.david.spinwheel.R;
import com.qi.david.spinwheel.exceptions.InsufficientNumberOfColoursException;
import com.qi.david.spinwheel.exceptions.InsufficientNumberOfOptionsException;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinWheel extends View {

    private final static float MAX_ANGLE = 360f;
    private final static float SLOW_FACTOR = 2f / 3f;
    private final static String CIRCLE_COLOR_DEFAULT = "#00ccff";
    private final static String ARROW_COLOR_DEFAULT = "#000000";
    private final static String TEXT_COLOR_DEFAULT = "#FFFFFF";
    private final static int ARROW_SIZE_DEFAULT = 50;
    private final static int TEXT_SIZE_DEFAULT = 30;
    private final static int WHEEL_AXLE_RADIUS_DEFAULT = 10;
    private final static int COLORS_DEFAULT = R.array.wheel_colors_default;
    private final static int MAX_ROTATION_ANGLE_PER_TICK = 10;
    private final static String[] OPTIONS_DEFAULT = new String[]{"Fire", "Water", "Earth", "Wind", "Wood", "Metal"};

    // Base Circle
    private float mCircleX;
    private float mCircleY;
    private float mCircleRadius;
    private int mCircleColor;
    private Paint mPaintCircle;

    // Arrow
    private int mArrowColor;
    private float mArrowWidth;
    private float mArrowHeight;
    private Paint mPaintArrow;

    // Options
    private int mOptionTextSize;
    private int mOptionTextColor;
    private ArrayList mOptions;
    private Paint mPaintOptionsText;

    // Spin Wheel Arcs
    private int[] mWheelArcsColors;
    private Paint mPaintWheel;

    // Spin Wheel Axle
    private float mWheelAxleRadius;
    private Paint mPaintWheelAxle;

    // Spin Wheel Rotation
    private int mRotationAngle = 0;
    private float mRotationAnglePerTick = 1;
    private long mSlowThreshold;
    private long mSpinDuration;
    private CountDownTimer mRotationTimer;

    public SpinWheel(Context context) {
        super(context);
        init(null);
    }

    public SpinWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SpinWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArrow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOptionsText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWheelAxle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWheel = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (set == null) {
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        int colorsResId = ta.getResourceId(R.styleable.CustomView_wheel_colors, 0);
        setColorsByResId(colorsResId);

        mCircleColor = ta.getColor(R.styleable.CustomView_circle_color, Color.parseColor(CIRCLE_COLOR_DEFAULT));
        mPaintCircle.setColor(mCircleColor);

        mArrowColor = ta.getColor(R.styleable.CustomView_arrow_color, Color.parseColor(ARROW_COLOR_DEFAULT));
        mArrowWidth = ta.getDimensionPixelSize(R.styleable.CustomView_arrow_width, ARROW_SIZE_DEFAULT);
        mArrowHeight = ta.getDimensionPixelSize(R.styleable.CustomView_arrow_height, ARROW_SIZE_DEFAULT);
        mPaintArrow.setColor(mArrowColor);

        mOptionTextColor = ta.getColor(R.styleable.CustomView_text_color, Color.parseColor(TEXT_COLOR_DEFAULT));
        mOptionTextSize = ta.getDimensionPixelSize(R.styleable.CustomView_text_size, TEXT_SIZE_DEFAULT);
        mPaintOptionsText.setStyle(Paint.Style.FILL);
        mPaintOptionsText.setTextSize(mOptionTextSize);
        mPaintOptionsText.setColor(mOptionTextColor);
        mOptions = new ArrayList<String>(Arrays.asList(OPTIONS_DEFAULT));

        mPaintWheelAxle.setColor(Color.parseColor(ARROW_COLOR_DEFAULT));
        mWheelAxleRadius = WHEEL_AXLE_RADIUS_DEFAULT;

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCircleX == 0f || mCircleY == 0f) {
            mCircleX = getWidth() / 2;
            mCircleY = getHeight() / 2;
            mCircleRadius = (getWidth() / 2) - (getWidth() * 0.1f);
        }

        drawCircle(canvas);
        drawWheel(canvas);
        drawText(canvas);
        drawAxle(canvas);
        drawArrow(canvas);
    }

    private void drawWheel(Canvas canvas) {

        if (!hasOptions()) {
            return;
        }

        float left = mCircleX - mCircleRadius;
        float top = mCircleY - mCircleRadius;
        float right = mCircleX + mCircleRadius;
        float bottom = mCircleY + mCircleRadius;

        canvas.rotate(mRotationAngle, mCircleX, mCircleY);

        RectF oval = new RectF(left, top, right, bottom);

        float angle = 0;
        for (int i = 0; i < getOptionsSize(); i++) {

            int colorIndex = i % mWheelArcsColors.length;
            mPaintWheel.setColor(mWheelArcsColors[colorIndex]);

            canvas.save();
            canvas.rotate(angle, mCircleX, mCircleY);
            canvas.drawArc(oval, 0, getOptionAngle(), true, mPaintWheel);
            canvas.restore();
            angle += getOptionAngle();
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);
    }

    private void drawArrow(Canvas canvas) {

        float halfWidth = mArrowWidth / 2;
        float halfHeight = mArrowHeight / 2;
        float x = mCircleX - mCircleRadius;
        float y = mCircleY;

        canvas.rotate(-mRotationAngle, mCircleX, mCircleY);

        Path path = new Path();
        path.moveTo(x - halfWidth, y - halfHeight);
        path.lineTo(x + halfWidth, y);
        path.lineTo(x - halfWidth, y + halfHeight);
        path.lineTo(x - halfWidth, y - halfHeight);
        path.close();

        canvas.drawPath(path, mPaintArrow);
    }

    private void drawText(Canvas canvas) {

        if (!hasOptions()) {
            return;
        }

        float x = mCircleX - mCircleRadius + (mCircleRadius * 0.15f);
        float y = mCircleY;
        float textWidth = mCircleRadius - ((mCircleRadius * 0.15f) * 2);
        TextPaint textPaint = new TextPaint();
        textPaint.set(mPaintOptionsText);

        float angle = getOptionAngle() / 2;

        for (int i = 0; i < getOptionsSize(); i++) {
            CharSequence option = TextUtils.ellipsize(mOptions.get(i).toString(), textPaint, textWidth, TextUtils.TruncateAt.END);
            canvas.save();
            canvas.rotate(angle, mCircleX, mCircleY);
            canvas.drawText(option.toString(), x, y, mPaintOptionsText);
            canvas.restore();

            angle += getOptionAngle();
        }
    }

    private void drawAxle(Canvas canvas) {
        canvas.drawCircle(mCircleX, mCircleY, mWheelAxleRadius, mPaintWheelAxle);
    }

    private float getOptionAngle() {
        return MAX_ANGLE / (float) getOptionsSize();
    }

    private int getOptionsSize() {
        return mOptions == null ? 0 : mOptions.size();
    }

    public void setOptions(ArrayList<String> options) {
        if (options.size() > 1) {
            mOptions = options;
            invalidate();
        } else {
            throw new InsufficientNumberOfOptionsException();
        }
    }

    public void setColours(int[] colors) {
        if (colors.length > 1) {
            mWheelArcsColors = colors;
            invalidate();
        } else {
            throw new InsufficientNumberOfColoursException();
        }
    }

    private boolean hasOptions() {
        return mOptions != null && !mOptions.isEmpty();
    }

    public void setColorsByResId(int colorsResId) {
        if (colorsResId == 0) {
            setColorsByResId(COLORS_DEFAULT);
            return;
        }

        int[] colorArray;

        if (isInEditMode()) {
            String[] colorStringTypeArray = getResources().getStringArray(colorsResId);
            colorArray = new int[colorStringTypeArray.length];

            for (int i = 0; i < colorStringTypeArray.length; i++) {
                colorArray[i] = Color.parseColor(colorStringTypeArray[i]);
            }
        } else {
            colorArray = getResources().getIntArray(colorsResId);
        }

        if (colorArray.length < 4) {
            setColorsByResId(COLORS_DEFAULT);
            return;
        }

        setColours(colorArray);
    }

    private void rotate(float angle) {
        mRotationAngle += angle;
        mRotationAngle %= MAX_ANGLE;
        invalidate();
    }

    private void startRotation(long duration) {

        mSpinDuration = duration;
        mSlowThreshold = (long) (duration * SLOW_FACTOR);
        mRotationTimer = new CountDownTimer(duration, 10) {

            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished <= mSlowThreshold) {

                    mRotationAnglePerTick = MAX_ROTATION_ANGLE_PER_TICK * ((float) millisUntilFinished / (float) mSpinDuration);

                } else if (mRotationAnglePerTick < MAX_ROTATION_ANGLE_PER_TICK) {
                    mRotationAnglePerTick = mRotationAnglePerTick * 2;

                    if (mRotationAnglePerTick >= MAX_ROTATION_ANGLE_PER_TICK) {
                        mRotationAnglePerTick = MAX_ROTATION_ANGLE_PER_TICK;
                    }
                }
                rotate(mRotationAnglePerTick);
            }

            public void onFinish() {
                mRotationAnglePerTick = 1;
            }
        }.start();
    }

    private void stopRotation() {
        if (mRotationTimer != null) {
            mRotationTimer.cancel();
        }
    }

    public void spinWheel(int spinDuration) {
        stopRotation();
        startRotation(spinDuration);
    }

}
