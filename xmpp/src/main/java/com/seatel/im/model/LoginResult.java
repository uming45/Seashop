package com.seatel.im.model;

/**
 * Created by eldorado on 17-5-3.
 */
public class LoginResult {
    String access_token;
    String userId;
    Login login;

    public class Login {
        public long loginTime;
        public String serial;

        public long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(long loginTime) {
            this.loginTime = loginTime;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "access_token='" + access_token + '\'' +
                ", userId='" + userId + '\'' +
                ", login=" + login +
                '}';
    }
}
