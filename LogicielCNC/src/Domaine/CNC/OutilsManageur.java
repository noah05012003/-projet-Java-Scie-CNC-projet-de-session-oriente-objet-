package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.Factory.OutilFactory;
import LogicielCNC.src.Domaine.Outil;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OutilsManageur {
    private List<Outil> listeOutils;
    private Outil outil;
    private CNC cnc;
    private OutilFactory outilFactory;

    public OutilsManageur(CNC cnc)
    {
        this.cnc = cnc;
        this.listeOutils = new ArrayList<>(12);
        this.outilFactory = new OutilFactory();
    }

    //Méthode pour retourner les outils en read-only
    public List<OutilReadOnly> getOutil() {
        List<OutilReadOnly> outilsReadOnly = new ArrayList<>(12);
        for (Outil outil : listeOutils ) {
            outilsReadOnly.add((OutilReadOnly) outil);
        }
        return  Collections.unmodifiableList(outilsReadOnly);
    }

    public OutilReadOnly getToolByName(String name)
    {
        OutilReadOnly outilReadOnly = null;
        for (Outil listeOutil : listeOutils) {
            if (listeOutil.getNom().equals(name)) {
                outilReadOnly = listeOutil;
                break;
            }
        }
        return outilReadOnly;
    }

    public List<String> getToolsNames()
    {
        List<String> names = new ArrayList<>();
        for (Outil outil : listeOutils ) {
            names.add(outil.getNom());
        }

        return names;
    }

    public void ajouterOutil(String nom, int largeur , int position){
        outil = outilFactory.createOutil(nom, largeur, position);
        listeOutils.add(outil);
    }

    public void supprimerOutil(String nom)
    {
        //On crée un itérateur
        Iterator<Outil> iterator = listeOutils.iterator();
        while (iterator.hasNext())
        {
            Outil outil = iterator.next();
            if(outil.getNom().equals(nom)){
                iterator.remove();
                //break; //On termine la boucle après la suppression
            }
        }
    }

    public void addSavedTool(OutilReadOnly outilReadOnly)
    {
        listeOutils.add((Outil) outilReadOnly);
    }

}
