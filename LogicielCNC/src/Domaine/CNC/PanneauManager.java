package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.*;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Factory.PanneauFactory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PanneauManager {
    private Panneau panneau;
    private List <Panneau> panneaux;
    private CNC cnc;
    private PanneauFactory panneauFactory;
    private  OutilsManageur outilsManageur;
    private  CoupesManageur coupeManageur;


   public PanneauManager(CNC cnc)
    {
        this.panneaux=new ArrayList<Panneau>();

        this.cnc = cnc;
        this.panneauFactory = new PanneauFactory(outilsManageur, coupeManageur);

    }

public Dimension getPanneauAdessiner(){
       PanneauReadOnly lastPanneau = getLastPanneau();
       return new Dimension(lastPanneau.getLargeur(), lastPanneau.getLongueur());
}
public PanneauReadOnly getLastPanneau(){
       if(!panneaux.isEmpty()){
           return(PanneauReadOnly) panneaux.get(panneaux.size()-1);
       }
       return null;
}



public void ajouterPanneau(int longeur, int largeur, int epaisseur){
       panneau = panneauFactory.createPanneau(longeur, largeur, epaisseur);
       this.panneaux.add(panneau);
}
    public int getTrueHeight(int height, int heightDrawn)
    {
        float multiplicator = (float) panneau.getLongueur()/heightDrawn;
        return (int) (height * multiplicator);
    }

    public int getTrueWidth(int width, int widthDrawn)
    {
        float multiplicator = (float) panneau.getLargeur()/widthDrawn;
        return (int) (width * multiplicator);
    }

    public int getPanelLength()
    {
        return panneau.getLargeur();
    }

    public int getPanelHeight()
    {
        return panneau.getLongueur();
    }

    public Point giveTruePosition(Point point, int lengthDrawn, int heightDrawn)
    {
        float multiplicator = (float) panneau.getLargeur()/lengthDrawn;
        point.x = (int) (point.x * multiplicator);
        multiplicator = (float) panneau.getLongueur()/heightDrawn;
        point.y = (int) (point.y * multiplicator);
        return point;
    }

    public List<ZoneInterditeReadOnly> getZoneInterdite(){
        return panneau.getZonesInterdites();
    }

    public PanneauReadOnly getPanneau(){
        return (PanneauReadOnly) panneau;
    }

    //Call la classe Panneau pour créer l'objet
    public void ajouterZoneInterdite(Point2D positionXY, Dimension dimensionZone){
        panneau.ajouterZoneInterdite(positionXY, dimensionZone);
    }


    //Call la classe Panneau pour effectuer la modification
    public void modifierZoneInterdite(ZoneInterditeReadOnly zone, Dimension dimension){
        panneau.modifierZoneInterdite(zone, dimension);
    }

    public void supprimerZoneInterdite(ZoneInterditeReadOnly zoneInterdite){
        panneau.supprimerZoneInterdite(zoneInterdite);
    }

    public void replacePanelBySavedOne(PanneauReadOnly panneauReadOnly)
    {
        this.panneau = (Panneau) panneauReadOnly;
    }
    public boolean RegularConflict(CoupeReadOnly coupe){
       return getDernier().RegularConflict(coupe);
    }
    public boolean BordureConflict(CoupeReadOnly coupe){
        return getDernier().BordureConflict(coupe);
    }
    public boolean Lconflict(CoupeReadOnly coupe){
       return getDernier().Lconflict(coupe);
    }
    public  boolean RectConflict(CoupeReadOnly coupe){
       return getDernier().RectConflict(coupe);
    }
    public Panneau getDernier(){
        if(!panneaux.isEmpty()){
            return(Panneau) panneaux.get(panneaux.size()-1);
        }
        return null;

    }
    public  ZoneInterditeReadOnly getLast(){
       return panneau.getLast();
    }
    /* À REVOIR
    public void zoomSur(Point centreZoom, double facteurZoom) {
        // Adapter les dimensions et positions en fonction du facteurZoom
        panneau.setLargeur((int) (panneau.getLargeur() * facteurZoom));
        panneau.setLongueur((int) (panneau.getLongueur() * facteurZoom));
    } */

}
