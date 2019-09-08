package com.aryanganotra.ficsrcc.alphavantagestockapp;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by johnla on 6/16/18.
 */


public class StockDailyParser implements JsonDeserializer<StockDailyInfo> {

    private static final String TAG = StockDailyParser.class.getSimpleName();

    @Override
    public StockDailyInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        StockDailyInfo result = new StockDailyInfo();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        JsonObject jsonObject = json.getAsJsonObject();
        ArrayList<StockData> stockDayData = new ArrayList<>();
        for(String date : jsonObject.keySet()) {
            StockData stockDay;
            try {
                JsonObject stockValues = jsonObject.getAsJsonObject(date);
                stockDay = new StockData(formatter.parse(date), fillOutStockData(stockValues));
                result.addDay(stockDay);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Parsing error has occured");
            }
        }
        return result;
    }

    private HashMap<StockData.StockValue, Float> fillOutStockData(JsonObject stockValues) {
        HashMap<StockData.StockValue, Float> data = new HashMap<>();
        data.put(StockData.StockValue.OPEN, stockValues.get(StockData.StockValue.OPEN.getValue()).getAsFloat());
        data.put(StockData.StockValue.HIGH, stockValues.get(StockData.StockValue.HIGH.getValue()).getAsFloat());
        data.put(StockData.StockValue.LOW, stockValues.get(StockData.StockValue.LOW.getValue()).getAsFloat());
        data.put(StockData.StockValue.CLOSE, stockValues.get(StockData.StockValue.CLOSE.getValue()).getAsFloat());
        data.put(StockData.StockValue.VOLUME, stockValues.get(StockData.StockValue.VOLUME.getValue()).getAsFloat());
        return data;
    }
}

