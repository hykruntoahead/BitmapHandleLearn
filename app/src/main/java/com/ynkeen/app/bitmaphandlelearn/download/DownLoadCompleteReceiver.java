package com.ynkeen.app.bitmaphandlelearn.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ynkeen.app.bitmaphandlelearn.GsWallpaperService;

/**
 * author: ykhe
 * date: 20-11-3
 * description:
 */
public class DownLoadCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DownLoadCompleteReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Toast.makeText(context, "编号：" + id + "的下载任务已经完成！", Toast.LENGTH_SHORT).show();
            //将通过id定位下载后文件路径及对图片处理的操作
            intent.setClass(context, GsWallpaperService.class);
            context.startService(intent);
        }
    }
}