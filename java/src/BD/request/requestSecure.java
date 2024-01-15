package BD.request;

import BD.interfaces.Requete;

public class requestSecure implements Requete {
    private byte[] data1;  // clé de session cryptée asymétriquement
    private byte[] data2;  // message crypté symétriquement

    public void setData1(byte[] d) { data1 = d; }
    public void setData2(byte[] d) { data2 = d; }
    public byte[] getData1() { return data1; }
    public byte[] getData2() { return data2; }
}