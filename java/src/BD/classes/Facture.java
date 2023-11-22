package BD.classes;

import java.io.Serializable;
import java.util.Date;

public class Facture implements Serializable {
    private int id;
    private int idClient;
    private Date date;
    private float montant;
    private boolean paye;

    public Facture(int id,int idc, Date date, float montant, boolean paye) {
        this.id = id;
        this.idClient = idc;
        this.date = date;
        this.montant = montant;
        this.paye = paye;
    }

    public int getId() {
        return id;
    }
    public int getIdClient(){
        return idClient;
    }
    public boolean getPaye(){return paye;}
    public Date getDate() {
        return date;
    }

    public float getMontant() {
        return montant;
    }

    public boolean isPaye() {
        return paye;
    }
}
