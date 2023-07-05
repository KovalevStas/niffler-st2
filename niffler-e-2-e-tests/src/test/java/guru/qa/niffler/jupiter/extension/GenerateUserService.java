package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthRestClient;
import guru.qa.niffler.api.SpendRestClient;
import guru.qa.niffler.api.UserdataRestClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

public class GenerateUserService {

    private static final AuthRestClient authClient = new AuthRestClient();
    private static final UserdataRestClient userdataClient = new UserdataRestClient();
    private static final SpendRestClient spendRestClient = new SpendRestClient();


    public UserJson generateUser(@Nonnull GenerateUser annotation) {
        UserJson user = createRandomUser();

        addFriendsIfPresent(user, annotation.friends());
        addOutcomeInvitationsIfPresent(user, annotation.outcomeInvitations());
        addIncomeInvitationsIfPresent(user, annotation.incomeInvitations());
        addCategoryIfPresent(user, annotation.categories());
        addSpendingIfPresent(user, annotation.spends());
        return user;
    }

    private void addSpendingIfPresent(UserJson targetUser, GenerateSpend[] spends) {
        if (isNotEmpty(spends)) {
            for (GenerateSpend spend : spends) {
                SpendJson spendJson = new SpendJson();
                spendJson.setUsername(targetUser.getUsername());
                spendJson.setAmount(spend.amount());
                spendJson.setDescription(spend.description());
                spendJson.setCategory("".equals(spend.category()) ? targetUser.getCategories().get(0).getCategory() : spend.category());
                spendJson.setSpendDate(new Date());
                spendJson.setCurrency(spend.currency());

                SpendJson created = spendRestClient.addSpend(spendJson);
                targetUser.getSpends().add(created);
            }
        }
    }

    private void addCategoryIfPresent(UserJson targetUser, Category[] categories) {
        if (isNotEmpty(categories)) {
            for (Category category : categories) {
                CategoryJson createdCategory = spendRestClient.createCategory(targetUser.getUsername(), category.value());
                targetUser.getCategories().add(createdCategory);
            }
        }
    }

    private void addFriendsIfPresent(UserJson targetUser, Friend[] friends) {
        if (isNotEmpty(friends)) {
            for (Friend friend : friends) {
                UserJson friendJson = createRandomUser();
                userdataClient.addFriend(targetUser.getUsername(), friendJson.getUsername());
                userdataClient.acceptInvitation(friendJson.getUsername(), targetUser.getUsername());
                targetUser.getFriends().add(friendJson);
            }
        }
    }

    private void addOutcomeInvitationsIfPresent(UserJson targetUser, Friend[] outcomeInvitations) {
        if (isNotEmpty(outcomeInvitations)) {
            for (Friend oi : outcomeInvitations) {
                UserJson friendJson = createRandomUser();
                userdataClient.addFriend(targetUser.getUsername(), friendJson.getUsername());
                targetUser.getOutcomeInvitations().add(friendJson);
            }
        }
    }

    private void addIncomeInvitationsIfPresent(UserJson targetUser, Friend[] incomeInvitations) {
        if (isNotEmpty(incomeInvitations)) {
            for (Friend ii : incomeInvitations) {
                UserJson friendJson = createRandomUser();
                userdataClient.addFriend(friendJson.getUsername(), targetUser.getUsername());
                targetUser.getIncomeInvitations().add(friendJson);
            }
        }
    }

    private UserJson createRandomUser() {
        final String username = DataUtils.generateRandomUsername();
        final String password = DataUtils.generateRandomPassword();
        authClient.register(username, password);
        UserJson user = waitWhileUserToBeConsumed(username, 5000L);
        user.setPassword(password);
        return user;
    }

    private UserJson waitWhileUserToBeConsumed(String username, long maxWaitTime) {
        Stopwatch sw = Stopwatch.createStarted();
        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            UserJson userJson = userdataClient.currentUser(username);
            if (userJson != null) {
                return userJson;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new IllegalStateException("Can`t obtain user from niffler-userdata");
    }
}
