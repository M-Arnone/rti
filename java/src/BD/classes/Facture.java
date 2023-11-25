package BD.classes;

import java.io.Serializable;
import java.util.Date;

public class Facture implements Serializable {
    private int id;
    private int idClient;
    private Date date;
    private String montant;
    private boolean paye;

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    public Facture(int id, int idc, Date date, String montant, boolean paye) {
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

    public String getMontant() {
        return montant;
    }

    public boolean isPaye() {
        return paye;
    }
}
