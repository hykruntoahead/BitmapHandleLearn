package com.ynkeen.app.bitmaphandlelearn.palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.ynkeen.app.bitmaphandlelearn.R;

/**
 * author: ykhe
 * date: 20-11-6
 * description:
 * Palette是一个类似调色板的工具类，根据传入的bitmap，提取出主体颜色，使得图片和颜色更加搭配，界面更协调。
 * Palette 可以从一张图片中提取颜色，我们可以把提取的颜色融入到App UI中，可以使UI风格更加美观融洽。
 * 比如，我们可以从图片中提取颜色设置给ActionBar做背景颜色，这样ActionBar的颜色就会随着显示图片的变化而变化。
 *
 * Palette 原理：通过得到一个 bitmap，通过方法进行分析，
 *          取出 LightVibrantSwatch，DarkVibrantSwatch，LightMutedSwatch，DarkMutedSwatch 这些样本，
 *          然后得到 rgb 值。
 * implementation 'androidx.palette:palette:1.0.0'
 */
public class PaletteActivity extends AppCompatActivity {
    private ImageView mIv;
    private TextView mTv,mTv1,mTv2,mTv3,mTv4,mTv5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        initView();

        getPaletteColor();
    }

    private void initView() {
        mIv = findViewById(R.id.iv_img);
        mTv = findViewById(R.id.textView);
        mTv1 = findViewById(R.id.textView1);
        mTv2 = findViewById(R.id.textView2);
        mTv3 = findViewById(R.id.textView3);
        mTv4 = findViewById(R.id.textView4);
        mTv5 = findViewById(R.id.textView5);
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaletteActivity.this,PaletteSwatchActivity.class));
            }
        });
    }


    /**
     *
     palette.getVibrantColor(Color.BLUE) 获取图片中最活跃的颜色（也可以说整个图片出现最多的颜色）（可传默认值）

     palette.getLightVibrantColor(Color.BLUE) 获取到活跃的明亮的颜色（可传默认值）

     palette.getMutedColor(Color.BLUE) 获取图片中一个最柔和的颜色（可传默认值）

     palette.getDarkMutedColor(Color.BLUE) 获取到柔和的深色的颜色（可传默认值）

     palette.getDarkVibrantColor(Color.BLUE) 获取到活跃的深色的颜色（可传默认值）

     palette.getLightMutedColor(Color.BLUE) 获取到柔和的明亮的颜色（可传默认值）


     使用步骤：
     1.获取Palette对象，也就是图像调色板
     2.获取从图像调色板生成的色样 palette.getVibrantSwatch()
        swatch.getPopulation(): 样本中的像素数量
        swatch.getRgb(): 颜色的RBG值
        swatch.getHsl(): 颜色的HSL值
        swatch.getBodyTextColor(): 主体文字的颜色值
        swatch.getTitleTextColor(): 标题文字的颜色值
     3.从色样中提取相应颜色 lightVibrantSwatch.getRgb();
     */
    private void getPaletteColor() {
        Bitmap bmp = ((BitmapDrawable)mIv.getDrawable()).getBitmap();
        Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                //获取到柔和的深色的颜色（可传默认值）
                int darkMutedColor = palette.getDarkMutedColor(Color.BLUE);//如果分析不出来，则返回默认颜色
                //获取到柔和的明亮的颜色（可传默认值）
                int lightMutedColor = palette.getLightMutedColor(Color.BLUE);
                //获取到活跃的深色的颜色（可传默认值）
                int darkVibrantColor = palette.getDarkVibrantColor(Color.BLUE);
                //获取到活跃的明亮的颜色（可传默认值）
                int lightVibrantColor = palette.getLightVibrantColor(Color.BLUE);
                //获取图片中一个最柔和的颜色（可传默认值）
                int mutedColor = palette.getMutedColor(Color.BLUE);
                //获取图片中最活跃的颜色（也可以说整个图片出现最多的颜色）（可传默认值）
                int vibrantColor = palette.getVibrantColor(Color.BLUE);
                //获取某种特性颜色的样品
                Palette.Swatch lightVibrantSwatch = palette.getVibrantSwatch();
                //谷歌推荐的：图片的整体的颜色rgb的混合值---主色调
                int rgb = lightVibrantSwatch.getRgb();
                //谷歌推荐：图片中间的文字颜色
                int bodyTextColor = lightVibrantSwatch.getBodyTextColor();
                //谷歌推荐：作为标题的颜色（有一定的和图片的对比度的颜色值）
                int titleTextColor = lightVibrantSwatch.getTitleTextColor();
                //颜色向量
                float[] hsl = lightVibrantSwatch.getHsl();
                //分析该颜色在图片中所占的像素多少值
                int population = lightVibrantSwatch.getPopulation();
//
//                helper.setText(R.id.articleListTitle, item.getTitle())
//                        .setTextColor(R.id.articleListTitle, titleTextColor)
//                        .setBackgroundColor(R.id.articleListTitle, getTranslucentColor(0.8f, rgb));

                mTv.setText("darkMutedColor");
                mTv.setBackgroundColor(darkMutedColor);
                mTv1.setText("lightMutedColor");
                mTv1.setBackgroundColor(lightMutedColor);
                mTv2.setText("darkVibrantColor");
                mTv2.setBackgroundColor(darkVibrantColor);
                mTv3.setText("lightVibrantColor");
                mTv3.setBackgroundColor(lightVibrantColor);
                mTv4.setText("mutedColor");
                mTv4.setBackgroundColor(mutedColor);
                mTv5.setText("vibrantColor");
                mTv5.setBackgroundColor(vibrantColor);
            }
        });

    }


    /**
     * @param percent   透明度
     * @param rgb   RGB值
     * @return 最终设置过透明度的颜色值
     */
    protected int getTranslucentColor(float percent, int rgb) {
        int blue = Color.blue(rgb);
        int green = Color.green(rgb);
        int red = Color.red(rgb);
        int alpha = Color.alpha(rgb);
        alpha = Math.round(alpha * percent);
        return Color.argb(alpha, red, green, blue);
    }

}
