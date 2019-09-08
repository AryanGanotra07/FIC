package com.aryanganotra.ficsrcc.Instagram;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InstagramService {

    @GET("/v1/users/self/media/recent/")
   Call< InstagramModel> getInstaData(@Query("access_token")String access_token);
}
