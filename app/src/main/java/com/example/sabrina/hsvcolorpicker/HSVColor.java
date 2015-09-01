package com.example.sabrina.hsvcolorpicker;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sabrina on 4/15/2015.
 */
public class HSVColor {

    private static float hueDelta = 10f;
    private static float hueCenter = 120f;
    private static float saturationDelta = 10f;
    private static float valueDelta = 10f;


    private static float exactHue;
    private static float leftHue = 0.0f;
    private static float rightHue = 0.0f;
    private static float saturation = 1.0f;
    private static float value = 1.0f;


    private float[] leftHsv = {     leftHue,        // float: [0.0f, 360.0f]
                                    saturation,     // float: [0.0f, 1.0f]
                                    value           // float: [0.0f, 1.0f]
    };

    private float[] rightHsv = {    rightHue,        // float: [0.0f, 360.0f]
                                    saturation,     // float: [0.0f, 1.0f]
                                    value           // float: [0.0f, 1.0f]
    };

    private HSVColor(float aHueLeft, float aHueRight, float aSaturation, float aValue) {
        leftHue = aHueLeft;
        rightHue = aHueRight;
        saturation = aSaturation;
        value = aValue;
        leftHsv = new float[]{leftHue, saturation, value};
        rightHsv = new float[]{rightHue, saturation, value};
    }

    static public List<GradientDrawable> populateHueList() {
        ArrayList<GradientDrawable> list = new ArrayList<>( );
        float delta = (360f/hueDelta)/2f;

        for (int i = 0; i < hueDelta; ++i) {
            //TODO check this math
            HSVColor hsvColor = new HSVColor(((hueCenter + i * (360 / hueDelta) - delta) % 360 + 360) % 360, (hueCenter + i * (360 / hueDelta) + delta) % 360, 1f, 1f);

            int[] values = gradientHelper(hsvColor, 1f, 1f);

            list.add(new GradientDrawable( GradientDrawable.Orientation.LEFT_RIGHT, values ));
        }
        return list;
    }

    static public List<GradientDrawable> populateSaturationList(float aHueLeft, float aHueRight) {
        ArrayList<GradientDrawable> list = new ArrayList<>( );

        if (saturationDelta <= 1){
            HSVColor hsvColor = new HSVColor(aHueLeft, aHueRight, 1f, 1f);

            int[] values = gradientHelper(hsvColor, 1f, 1f);

            list.add(new GradientDrawable( GradientDrawable.Orientation.LEFT_RIGHT, values ));
        }else {
            --saturationDelta;
            float dif = 1 / saturationDelta;
            for (int i = 0; i < saturationDelta + 1; ++i) {
                float inputSaturation = i == saturationDelta ? 0f : 1f - dif * i;
                HSVColor hsvColor = new HSVColor(aHueLeft, aHueRight, inputSaturation, 1f);

                int[] values = gradientHelper(hsvColor, inputSaturation, 1f);

                list.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, values));
            }
        }
        return list;
    }

    static public List<GradientDrawable> populateValueList(float aHueLeft, float aHueRight, float aSaturation) {
        ArrayList<GradientDrawable> list = new ArrayList<>( );

        if (valueDelta <= 1){
            HSVColor hsvColor = new HSVColor(aHueLeft, aHueRight, aSaturation, 1f);

            int[] values = gradientHelper(hsvColor, aSaturation, 1f);

            list.add(new GradientDrawable( GradientDrawable.Orientation.LEFT_RIGHT, values ));
        }
        else {
            --valueDelta;
            float dif = 1 / valueDelta;
            for (int i = 0; i < valueDelta + 1; ++i) {
                float inputValue = i == valueDelta ? 0f : 1f - dif * i;
                HSVColor hsvColor = new HSVColor(aHueLeft, aHueRight, aSaturation, inputValue);

                int[] values = gradientHelper(hsvColor, aSaturation, inputValue);

                list.add(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, values));
            }
        }

        return list;
    }

    private static int[] gradientHelper(HSVColor hsvColor, float saturation, float value ){

        int colorLeft = Color.HSVToColor(hsvColor.leftHsv);
        int colorRight = Color.HSVToColor(hsvColor.rightHsv);

        float diffBetween = hsvColor.leftHsv[0] == hsvColor.rightHsv[0] ? 360: (360 - hsvColor.leftHsv[0] + hsvColor.rightHsv[0]) % 360;

        int degreeDiff = 5;
        int times = (int) (diffBetween / degreeDiff);
        int[] values = new int[times+1];
        values[0] = colorLeft;

        for (int j = 0, k = 1; j < times - 1 && k < times+1 ; ++j, ++k) {
            values[k] = Color.HSVToColor(new float[]{(hsvColor.leftHsv[0] + degreeDiff * k)%360, saturation, value});
        }

        values[times] = colorRight;
        return values;
    }

    public static void setHueLeftByPosition(int position) {
        float delta = (360f/hueDelta)/2f;
        leftHue = ((hueCenter + position*(360/hueDelta) - delta)%360+360)%360;

    }

    public static void setHueRightByPosition(int position) {
        float delta = (360f/hueDelta)/2f;
        rightHue = (hueCenter + position*(360/hueDelta) + delta)%360;
    }

    public static void setSaturationByPosition(int position) {
        if(saturationDelta <= 0){
            saturation = 1f;
        }else {
            float dif = 1 / saturationDelta;
            saturation = position == saturationDelta ? 0f : 1f - dif * position;
        }
    }

    public static void setValueByPosition(int position){
        if(valueDelta <= 0){
            valueDelta = 1f;
        }else {
            float dif = 1 / valueDelta;
            value = position == valueDelta ? 0f : 1f - dif * position;
        }
    }

    public static float getHueDelta() {
        return hueDelta;
    }

    public static void setHueDelta(float hueDelta) {
        HSVColor.hueDelta = hueDelta;
    }

    public static float getHueCenter() {
        return hueCenter;
    }

    public static void setHueCenter(float hueCenter) {
        HSVColor.hueCenter = hueCenter;
    }

    public static float getSaturationDelta() {
        return saturationDelta;
    }

    public static void setSaturationDelta(float saturationDelta) {
        HSVColor.saturationDelta = saturationDelta;
    }

    public static float getValueDelta() {
        return valueDelta;
    }

    public static void setValueDelta(float valueDelta) {
        HSVColor.valueDelta = valueDelta;
    }

    public static void setHueLeft(float settingHue){
        leftHue = settingHue;
    }

    public static float getHueLeft(){
        return leftHue;
    }

    public static void setHueRight(float settingHue){
        rightHue = settingHue;
    }

    public static float getHueRight(){
        return rightHue;
    }

    public static void setSaturation (float settingSaturation){
        saturation = settingSaturation;
    }

    public static float getSaturation(){
        return saturation;
    }

    public static void setValue (float settingValue){
        value = settingValue;
    }

    public static float getValue(){
        return value;
    }

    public static float getExactHue() {
        return exactHue;
    }

    public static void setExactHue(float exactHue) {
        HSVColor.exactHue = exactHue;
    }
}
