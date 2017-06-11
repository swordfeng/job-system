package xyz.swordfeng.jobsystem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UserTests {
    @Before
    public void initialize() {
        DB.getInstance().reset();
        try {
            User.register("user1", "password", 0);
        } catch (User.UserExist | User.MoneyBelowZero e) {
            e.printStackTrace();
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
    }
    @Test(expected = User.MoneyBelowZero.class)
    public void negativeMoney() throws User.UserExist, User.MoneyBelowZero {
        User.register("user2", "password2", -1);
    }
    @Test
    public void get() {
        assertTrue("get user", User.get("user1") != null);
        assertTrue("get non-exist user", User.get("user111") == null);
    }
    @Test
    public void login() throws User.AuthFailure {
        User user = User.login("user1", "password");
        assertTrue("login", "user1".equals(user.username));
    }
    @Test(expected = User.AuthFailure.class)
    public void loginWrongPassword() throws User.AuthFailure {
        User user = User.login("user1", "xyz");
    }
    @Test(expected = User.AuthFailure.class)
    public void loginNonexistUser() throws User.AuthFailure {
        User user = User.login("user2", "password2");
    }

    @Test
    public void registerWithMoney0() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 0);
        assertTrue("Create a vip0 user", user.vipLevel == 0);
    }
    @Test
    public void registerWithMoney99() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 99);
        assertTrue("Create a vip0 user", user.vipLevel == 0);
    }
    @Test
    public void registerWithMoney100() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 100);
        assertTrue("Create a vip0 user", user.vipLevel == 1);
    }
    @Test
    public void registerWithMoney9999() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 9999);
        assertTrue("Create a vip0 user", user.vipLevel == 1);
    }
    @Test
    public void registerWithMoney10000() throws User.UserExist, User.MoneyBelowZero {
        User user = User.register("user2", "password2", 10000);
        assertTrue("Create a vip0 user", user.vipLevel == 2);
    }
}
