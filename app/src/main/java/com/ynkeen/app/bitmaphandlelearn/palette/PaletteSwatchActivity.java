package com.ynkeen.app.bitmaphandlelearn.palette;

import android.app.ListActivity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ynkeen.app.bitmaphandlelearn.R;

import java.util.Arrays;
import java.util.List;

/**
 * author: ykhe
 * date: 20-11-6
 * description:
 */
public class PaletteSwatchActivity extends ListActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         List<Integer> list = Arrays.asList(R.drawable.d1,R.drawable.d2,R.drawable.d3,
                 R.drawable.d4,R.drawable.d5,R.drawable.d6);
         setListAdapter(new SwatchListAdapter(list));
    }
}
