package guru.qa.niffler.test.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GqlRequest;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class GetAllCurrenciesGQLTest extends BaseGQLTest {

    @AllureId("400001")
    @Test
    @GenerateUser(categories = {
            @Category("Обучение"),
            @Category("Рыбалка"),
    })
    void getAllCurrenciesTest(UserJson user, @GqlRequest("gql/getAllCurrenciesQuery.json") JsonNode request) {
        apiLogin(user.getUsername(), user.getPassword());
        JsonNode allCategories = gatewayClient.getAllCurrencies(request);

        JsonNode jsonNode = allCategories.get("data").get("currencies");
        for (JsonNode node : jsonNode) {
            System.out.println(node);
        }
    }
}
