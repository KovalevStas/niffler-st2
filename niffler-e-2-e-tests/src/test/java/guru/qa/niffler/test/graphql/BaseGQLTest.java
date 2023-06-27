package guru.qa.niffler.test.graphql;

import guru.qa.niffler.api.AuthClient;
import guru.qa.niffler.api.AuthRestClient;
import guru.qa.niffler.api.GatewayGraphQLRestClient;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.jupiter.annotation.GqlTest;

@GqlTest
public abstract class BaseGQLTest {

    protected static final GatewayGraphQLRestClient gatewayClient = new GatewayGraphQLRestClient();

    private static final AuthClient authClient = new AuthRestClient();

    protected String apiLogin(String username, String password) {
        final SessionContext sessionContext = SessionContext.getInstance();
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);

        sessionContext.setCodeVerifier(codeVerifier);
        sessionContext.setCodeChallenge(codeChallenge);

        authClient.authorizePreRequest();
        authClient.login(username, password);
        return authClient.getToken();
    }

}
