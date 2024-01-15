package BD.request;

import BD.interfaces.Reponse;

import javax.crypto.SecretKey;

public class requestSecureResponse implements Reponse {

    private SecretKey cleSession;

    public requestSecureResponse(SecretKey cleSession) {
        this.cleSession = cleSession;
    }

    public SecretKey getCleSession() {
        return cleSession;
    }

    public void setCleSession(SecretKey cleSession) {
        this.cleSession = cleSession;
    }
}