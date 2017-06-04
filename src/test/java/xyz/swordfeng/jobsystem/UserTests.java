package xyz.swordfeng.jobsystem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserTests {
    @Before
    public void initialize() {
        DB.getInstance().reset();
        try {
            User.register("user1", "password", 0);
        } catch (User.UserExist userExist) {
            userExist.printStackTrace();
        } catch (User.MoneyBelowZero moneyBelowZero) {
            moneyBelowZero.printStackTrace();
        }
    }
    @Test
    public void createUser() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 0);
        assertTrue("Create a user", "user2".equals(user.username));
    }
    @Test(expected = User.UserExist.class)
    public void createExistingUser() throws User.UserExist, User.MoneyBelowZero {
        User.register("user1", "xyz", 0);
        fail("should throw UserExist exception");
    }
    @Test
    public void login() throws User.AuthFailure {
        User user = User.login("user1", "password");
        assertTrue("login", "user1".equals(user.username));
    }
    @Test(expected = User.AuthFailure.class)
    public void loginWrongPassword() throws User.AuthFailure {
        User user = User.login("user1", "xyz");
        fail("should throw AuthFailure exception");
    }
}