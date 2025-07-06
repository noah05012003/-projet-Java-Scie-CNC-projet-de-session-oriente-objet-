package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.Outil;

public class OutilFactory {

    public Outil createOutil(String nom, int largeur , int position)
    {
        return new Outil(nom, largeur, position);
    }
}
