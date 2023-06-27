package guru.qa.niffler;

import guru.qa.niffler.test.web.RegistrationWebTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor {

    static ExecutorService executorService = Executors.newFixedThreadPool(6);

    public static void main(String[] args) {

        executorService.execute(() -> {
            RegistrationWebTest testObject = new RegistrationWebTest();
            testObject.errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent();
        });
        executorService.execute(() -> {
            RegistrationWebTest testObject = new RegistrationWebTest();
            testObject.errorMessageShouldBeVisibleInCaseThatUsernameLessThan3Symbols();
        });
        executorService.execute(() -> {
            RegistrationWebTest testObject = new RegistrationWebTest();
            testObject.errorMessageShouldBeVisibleInCaseThatPasswordsLessThan3Symbols();
        });
        executorService.execute(() -> {
            RegistrationWebTest testObject = new RegistrationWebTest();
            testObject.errorMessageShouldBeVisibleInCaseThatUsernameNotUniq();
        });
        executorService.shutdown();
    }
}
