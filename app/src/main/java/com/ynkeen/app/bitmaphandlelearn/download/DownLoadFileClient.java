package com.ynkeen.app.bitmaphandlelearn.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.ynkeen.app.bitmaphandlelearn.utils.BitmapUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * author: ykhe
 * date: 20-11-2
 * email: ykhe@grandstream.cn
 * description: 用于下载文件
 */
public class DownLoadFileClient {
    private static final String TAG = "DownLoadFileClient";
    private DownloadManager mDownloadManager;
    private Context mContext;
    private BroadcastReceiver mReceiver;

    private DownLoadFileClient() {

    }

    public static DownLoadFileClient getInstance() {
        return DownLoadFileClient.HOLDER.API;
    }

    private static final class HOLDER {
        private static final DownLoadFileClient API = new DownLoadFileClient();
    }


    private DownloadManager getDownloadManager() {
        return mDownloadManager == null ? (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE) : mDownloadManager;
    }

    public void registerDownloadReceiver(Context context, BroadcastReceiver receiver) {
        this.mContext = context;
        this.mReceiver = receiver;
        mDownloadManager = getDownloadManager();

        IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    public void unregisterDownloadReceiver() {
        if (mContext == null || mReceiver == null) {
            throw new IllegalArgumentException("please registerDownloadReceiver first");
        }
        mContext.unregisterReceiver(mReceiver);
    }


    /**
     * 请求下载远程资源文件
     *
     * @param remotePath url
     */
    public void downloadReq(String remotePath) {
        if (!isValid(remotePath)){
            ToastUtils.showShort("远程链接无效");
            return;
        }

        Uri uri = Uri.parse(remotePath);
        Log.d(TAG, "downloadReq: uri-type:"+uri.getLastPathSegment());

        // uri 是你的下载地址，可以使用Uri.parse("http://")包装成Uri对象
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(remotePath));
        //隐藏通知项 需要权限 android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        // 设置下载文件存放的路径
        String name = "wallpaper_" + uri.getLastPathSegment();
        String filePath = BitmapUtils.getPrimaryPath(name);
        Uri primaryUri = Uri.fromFile(new File(filePath));
        req.setDestinationUri(primaryUri);

        long reqId = getDownloadManager().enqueue(req);
        Log.d(TAG, "downloadReq: reqId = " + reqId);
    }

    private boolean isValid(String urlString) {
        try {
            new URL(urlString);
            return URLUtil.isValidUrl(urlString)
                    && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException ignored) {

        }

        return false;
    }

    /**
     * 根据id找到下载路径
     *
     * @param reqId downloadManager 请求下载后会产生对应id
     * @return
     */
    public String findDownloadPathById(long reqId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(reqId);
        Cursor c = getDownloadManager().query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        String downloadFilePath = null;
                        String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (downloadFileLocalUri != null) {
                            File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
                            downloadFilePath = mFile.getAbsolutePath();
                        }
                        return downloadFilePath;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
        }
        return null;
    }

    public int removeDownloadReq(long reqId) {
        return getDownloadManager().remove(reqId);
    }

}
