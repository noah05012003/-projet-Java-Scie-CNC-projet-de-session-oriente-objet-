package LogicielCNC.src.Domaine.Fichier;

import java.util.List;

public class Projet implements ProjetReadOnly {
    private String nom;
    private List listeDeDonnees;
    private GCODE FichierGcode;

    /**
     * Exporte le projet en Gcode
     * */
    public void exporterGcode(GCODE fichierGcode){
        this.FichierGcode = fichierGcode;
    }

    /**
     * Retourne le nom du projet
     * */
    @Override
    public String getNom(){
        return this.nom;
    }

    /**
     * Retourne la liste de données du projet
     * */
    @Override
    public List getDonnees(){
        return this.listeDeDonnees;
    }
}
