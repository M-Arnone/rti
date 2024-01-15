package BD.login;

import BD.interfaces.Requete;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

public class RequeteLOGINSecure implements Requete {

    private String _login;
    private long _temps;
    private double _alea;
    private byte[] _digest;

    public RequeteLOGINSecure(String l,String p) throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        _login = l;
        _temps = new Date().getTime();
        _alea = Math.random();
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(_login.getBytes());
        md.update(p.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(_temps);
        dos.writeDouble(_alea);
        md.update(baos.toByteArray());
        _digest = md.digest();

    }

    public String getLogin() {
        return _login;
    }

    public boolean VerifyPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        // Construction du digest local
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(_login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(_temps);
        dos.writeDouble(_alea);
        md.update(baos.toByteArray());

        byte[] digestLocal = md.digest();
        // Comparaison digest reçu et digest local
        return MessageDigest.isEqual(_digest,digestLocal);
    }
}
