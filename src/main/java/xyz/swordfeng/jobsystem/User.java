package xyz.swordfeng.jobsystem;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Created by swordfeng on 17-5-29.
 */
public final class User implements Serializable {

    public static class AuthFailure extends Exception {
        public AuthFailure() {
            super("User authentication failed");
        }
    }
    public static class UserExist extends Exception {
        public UserExist() {
            super("User has already existed");
        }
    }
    public static class MoneyBelowZero extends Exception {
        public MoneyBelowZero() {
            super("Input money is lower than zero");
        }
    }
    public static User get(@NotNull String username) {
        return DB.getInstance().get(username, User.class);
    }
    public static @NotNull User login(@NotNull String username, @NotNull String password) throws AuthFailure {
        User user = DB.getInstance().get(username, User.class);
        if (user == null || !password.equals(user.password)) {
            throw new AuthFailure();
        }
        return user;
    }
    public static @NotNull User register(@NotNull String username, @NotNull String password, int money) throws UserExist, MoneyBelowZero {
        User user = new User(username, password, money);
        synchronized (User.class) {
            if (DB.getInstance().get(username, User.class) == null) {
                DB.getInstance().put(username, user);
            } else {
                throw new UserExist();
            }
        }
        return user;
    }

    private User(@NotNull String username, @NotNull String password, int money) throws MoneyBelowZero {
        this.username = username;
        this.password = password;
        if (money < 0) {
            throw new MoneyBelowZero();
        }
        else if (money < 100) {
            vipLevel = 0;
        }
        else if (money < 10000) {
            vipLevel = 1;
        }
        else {
            vipLevel = 2;
        }
    }

    public final String username;
    private String password;
    public final int vipLevel;
}
