package com.aryanganotra.ficsrcc.alphavantagestockapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by johnla on 6/16/18.
 */

public interface AlphaVantageAPIService {

    @GET("/query?function=TIME_SERIES_DAILY&symbol=+"+"" +
            "")
    Call<DailyResponse> getStockData(@Query("symbol") String symbol,@Query("apikey") String apiKey);





}






