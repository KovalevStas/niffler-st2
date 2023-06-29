package guru.qa.niffler.soap;

import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserdataSoapService {

    @Headers({
            "Content-type: text/xml"
    })
    @POST("/ws")
    Call<CurrentUserResponse> currentUser(@Body CurrentUserRequest currentUserRequest);
}
