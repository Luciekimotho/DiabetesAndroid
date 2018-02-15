package com.ellekay.lucie.diabetes.auth;

import com.ellekay.lucie.diabetes.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.POST;

/**
 * Created by lucie on 2/12/2018.
 */

public interface LoginService {
    @POST("api-token-auth/")
    Call<User> basicLogin();
}
