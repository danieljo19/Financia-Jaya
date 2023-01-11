package com.jaya.financia.API;

import com.jaya.financia.Model.ResponseAnalyticExpenses;
import com.jaya.financia.Model.ResponseAnalyticIncomes;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.ResponseUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequestData {
    @FormUrlEncoded
    @POST("retrieve.php")
    Call<ResponseModel> ardRetrieveData(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("total.php")
    Call<ResponseModel> ardRetrieveTotal(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("type") String type,
            @Field("category") String category,
            @Field("note") String note,
            @Field("amount") String amount,
            @Field("date") String date,
            @Field("user_uid") String user_uid
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("type") String type,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("read.php")
    Call<ResponseModel> ardGetData(
            @Field("type") String type,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("type") String type,
            @Field("category") String category,
            @Field("note") String note,
            @Field("amount") String amount,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("filter.php")
    Call<ResponseModel> ardDataFilter(
            @Field("type") String type,
            @Field("user_uid") String user_uid
    );

    @FormUrlEncoded
    @POST("filter_date_asc.php")
    Call<ResponseModel> ardDataFilterDateAsc(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("filter_date_desc.php")
    Call<ResponseModel> ardDataFilterDateDesc(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("read_user.php")
    Call<ResponseUser> ardGetUser(
            @Field("user_uid") String user_uid
    );

    @FormUrlEncoded
    @POST("create_user.php")
    Call<ResponseUser> ardCreateUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("user_uid") String user_uid
            );

    @FormUrlEncoded
    @POST("update_user.php")
    Call<ResponseUser> ardUpdateUser(
            @Field("user_uid") String user_uid,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("update_image.php")
    Call<ResponseUser> ardUpdateImage(
            @Field("id") int id,
            @Field("imageUrl") String imageUrl
    );

    @FormUrlEncoded
    @POST("analytic_incomes_yearly.php")
    Call<ResponseAnalyticIncomes> ardGetAnalyticIncomesYearly(@Field("user_uid") String user_uid);

    @FormUrlEncoded
    @POST("analytic_expenses_monthly.php")
    Call<ResponseAnalyticExpenses> ardGetAnalyticExpensesMonthly(@Field("user_uid") String user_uid);
}
