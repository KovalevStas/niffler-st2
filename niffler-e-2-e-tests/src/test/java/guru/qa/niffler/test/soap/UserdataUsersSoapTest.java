package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.soap.UserdataSoapClient;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserdataUsersSoapTest extends BaseSoapTest {

    private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();


    @AllureId("500001")
    @GenerateUser
    @Test
    void currentUserSoapEndpointShouldReturnCUserInfo(UserJson user) {
        final CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername(user.getUsername());

        final CurrentUserResponse response = userdataSoapClient.currentUser(cur);
        assertEquals(user.getUsername(), response.getUser().getUsername());
    }
}
