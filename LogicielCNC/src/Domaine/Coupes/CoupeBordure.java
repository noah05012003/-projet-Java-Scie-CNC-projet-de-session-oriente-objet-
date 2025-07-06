package LogicielCNC.src.Domaine.Coupes;

import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;

public class CoupeBordure extends Coupes{
    private Dimension dimension_finale;

    //Constructeur
    public CoupeBordure(float profondeur, Dimension newDimension, OutilReadOnly outilReadOnly) {
        super(profondeur, TypeCoupe.COUPE_BORDURE, outilReadOnly); // Appel au constructeur de la classe parente
        this.dimension_finale= newDimension;
    }

    @Override
    public Point getPoint(){
        return null;
    }
    public int getlengthawayfromborder(){return 0;}

    @Override
    public Point getPointOrigine(){return null;}

    @Override
    public Point getPointDestination() {
        return  null;
    }

    @Override
    public Point getPointOppose(){return  null;}

    public Dimension getDimensionFinale() {
        return dimension_finale;
    }

    @Override
    public boolean isPointInside(Point point) {
        return false;
    }


    @Override
    public boolean isSameCut(CoupeReadOnly coupeReadOnly) {
        if(coupeReadOnly.getType() == TypeCoupe.COUPE_BORDURE)
        {
            CoupeBordure coupeBordure = (CoupeBordure) coupeReadOnly;
            return getDimensionFinale() == coupeBordure.getDimensionFinale() &&
                getProfondeur() == coupeReadOnly.getProfondeur();
        }
        return false;

    }

}
