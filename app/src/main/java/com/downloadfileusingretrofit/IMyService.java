package com.downloadfileusingretrofit;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Streaming;
import retrofit.http.Url;

/**
 * Created by surya on 28/3/18.
 */

public interface IMyService {


    @GET
    @Streaming
    Call<ResponseBody> getFile(@Url String url);
}
