package niffler.api;

import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserDataSrvice {

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") String username);
}
