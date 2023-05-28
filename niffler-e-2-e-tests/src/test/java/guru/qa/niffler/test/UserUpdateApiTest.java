package guru.qa.niffler.test;

import guru.qa.niffler.api.UserDataSrvice;
import guru.qa.niffler.jupiter.annotation.ClasspathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class UserUpdateApiTest {

    @ValueSource(strings = {
            "testdata/updateUser.json"
    })
    @AllureId("105")
    @ParameterizedTest
    void loginTest(@ClasspathUser UserJson user) throws IOException {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://127.0.0.1:8089")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        UserDataSrvice userDataSrvice = retrofit.create(UserDataSrvice.class);
        UserJson originalUser = userDataSrvice.currentUser(user.getUsername()).execute()
                .body();
        UserJson changedUser = userDataSrvice.updateUserInfo(user).execute()
                .body();
        if (originalUser != null && changedUser != null) {
            Assertions.assertNotEquals(originalUser.getSurname(), changedUser.getSurname());
            Assertions.assertNotEquals(originalUser.getCurrency(), changedUser.getCurrency());
        }
    }
}
