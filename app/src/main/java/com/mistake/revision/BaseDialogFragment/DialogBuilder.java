package com.mistake.revision.BaseDialogFragment;

import androidx.annotation.StyleRes;

import java.lang.reflect.Constructor;

public class DialogBuilder<T> {

    private Object view;
    private float mAlpha = 1;
    private boolean mAutoDismiss = false;
    private boolean mCancelable = true;
    private int mAnimation = 0;
    private int mGravity;

    public Class<T> tClass;

    public DialogBuilder() {
    }

    public DialogBuilder(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * 设置布局资源，可以为 ID，也可以是 View
     */
    public DialogBuilder<T> setContentView(Object view) {
        this.view = view;
        return this;
    }

    /**
     * 设置透明度透明度
     *
     * @param alpha 从 0 - 1
     */
    public DialogBuilder<T> setAlpha(float alpha) {
        this.mAlpha = alpha;
        return this;
    }

    /**
     * 若为 true 所有的点击事件都不起作用，否则相反
     */
    public DialogBuilder<T> setAutoDismiss(boolean autoDismiss) {
        this.mAutoDismiss = autoDismiss;
        return this;
    }

    /**
     * 若为 false，对话框不可取消
     */
    public DialogBuilder<T> setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        return this;
    }

    /**
     * 设置动画
     */
    public DialogBuilder<T> setAnimation(@StyleRes int animation) {
        this.mAnimation = animation;
        return this;
    }

    /**
     * 设置对话框位置
     */
    public DialogBuilder<T> setGravity(int gravity) {
        this.mGravity = gravity;
        return this;
    }

    /**
     * @return 对话框的实例
     */
    public T build() {
        if (tClass != null) {
            try {
                Constructor<T> constructor = tClass.getConstructor(
					Object.class, float.class, boolean.class, boolean.class, int.class,int.class);
                return constructor.newInstance(view, mAlpha, mAutoDismiss, mCancelable,mAnimation,mGravity);
            } catch (Exception e) {
                throw new RuntimeException("创建 "+tClass.getName()+" 失败，原因可能是构造参数有问题："+e.getMessage());
            }
        } else {
            return (T) BaseDialogFragmnet.newInstance(view, mAlpha, mAutoDismiss, mCancelable,mAnimation,mGravity);
        }
	}
}
