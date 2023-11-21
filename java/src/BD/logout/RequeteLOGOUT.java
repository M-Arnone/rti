package BD.logout;

import BD.interfaces.Requete;

public class RequeteLOGOUT implements Requete {
    private String login;
    public RequeteLOGOUT(String l) {
        login = l;
    }
    public String getLogin() {
        return login;
    }
}
