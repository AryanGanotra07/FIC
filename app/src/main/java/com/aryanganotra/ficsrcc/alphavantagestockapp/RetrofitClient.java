package com.aryanganotra.ficsrcc.alphavantagestockapp;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by johnla on 6/16/18.
 */

public class RetrofitClient {
    private static Retrofit retrofitDaily = null;

    public interface RetrofitListener {
        public void onData(List<Entry> entries);
    }

    public static Retrofit getDailyClient(String baseUrl) {
        if (retrofitDaily==null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(StockDailyInfo.class, new StockDailyParser());
            retrofitDaily = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();
        }
        return retrofitDaily;
    }
}
