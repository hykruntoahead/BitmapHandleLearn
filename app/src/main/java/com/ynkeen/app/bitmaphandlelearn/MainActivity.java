package com.ynkeen.app.bitmaphandlelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.ynkeen.app.bitmaphandlelearn.download.DownLoadCompleteReceiver;
import com.ynkeen.app.bitmaphandlelearn.palette.PaletteActivity;
import com.ynkeen.app.bitmaphandlelearn.utils.BitmapUtils;
import com.ynkeen.app.bitmaphandlelearn.download.DownLoadFileClient;
import com.ynkeen.app.bitmaphandlelearn.utils.FileUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIv;
    private Button mBottomHandleBtn, mAllHandleBtn;
    private Bitmap mBitmap;
    public static final String FILE_NAME = "default.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.imageView);
        mBottomHandleBtn = findViewById(R.id.button);
        mAllHandleBtn = findViewById(R.id.button2);
        DownLoadFileClient.getInstance().registerDownloadReceiver(this,new DownLoadCompleteReceiver());
        mBottomHandleBtn.setOnClickListener(this);
        mAllHandleBtn.setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.remote_edited);
        if (mBitmap==null){
            ToastUtils.showShort("图片文件不存在!");
            return;
        }

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
        mIv.setBackground(bitmapDrawable);
    }

    @Override
    public void onClick(View v) {
        if (v == mBottomHandleBtn) {
            startActivity(new Intent(this, PaletteActivity.class));
        }

        if (v == mAllHandleBtn) {
            Bitmap bmp = BitmapUtils.createAllBlurAndMask(mBitmap);
            FileUtils.storeBitmapToPath(bmp,BitmapUtils.getLockScreenPath(FILE_NAME));
            BitmapDrawable drawable = new BitmapDrawable(getResources(),bmp);
            mIv.setBackground(drawable);
        }

        if (v.getId() == R.id.button3){
            DownLoadFileClient.getInstance().downloadReq("https://cdn.jsdelivr.net/gh/suiyuan118144/qqleimg/263f7b97b744eb2a27762963b7359f9f.jpg");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownLoadFileClient.getInstance().unregisterDownloadReceiver();
    }
}
