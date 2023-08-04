package com.example.myapplication.utils.retrofit;

import com.example.myapplication.dataclass.UserData;
import com.example.myapplication.utils.request.DepartmentData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCall {
    @GET("posts")
    Call<List<UserData>> getAllData();
    @GET("lookup?type=department")
    Call<List<DepartmentData>> getDepartmentData();
}
