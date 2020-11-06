package com.ynkeen.app.bitmaphandlelearn;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ynkeen.app.bitmaphandlelearn.download.DownLoadFileClient;
import com.ynkeen.app.bitmaphandlelearn.utils.BitmapUtils;
import com.ynkeen.app.bitmaphandlelearn.utils.FileUtils;

/**
 * author: ykhe
 * date: 20-11-2
 * email: ykhe@grandstream.cn
 * description:
 */
public class GsWallpaperService extends IntentService {
    private static final String TAG = "WallpaperService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GsWallpaperService() {
        super("GsWallpaperService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent==null) return;

        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        String primaryPath = DownLoadFileClient.getInstance().findDownloadPathById(id);
        Log.d(TAG, "onHandleIntent: primary-path="+primaryPath
                +";thread="+Thread.currentThread().getName());

        if (!TextUtils.isEmpty(primaryPath)
                && primaryPath.startsWith(BitmapUtils.wallpaper_path)) {
            String name = primaryPath.replace(BitmapUtils.wallpaper_path,"");

            Log.d(TAG, "onHandleIntent: name="+name);

            Bitmap primaryBmp = BitmapUtils.getPrimaryBitmap(name);
            Bitmap bmp = BitmapUtils.createBottomBlurAndMask(primaryBmp);
            FileUtils.storeBitmapToPath(bmp, BitmapUtils.getEditedPath(name));
            Bitmap bitmap = BitmapUtils.createAllBlurAndMask(primaryBmp);
            FileUtils.storeBitmapToPath(bitmap, BitmapUtils.getLockScreenPath(name));
        }
        //todo 2:remove complete task
        int remove = DownLoadFileClient.getInstance().removeDownloadReq(id);

        Log.d(TAG, "onHandleIntent: remove="+remove+";end");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
