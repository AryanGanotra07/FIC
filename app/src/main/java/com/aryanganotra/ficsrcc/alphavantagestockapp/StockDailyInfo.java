package com.aryanganotra.ficsrcc.alphavantagestockapp;

import java.util.ArrayList;

/**
 * Created by johnla on 6/16/18.
 */

public class StockDailyInfo {
    private ArrayList<StockData> dailyInfoList;

    public StockDailyInfo() {
       dailyInfoList = new ArrayList<>();
    }

    public void addDay(StockData stockData) {
        dailyInfoList.add(stockData);
    }

    public ArrayList<StockData> getDailyInfoList() {
        return dailyInfoList;
    }
}
