package guru.qa.niffler.soap;

import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;

import java.io.IOException;

public class UserdataSoapClient extends BaseSoapClient {

    public UserdataSoapClient() {
        super(CFG.getUserdataUrl());
    }

    private final UserdataSoapService userdataService = retrofit.create(UserdataSoapService.class);

    public CurrentUserResponse currentUser(CurrentUserRequest currentUserRequest) {
        try {
            return userdataService.currentUser(currentUserRequest).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
