package com.aiitec.contentproviderdemo.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


/**
 * @author ailibin
 * @time 2018/12/20
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    private BitmapPool mBitmapPool;
    private static float mRadius;
    private static float mDiameter;
    private static CornerType mCornerType = CornerType.ALL;

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    /**
     * 画指定位置的圆角
     */
    public enum CornerType {
        /**
         * 所有角
         */
        ALL,
        /**
         * 左上
         */
        LEFT_TOP,
        /**
         * 左下
         */
        LEFT_BOTTOM,
        /**
         * 右上
         */
        RIGHT_TOP,
        /**
         * 右下
         */
        RIGHT_BOTTOM,
        /**
         * 左侧
         */
        LEFT,
        /**
         * 右侧
         */
        RIGHT,
        /**
         * 下侧(左下和右下)
         */
        BOTTOM,
        /**
         * 上侧(左上和右上)
         */
        TOP,
    }

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
//        super(context);
        mRadius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

//    public GlideRoundTransform(Context context, float leftdp) {
//        super(context);
//        leftRadius = Resources.getSystem().getDisplayMetrics().density * leftdp;
//    }

    public GlideRoundTransform(Context context, int radius, CornerType type) {
//        super(context);
        mBitmapPool = Glide.get(context).getBitmapPool();
        mCornerType = type;
        mRadius = Resources.getSystem().getDisplayMetrics().density * radius;
        mDiameter = 2 * mRadius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        drawRoundRect(canvas, paint, width, height);
        return result;
    }

//    @Override
//    public String getId() {
//        return getClass().getName() + Math.round(mRadius);
//    }

    private static void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        Log.e("ailibin", "drawRoundRect");
        switch (mCornerType) {
            case LEFT_TOP:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case LEFT_BOTTOM:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case RIGHT_TOP:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case RIGHT_BOTTOM:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case TOP:
                drawTopCorner(canvas, paint, width, height);
                break;
            case ALL:
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
                break;
            default:
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
                break;
        }
    }

    /**
     * 画左上角
     */
    private static void drawLeftTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
    }

    /**
     * 画左下角
     */
    private static void drawLeftBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右上角
     */
    private static void drawRightTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画右下角
     */
    private static void drawRightBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(0, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画左角
     */
    private static void drawLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height - mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右角
     */
    private static void drawRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height - mRadius), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画上角
     */
    private static void drawTopCorner(Canvas canvas, Paint paint, float width, float height) {
        Log.e("ailibin", "drawTopCorner");
        canvas.drawRect(new RectF(0, mRadius, width, height), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画下角
     */
    private static void drawBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

//    @Override
//    public String getId() {
//        return "RoundedTransformation(radius=" + mRadius + ", diameter=" + mDiameter + ")";
//    }


}