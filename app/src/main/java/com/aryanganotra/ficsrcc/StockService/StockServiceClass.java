package com.aryanganotra.ficsrcc.StockService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockServiceClass {

    @GET("aq/autoc")
    public Call<StockResultModel> getStocks(@Query("query") String query, @Query("lang") String lang);

}