package com.ynkeen.app.bitmaphandlelearn.palette;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.ynkeen.app.bitmaphandlelearn.BhApplication;
import com.ynkeen.app.bitmaphandlelearn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ykhe
 * date: 20-11-6
 * email: ykhe@grandstream.cn
 * description:
 */
public class SwatchListAdapter extends BaseAdapter {
    private static final String TAG = "SwatchListAdapter";
    private List<Integer> mResList = new ArrayList<>();

    SwatchListAdapter(List<Integer> list){
        mResList.clear();
        mResList.addAll(list);
    }

    @Override
    public int getCount() {
        return mResList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swatch,parent,false);
            holder = new Holder();
            holder.ivPng = convertView.findViewById(R.id.iv_pic);
            holder.tvDes = convertView.findViewById(R.id.tv_des);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        Drawable drawable = BhApplication.getInstance().getResources().getDrawable(mResList.get(position));
        holder.ivPng.setBackground(drawable);
        Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
        final Holder finalHolder = holder;
        Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                Palette.Swatch vibrant =
                        palette.getVibrantSwatch();
                if (vibrant != null) {
                    // If we have a vibrant color
                    // update the title TextView
                    finalHolder.tvDes.setBackgroundColor(
                            vibrant.getRgb());
                    finalHolder.tvDes.setTextColor(
                            vibrant.getTitleTextColor());
                    finalHolder.tvDes.setText("图片序号:"+position);
                }else {
                    Palette.Swatch darkVibrant = palette.getMutedSwatch();
                    Log.d(TAG, "onGenerated: vibrant is null;mutedSwatch is "+darkVibrant);
                    if (darkVibrant!=null){
                        finalHolder.tvDes.setBackgroundColor(
                                darkVibrant.getRgb());
                        finalHolder.tvDes.setTextColor(
                                darkVibrant.getTitleTextColor());
                        finalHolder.tvDes.setText("图片序号:"+position);
                    }
                }
            }
        });
        return convertView;
    }

    static class Holder{
        ImageView ivPng;
        TextView tvDes;
    }
}
