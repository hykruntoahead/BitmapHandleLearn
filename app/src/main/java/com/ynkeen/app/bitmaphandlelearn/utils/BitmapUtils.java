package com.ynkeen.app.bitmaphandlelearn.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import androidx.annotation.ColorInt;

import com.ynkeen.app.bitmaphandlelearn.BhApplication;

import java.io.File;

/**
 * create by heyukun on 2020/10/31  10:17
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

//    public static final String NAME= "wp.png";

    //壁纸列表图片存放位置
    public final static String wallpaper_path = "/sdcard/wallpaper/primary/";
    //实际生效图片存放位置
    public final static String wallpaper_path_edited = "/sdcard/wallpaper/edited/";

    public final static String wallpaper_path_lockscreen = "/sdcard/wallpaper/lockscreen/";

    private static int scaleFactor = 1;

    public static final int RADIUS_RS_BLUR = 35;

    public static final int DEFAULT_HEIGHT_BOTTOM = 30;

    public static final int DEFAULT_DISPLAY_WIDTH = 240;
    public static final int DEFAULT_DISPLAY_HEIGHT = 320;

    public static Bitmap getPrimaryBitmap(String name) {
        return getBitmapByPath(wallpaper_path, name);
    }


    private static Bitmap getBitmapByPath(String dir, String name) {
        if (FileUtils.createOrExistsDir(dir)) {
            StringBuilder pathBuilder = new StringBuilder(dir);
            if (!pathBuilder.toString().endsWith("/")) {
                pathBuilder.append("/");
            }
            pathBuilder.append(name);
            String path = pathBuilder.toString();
            Log.d(TAG, "getBitmapByPath: has dir" + dir + ";path=" + path);
            if (FileUtils.isFileExists(path)) {
                return  BitmapFactory.decodeFile(path);
            }
            return null;
        }
        return null;
    }



    public static String getPrimaryPath(String name) {
        return wallpaper_path  + name;
    }


    public static String getEditedPath(String name) {
        return wallpaper_path_edited + name;
    }

    public static String getLockScreenPath(String name) {
        return wallpaper_path_lockscreen + name;
    }

    public static Bitmap createBottomBlurAndMask(Bitmap primaryBitmap) {
        //todo 1:将原图缩放图片至合适比率
        Bitmap scaleBmp = getDefaultSizeScaleBmp(primaryBitmap);
        int width = scaleBmp.getWidth();
        int height = scaleBmp.getHeight();
        Log.d(TAG, "createBottomBlurAndMask: width=" + width + "height=" + height);

        // alpha:56%:(8f?)/ 100*255;#004b73
        //裁剪
        Bitmap bottomBitmap = cropBitmap(scaleBmp, height - DEFAULT_HEIGHT_BOTTOM*scaleFactor, DEFAULT_HEIGHT_BOTTOM*scaleFactor);
        Bitmap copyBmp = bottomBitmap.copy(bottomBitmap.getConfig(), true);
        copyBmp = doBlur(copyBmp, RADIUS_RS_BLUR, true);

        Bitmap gsBmp = mask(copyBmp, Color.parseColor("#50004b73"));

        //合成
        return compose(scaleBmp, gsBmp);
    }


    public static Bitmap createAllBlurAndMask(Bitmap primaryBitmap) {
        //todo 1:将原图缩放图片至合适比率
        Bitmap scaleBmp = getDefaultSizeScaleBmp(primaryBitmap);
        int width = scaleBmp.getWidth();
        int height = scaleBmp.getHeight();
        Log.d(TAG, "createBottomBlurAndMask: width=" + width + "height=" + height);
        // alpha:20%(33?); #000000
        Bitmap copyBmp = scaleBmp.copy(scaleBmp.getConfig(), true);
        Bitmap maskBmp = mask(copyBmp, Color.parseColor("#33000000"));
        //高斯模糊
        Bitmap handleScaleBmp = doBlur(maskBmp, RADIUS_RS_BLUR, true);

        Bitmap bottomBitmap = Bitmap.createBitmap(width, DEFAULT_HEIGHT_BOTTOM*scaleFactor, Bitmap.Config.ARGB_8888);
        // alpha:16%(29?);#000000
        bottomBitmap.eraseColor(Color.parseColor("#29000000"));

        return compose(handleScaleBmp, bottomBitmap);
    }


    /**
     * 将bitmap缩放至默认屏幕尺寸大小
     * @param bmp
     * @return
     */
    private static Bitmap getDefaultSizeScaleBmp(Bitmap bmp) {
        int w = DEFAULT_DISPLAY_WIDTH;
        int h = DEFAULT_DISPLAY_HEIGHT;
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        if (bmpW>DEFAULT_DISPLAY_WIDTH && bmpH>DEFAULT_DISPLAY_HEIGHT) {
            if (bmpW/DEFAULT_DISPLAY_WIDTH>=bmpH/DEFAULT_DISPLAY_HEIGHT){
                scaleFactor = bmpH/DEFAULT_DISPLAY_HEIGHT;
            } else {
                scaleFactor = bmpW/DEFAULT_DISPLAY_WIDTH;
            }
            Log.d(TAG, "getDefaultSizeScaleBmp: scaleFactor="+scaleFactor);
            w = scaleFactor*w;
            h = scaleFactor*h;
        }
        return Bitmap.createScaledBitmap(bmp, w, h, true);

    }


    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的bottom图像
     */
    private static Bitmap cropBitmap(Bitmap bitmap, int startY, int cropH) {
        return Bitmap.createBitmap(bitmap, 0, startY, bitmap.getWidth(), cropH, null, false);
    }

    /**
     * 合成
     *
     * @param primaryBitmap
     * @param bottomBitmap
     */
    private static Bitmap compose(Bitmap primaryBitmap, Bitmap bottomBitmap) {
        if (primaryBitmap == null || primaryBitmap.isRecycled()) {
            Log.e(TAG, "compose ERROR: primaryBitmap is not valuable");
            return null;
        }
        Bitmap composedBitmap = Bitmap.createBitmap(primaryBitmap.getWidth(), primaryBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (composedBitmap == null || composedBitmap.isRecycled()) {
            Log.e(TAG, "compose ERROR: composedBitmap is not valuable");
            return null;
        }
        if (bottomBitmap == null || bottomBitmap.isRecycled()) {
            Log.e(TAG, "compose ERROR: resultBitmap is not valuable");
            return null;
        }
        Canvas cv = new Canvas(composedBitmap);
        cv.drawBitmap(primaryBitmap, 0, 0, null);
        float top = primaryBitmap.getHeight() - bottomBitmap.getHeight();
        cv.drawBitmap(bottomBitmap, 0, top, null);
        cv.save();
        cv.restore();
        return composedBitmap;
    }


    /**
     * 蒙版
     *
     * @param bitmap
     * @param maskColor
     * @return
     */
    private static Bitmap mask(Bitmap bitmap, @ColorInt int maskColor) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        Bitmap maskBitmap = Bitmap.createBitmap(width,height,
//                Bitmap.Config.ARGB_8888);
//        maskBitmap.eraseColor(maskColor);

//        int[] picPixels = new int[width*height];
//        int[] maskPixels = new int[width*height];
//        bitmap.getPixels(picPixels, 0, width, 0, 0, width, height);
//        maskBitmap.getPixels(maskPixels, 0, width, 0, 0, width, height);
//        for(int i = 0; i < maskPixels.length; i++){
//            if(maskPixels[i] == 0xff000000){
//                picPixels[i] = 0;
//            }else if(maskPixels[i] == 0){
//                //donothing
//            }else{
//                //把mask的a通道应用与picBitmap
//                maskPixels[i] &= 0xff000000;
//                maskPixels[i] = 0xff000000 - maskPixels[i];
//                picPixels[i] &= 0x00ffffff;
//                picPixels[i] |= maskPixels[i];
//            }
//        }
//
//        Bitmap resultBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
//        //生成前置图片添加蒙板后的bitmap:resultBitmap
//        resultBitmap.setPixels(picPixels, 0, width, 0, 0, width, height);
//        return resultBitmap;
        return getBlackImage(bitmap, maskColor);
    }


    private static Bitmap getBlackImage(Bitmap bm, @ColorInt int color) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bmp);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, 0, 0, paint);
        canvas.drawColor(color);
        return bmp;

    }


    /**
     * 高斯模糊
     *
     * @param source
     * @param radius
     * @return
     */
    private static Bitmap rsBlur(Bitmap source, int radius) {

        Log.i(TAG, "origin size:" + source.getWidth() + "*" + source.getHeight());
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap inputBmp = Bitmap.createScaledBitmap(source, width, height, false);

        RenderScript renderScript = RenderScript.create(BhApplication.getInstance());

        Log.i(TAG, "scale size:" + inputBmp.getWidth() + "*" + inputBmp.getHeight());

        // Allocate memory for Renderscript to work with

        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);

        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);

        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);


        renderScript.destroy();
        return inputBmp;
    }

    /**
     * 毛玻璃效果
     *
     * @param sentBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     */
    private static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

}
