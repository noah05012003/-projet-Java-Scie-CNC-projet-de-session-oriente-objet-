package LogicielCNC.src.Domaine.Coupes;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.Point;
import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeRectangulaire extends CoupeIrreguliere{
    //Constructeur
    public CoupeRectangulaire(float profondeur, Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension,
                              OutilReadOnly outilReadOnly) {
        super(profondeur, point, pointOppose, reference, dimension,  outilReadOnly, TypeCoupe.COUPE_RECTANGULAIRE); // Appel au constructeur de la classe parente
    }

    @Override
    public int getlengthawayfromborder() {
        return 0;
    }

    @Override
    public Point getPointDestination() {
        return  null;
    }

    @Override
    public Point getPointOrigine() {
        return null;
    }

    @Override
    public boolean isPointInside(Point point) {
        return false;
    }


    @Override
    public boolean isSameCut(CoupeReadOnly coupeReadOnly) {
        if(coupeReadOnly.getType()==TypeCoupe.COUPE_RECTANGULAIRE)
        {
            CoupeIrreguliere coupeIrreguliere = (CoupeIrreguliere) coupeReadOnly;
            return getPoint().x == coupeIrreguliere.getPoint().x
                    && getPoint().y == coupeIrreguliere.getPoint().y
                    && getPointOppose().x == coupeIrreguliere.getPointOppose().x
                    && getPointOppose().y == coupeIrreguliere.getPointOppose().y;
        }
        return false;
    }
}
