package ClientAchat.model;

public class Article {
    int id;
    String nom;
    Double prix;
    Integer quantite;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    String img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                ", img='" + img + '\'' +
                '}';
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Article(){
        setId(-1);
        setNom("nul");
        setPrix(null);
        setQuantite(-1);
        setImg("nul");
    }
    public Article(int id, String nom, Double prix,Integer quantite){
        setId(id);
        setNom(nom);
        setPrix(prix);
        setQuantite(quantite);
    }

    public Article(String nom, Double prix, Integer quantite,String image) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.img = image;
    }

    public Article(int id, String nom, Double prix, Integer quantite,String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.img = image;
    }
}
