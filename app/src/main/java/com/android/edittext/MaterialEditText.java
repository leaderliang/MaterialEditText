package com.android.edittext;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * MaterialEditText
 *
 * @author liangyanqiao
 */
public class MaterialEditText extends android.support.v7.widget.AppCompatEditText {

    private static final float TEXT_HORIZONTAL_OFFSET = Utils.dp2px(4f);
    private static final float TEXT_VERTICAL_OFFSET = Utils.dp2px(22f);
    private static final int TEXT_ANIMATION_OFFSET = (int) Utils.dp2px(16);
    private static final float TEXT_SIZE = Utils.dp2px(12f);
    private static final float TEXT_MARGIN = Utils.dp2px(8f);

    private boolean isShown;
    private boolean isUseMaterialEditText;

    /**
     * 动画执行的进度数值
     */
    private float floatFraction;

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        mPaint.setTextSize(TEXT_SIZE);
        mPaint.setColor(Color.BLACK);

        setPadding(getPaddingLeft(), (int) (getPaddingTop() + TEXT_MARGIN + TEXT_SIZE), getPaddingRight(), getPaddingBottom());

        addTextChangedListener(new OnEditTextChangeListener());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 0xff 满值
        mPaint.setAlpha((int) (0xff * floatFraction));
        // 移动的偏移量
        float extraOffSet = TEXT_ANIMATION_OFFSET * (1 - floatFraction);
        // 向上时 extraOffSet  24.0 ----> 0.0          floatFraction 1->0
        // 向下时 extraOffSet  0.0 ----> 24.0          floatFraction 0->1
//        System.out.println("floatFraction= " + floatFraction + "   1 - floatFraction = [" + (1 - floatFraction) + "]" + "  extraOffSet = [" + extraOffSet + "]");
        canvas.drawText(getHint().toString(), TEXT_HORIZONTAL_OFFSET, TEXT_VERTICAL_OFFSET + extraOffSet, mPaint);


    }


    private class OnEditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isShown && TextUtils.isEmpty(s)) {
                isShown = false;
                getAnimator().reverse();
            } else if (!isShown && !TextUtils.isEmpty(s)) {
                isShown = true;

                getAnimator().start();
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
     *
     *  动画显示过，并且没有输入文字时候   执行 hide
     *  ObjectAnimator mObjectAnimatorHide = ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 0);
     *  mObjectAnimatorHide.start();
     *
     *  动画显示过，并且输入文字时候或有文字   执行 show
     *  ObjectAnimator mObjectAnimatorShow = ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 1);
     *  mObjectAnimatorShow.start();
     *
     *  上面代码逐渐优化为 getAnimator()
     *
     * getAnimator
     * @return ObjectAnimator animator
     */
    public ObjectAnimator getAnimator() {
        //为 floatFraction 定义个区间
        return ObjectAnimator.ofFloat(MaterialEditText.this, "floatFraction", 0, 1);
    }



    public void setUseMaterialEditText(boolean isUse){
         this.isUseMaterialEditText = isUse;
         requestLayout();
    }

}