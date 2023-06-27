package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BaseComponent;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    private final SelenideElement mainPageBtn = $("a[href*='main']");
    private final SelenideElement friendsPageBtn = $("a[href*='friends']");
    private final SelenideElement peoplePageBtn = $("a[href*='people']");
    private final SelenideElement logoutBtn = $(".button-icon_type_logout");
    private final SelenideElement profilePageBtn = $("a[href*='profile']");

    public Header() {
        super($(".header"));
    }

    @Override
    public Header checkThatComponentDisplayed() {
        self.$(".header__title").shouldHave(text("Niffler. The coin keeper."));
        return this;
    }

    public FriendsPage goToFriendsPage() {
        friendsPageBtn.click();
        return new FriendsPage();
    }

    public MainPage goToMainPage() {
        mainPageBtn.click();
        return new MainPage();
    }

    public MainPage goToPeoplePage() {
        peoplePageBtn.click();
        return new MainPage();
    }

    public MainPage goToProfilePage() {
        profilePageBtn.click();
        return new MainPage();
    }

    public MainPage logout() {
        logoutBtn.click();
        return new MainPage();
    }
}
