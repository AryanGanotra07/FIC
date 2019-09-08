package com.aryanganotra.ficsrcc.alphavantagestockapp;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by johnla on 6/16/18.
 */

public class StockData {
    private Date stockDate;
    private HashMap<StockValue, Float> stockDataValues;

    public enum StockValue {
        OPEN("1. open"),
        HIGH("2. high"),
        LOW("3. low"),
        CLOSE("4. close"),
        VOLUME("5. volume");

        private String value;

        StockValue(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public StockData(Date stockDate, HashMap<StockValue, Float> stockDataValues) {
        this.stockDate = stockDate;
        this.stockDataValues = stockDataValues;
    }

    public Date getDate() {
        return stockDate;
    }

    public Float getStockValue(StockValue desiredValue){
        return stockDataValues.get(desiredValue);
    }

}
