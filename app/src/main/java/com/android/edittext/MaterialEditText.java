package com.android.edittext;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * MaterialEditText
 *
 * @author liang
 */
public class MaterialEditText extends android.support.v7.widget.AppCompatEditText {

    private static final float TEXT_HORIZONTAL_OFFSET = Utils.dp2px(4f);
    private static final float TEXT_VERTICAL_OFFSET = Utils.dp2px(22f);
    private static final int TEXT_ANIMATION_OFFSET = (int) Utils.dp2px(16);
    private static final float TEXT_SIZE = Utils.dp2px(12f);
    private static final float TEXT_MARGIN = Utils.dp2px(8f);

    private boolean isShown;
    private boolean mIsUseMaterialEditText;

    /**
     * 动画执行的进度数值
     */
    private float floatFraction;

    private Rect mRectPadding = new Rect();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        mIsUseMaterialEditText = typedArray.getBoolean(R.styleable.MaterialEditText_useMaterialEditText, true);
        typedArray.recycle();

        mPaint.setTextSize(TEXT_SIZE);
        mPaint.setColor(Color.BLACK);

        /*mPaint.setStyle(Paint.Style.STROKE);
         mPaint.setStrokeWidth(Utils.dp2px(1));*/


        onMaterialEditTextChanged();
        addTextChangedListener(new OnEditTextChangeListener());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       /* Rect backRect = getBackground().getBounds();
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(backRect, mPaint);
        mPaint.setColor(Color.BLACK);*/

        /*0xff 满值*/
        mPaint.setAlpha((int) (0xff * floatFraction));
        /*移动的偏移量*/
        float extraOffSet = TEXT_ANIMATION_OFFSET * (1 - floatFraction);
        /*向上时 extraOffSet  24.0 ----> 0.0          floatFraction 1->0
          向下时 extraOffSet  0.0 ----> 24.0          floatFraction 0->1*/
//        System.out.println("floatFraction= " + floatFraction + "   1 - floatFraction = [" + (1 - floatFraction) + "]" + "  extraOffSet = [" + extraOffSet + "]");
        canvas.drawText(getHint().toString(), TEXT_HORIZONTAL_OFFSET, TEXT_VERTICAL_OFFSET + extraOffSet, mPaint);


    }


    private class OnEditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mIsUseMaterialEditText) {
                if (isShown && TextUtils.isEmpty(s)) {
                    isShown = false;
                    getAnimator().reverse();
                } else if (!isShown && !TextUtils.isEmpty(s)) {
                    isShown = true;
                    getAnimator().start();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public float getFloatFraction() {
        return floatFraction;
    }

    public void setFloatFraction(float floatFraction) {
        this.floatFraction = floatFraction;
        invalidate();
    }


    /**
     * 动画显示过，并且没有输入文字时候   执行 hide
     * ObjectAnimator mObjectAnimatorHide = ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 0);
     * mObjectAnimatorHide.start();
     * <p>
     * 动画显示过，并且输入文字时候或有文字   执行 show
     * ObjectAnimator mObjectAnimatorShow = ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 1);
     * mObjectAnimatorShow.start();
     * <p>
     * 上面代码逐渐优化为 getAnimator()
     * <p>
     * getAnimator
     *
     * @return ObjectAnimator animator
     */
    public ObjectAnimator getAnimator() {
        //为 floatFraction 定义个区间
        return ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 0, 1);
    }


    public void setIsUseMaterialEditText(boolean isUse) {
        if (mIsUseMaterialEditText != isUse) {
            this.mIsUseMaterialEditText = isUse;
            onMaterialEditTextChanged();
        }
    }

    public void onMaterialEditTextChanged() {
        // 获取 view 自身的 padding
        getBackground().getPadding(mRectPadding);
        if (mIsUseMaterialEditText) {
            setPadding(getPaddingLeft(), (int) (mRectPadding.top + TEXT_MARGIN + TEXT_SIZE), getPaddingRight(), getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(), mRectPadding.top, getPaddingRight(), getPaddingBottom());
        }

    }


}