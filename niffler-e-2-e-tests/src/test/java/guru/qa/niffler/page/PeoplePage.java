package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage {
    public static final String URL = Config.getConfig().getAuthUrl() + "/people";
    private final Header header = new Header();
    private final SelenideElement peopleTable = $(".abstract-table");

    public Header getHeader() {
        return header;
    }

    @Override
    public PeoplePage checkThatPageLoaded() {
        $(".people-content").shouldBe(Condition.visible, Duration.ofSeconds(3));
        return this;
    }

    public PeoplePage addUserToFriends(String username) {
        peopleTable.$$("tr").find(Condition.exactText(username)).$("div[data-tooltip-id='add-friend']").click();
        return this;
    }

    public PeoplePage removeFriend(String username) {
        peopleTable.$$("tr").find(Condition.exactText(username)).$(".button-icon_type_close").click();
        return this;
    }

}
