package BD.facture;

import BD.interfaces.Requete;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static Serveur.ProtocoleVESPAP.Mycrypto.CryptSymDES;

public class RequetePAYFACTURESsecure implements Requete {
    private byte[] responseCrypt;



    public RequetePAYFACTURESsecure(int idFacture, String numCard, SecretKey cleSession) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(numCard);
        dos.writeInt(idFacture);

        byte[] dataToEncrypt = baos.toByteArray();

        responseCrypt = CryptSymDES(cleSession, dataToEncrypt);

    }

    public byte[] getResponseCrypt() {
        return responseCrypt;
    }
}
