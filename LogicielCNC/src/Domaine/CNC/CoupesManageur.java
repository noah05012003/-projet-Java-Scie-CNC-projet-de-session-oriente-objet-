package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.Coupes.*;
import LogicielCNC.src.Domaine.Factory.CoupeBordureFactory;
//import LogicielCNC.src.Domaine.Factory.CoupeIrreguliereFactory;
import LogicielCNC.src.Domaine.Factory.CoupeLFactory;
import LogicielCNC.src.Domaine.Factory.CoupeRectangulaireFactory;
import LogicielCNC.src.Domaine.Factory.CoupeReguliereFactory;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoupesManageur {
    private static List<Coupes> listeCoupe;
    private  static  List<CoupeReadOnly> coupeReadOnlyList;
    private static Coupes coupe;
    private CNC cnc;
    CoupeReguliereFactory coupeReguliereFactory;
    CoupeBordureFactory coupeBordureFactory;
    CoupeRectangulaireFactory coupeRectangulaireFactory;
    CoupeLFactory coupeLFactory;

    public CoupesManageur(CNC cnc){
        this.cnc = cnc;
        this.listeCoupe = new ArrayList<>();
        coupeReadOnlyList = new ArrayList<>();
        coupeReguliereFactory = new CoupeReguliereFactory();
        coupeBordureFactory = new CoupeBordureFactory();
        coupeRectangulaireFactory = new CoupeRectangulaireFactory();
        coupeLFactory = new CoupeLFactory();

    }




    //Méthode pour retourner les coupes en read-only
    public static  List<CoupeReadOnly> getCoupe() {
        List<CoupeReadOnly> coupeReadOnlyList = new ArrayList<>();
        for (Coupes coupe : listeCoupe) {
            if(coupe != null) {
                coupeReadOnlyList.add(coupe);
            } else {
                System.out.println("Attention : L'élément " + coupe + " n'est pas une instance de CoupeReadOnly.");
            }
        }
        return Collections.unmodifiableList(coupeReadOnlyList);
    }





    public CoupeReadOnly getCurrentCut()
    {
        return coupe;
    }

    //Méthode qui crée coupe régulière
    public CoupeReadOnly addRegularCut(float deepness, Line2D reference,
                                       int dimensionAwayFromReference, String toolName)
    {
        //On fera une assurance plus tard
        CoupeReguliere coupeReguliere = coupeReguliereFactory.createCoupeReguliere(deepness, reference,
                dimensionAwayFromReference, cnc.getToolByName(toolName), cnc.getPanneauAdessiner());
        this.coupe = coupeReguliere;
        listeCoupe.add(coupeReguliere);
        return coupeReguliere;
    }

    //Méthode qui crée une coupe de type L
    public CoupeReadOnly ajouterCoupeL(float profondeur , Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension , String nomOutil)
    {
        CoupeL coupeL = new CoupeL(profondeur,pointOrigine,pointOppose,reference, dimension,cnc.getToolByName(nomOutil));
        this.coupe = coupeL;
        listeCoupe.add(coupeL);
        return coupeL;
    }

    //méthode qui crée une coupe de type rectangulaire
    public CoupeReadOnly ajouterCoupeRectangulaire(float profondeur , Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension , String nomOutil)
    {
        CoupeRectangulaire coupeRectangulaire = new CoupeRectangulaire(profondeur,pointOrigine,pointOppose, reference, dimension,cnc.getToolByName(nomOutil));
        this.coupe = coupeRectangulaire;
        listeCoupe.add(coupeRectangulaire);
        return coupeRectangulaire;
    }

    //Méthode qui crée une coupe de type Retaille de Bordure
    public CoupeReadOnly ajouterCoupeBordure(float profondeur ,  Dimension newDimension, String toolName){
        CoupeBordure coupe = coupeBordureFactory.createCoupeBordure(profondeur, newDimension, cnc.getToolByName(toolName));
        this.coupe = coupe;
        listeCoupe.add(coupe);
        return coupe;
    }

    //A voir...
    public CoupeReadOnly modifierCoupeReguliere(float deepness, float movement, CoupeReadOnly coupeReadOnly,
                                       String toolName){

        if (coupeReadOnly==null) {
            System.out.println("Avertissement: On ne peut pas modifier une coupe si elle n'est pas enregistrée");
            return null;
        }
        else{
            CoupeReguliere newCut = (CoupeReguliere) coupeReadOnly;
            newCut.addLengthAwayFromReference((int) movement);
            newCut = coupeReguliereFactory.createCoupeReguliere(deepness, newCut.getReference(),
                    newCut.getLengthAwayFromReference(), cnc.getToolByName(toolName), cnc.getPanneauAdessiner());
            replaceCutByModifiedOne(coupeReadOnly, newCut);
            return this.coupe;
        }
    }

    public CoupeReadOnly modifyBorderCut(float deepness, CoupeReadOnly coupeReadOnly,
                                         Dimension newDimension, String toolName)
    {
        CoupeReadOnly newCut = coupeBordureFactory.createCoupeBordure(deepness,
                newDimension, cnc.getToolByName(toolName));
        replaceCutByModifiedOne(coupeReadOnly, newCut);
        return newCut;
    }

    //Pour L et rectangles
   /* public CoupeReadOnly modifierCoupeIrreguliere(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName, TypeCoupe typeCoupe)
    {
        CoupeIrreguliere coupeIrreguliere = (CoupeIrreguliere) coupeReadOnly;
        CoupeReadOnly newCut = coupeIrreguliereFactory.createIrregularCut(deepness,
                coupeIrreguliere.getReferenceVertical(),coupeIrreguliere.getReferenceHorizontal(), dimension,
                cnc.getToolByName(toolName), typeCoupe, cnc.getPanneauAdessiner());
        replaceCutByModifiedOne(coupeReadOnly, newCut);
        return newCut;
    }*/

    // Modifier coupe L
    public CoupeReadOnly modifierCoupeL(float profondeur, Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension,
                                        String outilReadOnly, CoupeReadOnly coupe){

        CoupeL coupeL = coupeLFactory.createCoupeL(profondeur, point, pointOppose, reference, dimension, cnc.getToolByName(outilReadOnly));
        CoupeReadOnly newCut = coupeL;
        replaceCutByModifiedOne(coupe, newCut);
        return newCut;
    }

    //Modifier Coupe Rectangle
    public CoupeReadOnly modifierCoupeRectangulaire(float profondeur , Point pointOrigine , Point pointOppose , CoupeReadOnly reference, Dimension dimension, String outilReadOnly, CoupeReadOnly coupeReadOnly){
        CoupeRectangulaire coupeRectangulaire = coupeRectangulaireFactory.createCoupeRectangulaire(profondeur,pointOrigine,pointOppose, reference, dimension, cnc.getToolByName(outilReadOnly));
        CoupeReadOnly newCut = coupeRectangulaire;
        replaceCutByModifiedOne(coupeReadOnly,newCut);
        return  newCut;
    }

    private void replaceCutByModifiedOne(CoupeReadOnly oldCut, CoupeReadOnly newCut)
    {
        for(int cpt =0; cpt<listeCoupe.size();cpt++)
        {
            Coupes coupes = listeCoupe.get(cpt);
            if(oldCut.isSameCut(coupes))
            {
                listeCoupe.set(cpt, (Coupes) newCut);
                this.coupe = (Coupes) newCut;
            }
        }
    }

    //A voir...
    public static boolean supprimerCoupe(Coupes coupe){
        boolean supprimer = removeCutFromList(coupe);
        if (!supprimer) {
            System.out.println("Avertissement: La coupe spécifiée n'a pas été trouvée dans la liste.");
        } else {
            System.out.println("La coupe a été supprimée avec succès.");
        }

        return supprimer;
    }

    public CoupeReadOnly getNextCut()
    {
        for(int cpt=0; cpt<listeCoupe.size(); cpt++)
        {
            if(coupe==listeCoupe.get(cpt))
            {
                if(cpt== listeCoupe.size()-1)
                {
                    this.coupe = listeCoupe.get(0);
                }
                else
                {
                    this.coupe = listeCoupe.get(cpt+1);
                }
                break;
            }
        }
        return this.coupe;
    }

    public CoupeReguliere findCoupeReguliereUnderProjection(Point pointclickedOppose, List<CoupeReadOnly> coupeReadOnlyList) {
        if (pointclickedOppose == null || coupeReadOnlyList == null || coupeReadOnlyList.isEmpty()) {
            return null; // Aucun point ou liste vide
        }

        Line2D projection = new Line2D.Double(
                pointclickedOppose.x,
                pointclickedOppose.y,
                pointclickedOppose.x,
                Integer.MAX_VALUE // Une ligne verticale infinie vers le bas
        );

        for (CoupeReadOnly coupe : coupeReadOnlyList) {
            if (coupe.getType() == TypeCoupe.COUPE_REGULIERE) {
                CoupeReguliere coupeReguliere = (CoupeReguliere) coupe;
                Line2D reference = new Line2D.Double(
                        coupeReguliere.getPointOrigine().x,
                        coupeReguliere.getPointOrigine().y,
                        coupeReguliere.getPointDestination().x,
                        coupeReguliere.getPointDestination().y
                );

                // Vérifier si la projection croise la ligne de référence
                if (reference.intersectsLine(projection)) {
                    return coupeReguliere; // Retourner la première coupe trouvée
                }
            }
        }

        return null; // Aucune coupe régulière croisée
    }

    private static boolean removeCutFromList(Coupes coupe)
    {
        for(int cpt =0; cpt<listeCoupe.size();cpt++)
        {
            Coupes coupes = listeCoupe.get(cpt);
            if(coupe.isSameCut(coupes))
            {
                listeCoupe.remove(cpt);
                if(cpt!=0)
                {
                    CoupesManageur.coupe = listeCoupe.get(cpt-1);
                }
                else
                {
                    if(!listeCoupe.isEmpty())
                    {
                        CoupesManageur.coupe = listeCoupe.getFirst();
                    }
                    else
                    {
                        CoupesManageur.coupe = null;
                    }
                }
                return true;

            }
        }
        return false;
    }

    public void DeleteAllCoupes(){
        listeCoupe.clear();
    }

    public static void addSavedCut(CoupeReadOnly coupeReadOnly)
    {
        listeCoupe.add((Coupes) coupeReadOnly);
    }

    public boolean areAllCutsFine()
    {
        for(int cpt=0; cpt<listeCoupe.size(); cpt++)
        {
            if(cnc.RegularConflict(coupe))
            {
                return false;
            }
        }
        return true;
    }
}
