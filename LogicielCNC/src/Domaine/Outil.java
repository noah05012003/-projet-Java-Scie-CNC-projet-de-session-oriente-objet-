package LogicielCNC.src.Domaine;

public class Outil  implements  OutilReadOnly{
    private String nom;
    private int largeur;
    private  int position;

    public Outil(String nom, int largeur, int position) {
        this.nom = nom;
        this.largeur = largeur;
        this.position = position;
    }

    //Get
    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public int getLargeur() {
        return largeur;
    }
    @Override
    public int getPosition() {
        return position;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
