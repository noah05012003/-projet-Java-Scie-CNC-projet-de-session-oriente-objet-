package LogicielCNC.src.Domaine;
import LogicielCNC.src.Domaine.CNC.CoupesManageur;
import LogicielCNC.src.Domaine.Coupes.Coupes;
import LogicielCNC.src.Domaine.CNC.CNC;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;


public class AddCutAction implements Action {
    private Coupes cutToAdd;
    private CNC cnc;
    private CoupeReadOnly cut;

    public AddCutAction(CNC cnc, CoupeReadOnly cut) {
        this.cnc = cnc;
        this.cut = cut;
    }

    @Override
    public void execute() {
        CoupesManageur.addSavedCut(cutToAdd);  // Ajouter la coupe
    }

    @Override
    public void undo() {
        CoupesManageur.supprimerCoupe(cutToAdd);  // Supprimer la coupe
    }


}

