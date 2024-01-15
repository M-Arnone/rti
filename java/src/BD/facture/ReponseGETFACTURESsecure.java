package BD.facture;

import BD.classes.Facture;
import BD.interfaces.Reponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import static Serveur.ProtocoleVESPAP.Mycrypto.CryptSymDES;

public class ReponseGETFACTURESsecure implements Reponse {
    private SecretKey cleSession;

    private byte[] factureBytesCrypte ;

    public ReponseGETFACTURESsecure(ArrayList<Facture> factures, SecretKey cleSession) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        this.cleSession = cleSession;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(factures);
        objectOutputStream.close();
        byte[] facturesBytes = byteArrayOutputStream.toByteArray();
        System.out.println("1");
        factureBytesCrypte = CryptSymDES(cleSession, facturesBytes);
        System.out.println("22");
    }

    public byte[] getFactureBytesCrypte() {
        return factureBytesCrypte;
    }
}
