package com.aryanganotra.ficsrcc.alphavantagestockapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johnla on 6/16/18.
 */


public class DailyResponse {
    @SerializedName("Time Series (Daily)")
    @Expose
    private StockDailyInfo dailyResults;

    public StockDailyInfo getDailyResults() {
        return dailyResults;
    }
}
