package ClientPayement.model;

public class User {
    private String login;
    private String pwd;
    private boolean isConnected;

    public User(String login, String pwd) {
        this.login = login;
        this.pwd = pwd;
        isConnected = true;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
