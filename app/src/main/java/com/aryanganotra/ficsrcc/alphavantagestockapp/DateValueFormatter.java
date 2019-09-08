package com.aryanganotra.ficsrcc.alphavantagestockapp;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by johnla on 6/16/18.
 */
public class DateValueFormatter extends IndexAxisValueFormatter {

    @Override
    public String getFormattedValue(float value) {

        Date parsedDate = new Date(Float.valueOf(value).longValue());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
        return formatter.format(parsedDate);
    }
}