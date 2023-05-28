package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage {
    public static final String URL = Config.getConfig().getAuthUrl() + "/profile";

    private final Header header = new Header();
    private SelenideElement categoryInput = $("input[name=category]");
    private SelenideElement firstnameInput = $("input[name=firstname]");
    private SelenideElement surnameInput = $("input[name=surname]");
    private SelenideElement curencyValue = $("[class$=ValueContainer]");
    private SelenideElement curencyArrow = $("[class$=indicatorContainer]");
    private SelenideElement curencyMenuList = $("[class$=MenuList]");
    private SelenideElement createBtn = $(".add-category__input-container button");
    private SelenideElement submitBtn = $("button[type=submit]");
    private SelenideElement categoriesList = $(".categories__list");

    public Header getHeader() {
        return header;
    }

    @Override
    public ProfilePage checkThatPageLoaded() {
        $(".profile__avatar").shouldHave(Condition.visible, Duration.ofSeconds(3));
        return this;
    }

    public ProfilePage setCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    public ProfilePage setFirstname(String firstname) {
        firstnameInput.setValue(firstname);
        return this;
    }

    public ProfilePage setSurname(String surname) {
        surnameInput.setValue(surname);
        return this;
    }

    public ProfilePage clickCreateBtn() {
        createBtn.click();
        return this;
    }

    public ProfilePage clickSubmitBtn() {
        submitBtn.click();
        return this;
    }

    public ProfilePage setCurencyValue(String curency) {
        curencyArrow.click();
        curencyMenuList.shouldBe(Condition.visible).$(byText(curency)).click();
        curencyValue.shouldHave(exactText(curency), Duration.ofSeconds(3));
        return this;
    }

    public List<String> getCreatedCategories() {
        return categoriesList.$$(".categories__item").texts();
    }
}
