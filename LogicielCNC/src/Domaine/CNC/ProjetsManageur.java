package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.Fichier.Projet;
import LogicielCNC.src.Domaine.Fichier.ProjetReadOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjetsManageur {
    private List<Projet> listeProjet;
    private CNC cnc;

    public ProjetsManageur(CNC cnc){
        this.cnc = cnc;
        this.listeProjet = new ArrayList<>();
    }

    public List<ProjetReadOnly> getProjet(){
        List<ProjetReadOnly> projetReadOnlyList = new ArrayList<>();
        for (Projet projet : listeProjet){
            projetReadOnlyList.add((ProjetReadOnly) projet);
        }
        return  Collections.unmodifiableList(projetReadOnlyList);
    }
}
