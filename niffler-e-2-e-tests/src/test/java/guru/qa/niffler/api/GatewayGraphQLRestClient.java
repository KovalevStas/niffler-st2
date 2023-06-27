package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.context.SessionContext;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GatewayGraphQLRestClient extends BaseRestClient {

    public GatewayGraphQLRestClient() {
        super(CFG.getGatewayUrl());
    }

    private final GatewayGraphQLService gatewayService = retrofit.create(GatewayGraphQLService.class);

    public @Nonnull JsonNode getAllCurrencies(JsonNode request) {
        try {
            return gatewayService.graphql(
                    "Bearer " + SessionContext.getInstance().getToken(),
                    request
            ).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
