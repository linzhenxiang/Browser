package com.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;

import com.skin.attr.SkinParam;
import com.skin.utils.L;
import com.vector.VectorDrawable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * Created by zhy on 15/9/22.
 */
public class ResourceManager {
    private static final String DEFTYPE_DRAWABLE = "drawable";
    private static final String DEFTYPE_COLOR = "color";
    private Resources mResources;
    private String mPluginPackageName;
    private String mSuffix;
    private int mSuffixColor = Color.TRANSPARENT;
    private SkinParam skinParam;


    public ResourceManager(Resources res, String pluginPackageName, int color, SkinParam param) {
        mResources = res;
        mPluginPackageName = pluginPackageName;
        skinParam = param;
        if (color != Color.TRANSPARENT) {
            mSuffixColor = color;
        }

    }

    public Drawable getDrawableByName(String name) {
        try {
//            name = appendSuffix(name);
            L.e("name = " + name + " , " + mPluginPackageName);
            Drawable drawable = mResources.getDrawable(mResources.getIdentifier(name, DEFTYPE_DRAWABLE, mPluginPackageName));

            if (mSuffixColor != Color.TRANSPARENT && drawable != null && drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColors(new int[]{mSuffixColor, Color.TRANSPARENT});
            }
            return drawable;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Switch
     * <p/>
     * * @param name
     *
     * @return
     */
    public Drawable[] getSwitchDrawableByName(String name) {
        Drawable[] drawables = new Drawable[2];
        if (!name.contains("?") || mSuffixColor == Color.TRANSPARENT || skinParam == null)
            return null;

        String[] names = name.split("\\?");
        LayerDrawable[] layerDrawables = new LayerDrawable[names.length];
        for (int i = 0; i < names.length; i++) {
            XmlPullParser xmlPullParser = mResources.getXml(mResources.getIdentifier(names[i], DEFTYPE_DRAWABLE, mPluginPackageName));
            try {
                LayerDrawable layerDrawable = (LayerDrawable) LayerDrawable.createFromXml(mResources, xmlPullParser);
                if (i % 2 == 1) {//checked
                    Drawable dwCheck = layerDrawable.findDrawableByLayerId(i == 1 ? skinParam.mSwitchThumb : skinParam.mSwitchTrack);
                    if (dwCheck instanceof GradientDrawable) {
                        if (i == 3) {
                            ((GradientDrawable) dwCheck).setColor(Color.argb(120, Color.red(mSuffixColor), Color.green(mSuffixColor), Color.blue(mSuffixColor)));
                        } else {
                            ((GradientDrawable) dwCheck).setColor(mSuffixColor);
                        }
                        layerDrawables[i] = layerDrawable;
                    }
                } else {//default
                    layerDrawables[i] = layerDrawable;
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        for (int i = 0; i < drawables.length; i++) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, layerDrawables[i * 2 + 1]);
            stateListDrawable.addState(new int[]{}, layerDrawables[i * 2]);

            drawables[i] = stateListDrawable;
        }
        return drawables;
    }

    public Drawable getDrawableXmlByName(String name) throws Resources.NotFoundException {
        try {
            L.e("name = " + name + " , " + mPluginPackageName);

            int resId = mResources.getIdentifier(name, DEFTYPE_DRAWABLE, mPluginPackageName);
            VectorDrawable vectorDrawable = VectorDrawable.create(mResources, resId);
            if (vectorDrawable == null || mSuffixColor == Color.TRANSPARENT)
                return null;
            VectorDrawable.VectorDrawableState vectorDrawableState = vectorDrawable.getVectorState();
            VectorDrawable.VPathRenderer vPathRenderer = vectorDrawableState.getVPathRenderer();
            vPathRenderer.setStorkColor(mSuffixColor);
            return vectorDrawable.getCurrent();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public int getColor(String name) throws Resources.NotFoundException {
//        name = appendSuffix(name);
        L.e("name = " + name);
        return mSuffixColor;
//                mResources.getColor(mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName));
    }

    public ColorStateList getColorStateList(String name) {
        try {
//            name = appendSuffix(name);
            L.e("name = " + name);
            return mResources.getColorStateList(mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String appendSuffix(String name) {
        if (!TextUtils.isEmpty(mSuffix))
            return name += "_" + mSuffix;
        return name;
    }

}
