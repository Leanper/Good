package com.yongche.merchant.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhiyuan on 17/1/11.
 */

public interface ProjectAPI {
    //http://www.baidu.com/aaa?key=123&qq=aaa

    //返回值类型是被观察者
    @GET
    Observable<String> getMethod(@Url String url);

    @FormUrlEncoded
    @POST
    Observable<String> postMethod(@Url String url, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    Observable<String> postMethod(@Url String url, @FieldMap Map<String, String> map, @Field("carjb[]") String[] carjb, @Field("card[]") String[] card, @Field("carpaifang[]") String[] carpaifang, @Field("recommend[]") String[] recommend,
                                  @Field("create_addr[]") String[] create_addr, @Field("oil_standard[]") String[] oil_standard, @Field("enctype[]") String[] enctype, @Field("carsize[]") String[] carsize, @Field("car_color[]") String[] car_color, @Field("interiorConf[]") String[] interiorConf);

    @FormUrlEncoded
    @POST
    Observable<String> postTouchMethod(@Url String url, @Field("flag") boolean isTouch, @Field("userId") String userId);

    //上传多张图片
    @Multipart
    @POST
    Observable<ResponseBody> upLoadsImgs(@Url String url, @Part("accessToken") RequestBody accessToken, @Part("userId") RequestBody userId, @Part("deviceId") RequestBody deviceId, @Part("os") RequestBody os, @Part("id") RequestBody id, @Part MultipartBody.Part photo1, @Part MultipartBody.Part photo2, @Part MultipartBody.Part photo3, @Part MultipartBody.Part photo4, @Part MultipartBody.Part photo5, @Part MultipartBody.Part photo6, @Part MultipartBody.Part photo7, @Part MultipartBody.Part photo8, @Part MultipartBody.Part photo9, @Part MultipartBody.Part photo10, @Part MultipartBody.Part photo11);

    // 带参数上传单张图片
    @Multipart
    @POST
    Observable<ResponseBody> upLoadImg(@Url String url, @Part MultipartBody.Part photo, @Part("accessToken") RequestBody accessToken, @Part("deviceId") RequestBody deviceId, @Part("os") RequestBody os, @Part("userId") RequestBody userId, @PartMap Map<String, RequestBody> params);

    // 上传单张图片
    @Multipart
    @POST
    Observable<ResponseBody> upLoadImg(@Url String url, @Part MultipartBody.Part photo, @Part("accessToken") RequestBody accessToken, @Part("deviceId") RequestBody deviceId, @Part("os") RequestBody os, @Part("userId") RequestBody userId);

    //刷新token
    @FormUrlEncoded
    @POST
    Call<String> refrshToken(@Url String url, @FieldMap Map<String, String> map);
}
