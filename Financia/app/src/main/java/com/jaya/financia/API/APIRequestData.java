package com.jaya.financia.API;

import com.jaya.financia.Model.ResponseModel;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData {
    @FormUrlEncoded
    @POST("retrieve.php")
    Call<ResponseModel> ardRetrieveData(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("name") String name,
            @Field("type") String type,
            @Field("total") String total,
            @Field("date") String date,
            @Field("user_uid") String user_uid
    );

    @FormUrlEncoded
    @POST("filter.php")
    Call<ResponseModel> ardDataFilter(
            @Field("type") String type,
            @Field("user_uid") String user_uid
    );
    @FormUrlEncoded
    @POST("filter_date.php")
    Call<ResponseModel> ardDataFilterDate(@Field("user_uid") String user_uid);
}
