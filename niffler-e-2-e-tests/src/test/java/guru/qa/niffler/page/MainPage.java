package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage {
    public static final String URL = Config.getConfig().getAuthUrl() + "/people";
    private final Header header = new Header();
    private SelenideElement amountInput = $("input[name=amount]");
    private SelenideElement spendDataInput = $(".react-datepicker__input-container input");
    private SelenideElement descriptionInput = $("input[name=description]");
    private SelenideElement categoryValue = $("[class$=ValueContainer]");
    private SelenideElement categoryArrow = $("[class$=indicatorContainer]");
    private SelenideElement categoryMenuList = $("[class$=MenuList]");
    private SelenideElement addNewSpendingBtn = $("button[type=submit]");
    private SelenideElement spendingList = $(".spendings-table");

    public Header getHeader() {
        return header;
    }

    @Override
    public MainPage checkThatPageLoaded() {
        $(".main-content__section-history").shouldBe(Condition.visible, Duration.ofSeconds(3));
        return this;
    }

    public MainPage setDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    public MainPage setSpendData(String spendData) {
        spendDataInput.setValue(spendData);
        return this;
    }

    public MainPage setAmount(String amount) {
        amountInput.setValue(amount);
        return this;
    }

    public MainPage clickAddNewSpendingBtn() {
        addNewSpendingBtn.click();
        return this;
    }

    public MainPage setCategoryValue(String category) {
        categoryArrow.click();
        categoryMenuList.shouldBe(Condition.visible).$(byText(category)).click();
        categoryValue.shouldHave(exactText(category), Duration.ofSeconds(3));
        return this;
    }
}
