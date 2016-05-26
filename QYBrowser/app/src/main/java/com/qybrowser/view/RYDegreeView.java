package com.qybrowser.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.qybrowser.R;


/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class RYDegreeView extends View {

    private final int mWidth = getResources().getDimensionPixelOffset(R.dimen.weather_degreee_size);

    public RYDegreeView(Context context) {
        this(context, null);
    }

    public RYDegreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RYDegreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground("16");
    }


    public void setBackground(String degree) {
        char[] chars = degree.toCharArray();
        Drawable[] drawables = new Drawable[chars.length + 1];
        for (int i = 0; i < chars.length; i++) {

            drawables[i] = getDrawable(String.valueOf(chars[i]));
        }
        drawables[chars.length] = getResources().getDrawable(R.drawable.ic_num_degree);
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        for (int j = 0; j < layerDrawable.getNumberOfLayers(); j++) {
            if (drawables.length < 3) {
                layerDrawable.setLayerInset(j, mWidth * j, 0, mWidth * (layerDrawable.getNumberOfLayers() - j), 0);
            } else if (drawables.length == 3) {
                layerDrawable.setLayerInset(j, mWidth * j, 0, mWidth * (layerDrawable.getNumberOfLayers() - j - 1), 0);
            } else {

            }
        }

        setBackgroundDrawable(layerDrawable);
    }


    private Drawable getDrawable(String degree) {

        int resImg = R.drawable.ic_num_degree_0;
        switch (degree) {
            case "0":
                resImg = R.drawable.ic_num_degree_0;
                break;
            case "1":
                resImg = R.drawable.ic_num_degree_1;
                break;
            case "2":
                resImg = R.drawable.ic_num_degree_2;
                break;
            case "3":
                resImg = R.drawable.ic_num_degree_3;
                break;
            case "4":
                resImg = R.drawable.ic_num_degree_4;
                break;
            case "5":
                resImg = R.drawable.ic_num_degree_5;
                break;
            case "6":
                resImg = R.drawable.ic_num_degree_6;
                break;
            case "7":
                resImg = R.drawable.ic_num_degree_7;
                break;
            case "8":
                resImg = R.drawable.ic_num_degree_8;
                break;
            case "9":
                resImg = R.drawable.ic_num_degree_9;
            case "-":
                resImg = R.drawable.ic_num_degree_;
                break;
        }
        return getResources().getDrawable(resImg);

    }

}
