package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.CNC.CoupesManageur;
import LogicielCNC.src.Domaine.CNC.OutilsManageur;
import LogicielCNC.src.Domaine.CNC.PanneauManager;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Outil;
import LogicielCNC.src.Domaine.OutilReadOnly;
import LogicielCNC.src.Domaine.Panneau;
import LogicielCNC.src.Domaine.PanneauReadOnly;

public class PanneauFactory {
    private final OutilsManageur outilsManageur;
    private final CoupesManageur coupeManageur;

    public PanneauFactory(OutilsManageur outilsManageur, CoupesManageur coupeManageur) {
        this.outilsManageur = outilsManageur;
        this.coupeManageur = coupeManageur;
    }

    public Panneau createPanneau(int longeur, int largeur, int epaisseur)
    {
        return new Panneau(longeur, largeur, epaisseur);
    }

    public Panneau createPanneauCopy(PanneauReadOnly loadedPanneau) {
        return new Panneau(loadedPanneau.getLargeur(), loadedPanneau.getLongueur(), loadedPanneau.getEpaisseur());
    }
}
