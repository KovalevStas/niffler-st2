package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage {

    public static final String URL = Config.getConfig().getAuthUrl() + "/friends";
    private final Header header = new Header();
    private final SelenideElement peopleTable = $(".abstract-table");

    public Header getHeader() {
        return header;
    }

    @Override
    public FriendsPage checkThatPageLoaded() {
        $(".people-content").shouldBe(Condition.visible, Duration.ofSeconds(3));
        return this;
    }

    public FriendsPage removeFriend(String username) {
        peopleTable.$$("tr").find(Condition.exactText(username)).$("div[data-tooltip-id='remove-friend']").click();
        return this;
    }

    public FriendsPage submitInvitation(String username) {
        peopleTable.$$("tr").find(Condition.exactText(username)).$("div[data-tooltip-id='submit-invitation'] button").click();
        return this;
    }

    public FriendsPage declineInvitation(String username) {
        peopleTable.$$("tr").find(Condition.exactText(username)).$("div[data-tooltip-id='decline-invitation'] button").click();
        return this;
    }
}
