package LogicielCNC.src.Domaine.Coupes;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeL extends CoupeIrreguliere implements CoupeReadOnly{

    //Constructeur
    public CoupeL(float profondeur, Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension,
                  OutilReadOnly outilReadOnly) {
        super(profondeur, point, pointOppose, reference, dimension, outilReadOnly, TypeCoupe.COUPE_L); // Appel au constructeur de la classe parente
    }
    @Override
    public int getlengthawayfromborder() {
        return 0;
    }

    @Override
    public Point getPointOrigine() {
        return null;
    }

    @Override
    public Point getPointDestination() {
        return null;
    }

    @Override
    public boolean isPointInside(Point point) {
        // Partie verticale : largeur définie par `point` et `pointOppose`
        int minXVertical = this.getPoint().x;
        int maxXVertical = this.getPointOppose().x;
        int minYVertical = this.getPoint().y;
        int maxYVertical = this.getPointOppose().y;

        // Partie horizontale : les deux autres côtés de l'angle
        int minXHorizontal = this.getPoint().x;
        int maxXHorizontal = this.getPointOppose().x;
        int minYHorizontal = this.getPointOppose().y;
        int maxYHorizontal = minYHorizontal + Math.abs(this.getPoint().x - this.getPointOppose().x);

        // Vérifie si le point est dans l'une des deux parties
        boolean isInVertical = (point.x >= minXVertical && point.x <= maxXVertical) &&
                (point.y >= minYVertical && point.y <= maxYVertical);
        boolean isInHorizontal = (point.x >= minXHorizontal && point.x <= maxXHorizontal) &&
                (point.y >= minYHorizontal && point.y <= maxYHorizontal);

        return isInVertical || isInHorizontal;
    }

    @Override
    public boolean isSameCut(CoupeReadOnly coupeReadOnly) {
        if(coupeReadOnly.getType()==TypeCoupe.COUPE_L)
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

//J'ai rajouté ce qui manquait pour l'interface CoupeReadOnly et retiré l'attribut angle
//On a pas besoin d'avoir l'angle de la coupe L qui est formé de deux coupes droites (angle de 90°)
