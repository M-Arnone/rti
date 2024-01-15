package BD.login;

import BD.interfaces.Reponse;

public class ReponseLOGINSecure implements Reponse {
    private boolean success;

    public ReponseLOGINSecure(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
