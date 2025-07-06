package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.*;
import LogicielCNC.src.Domaine.Coupes.CoupeReguliere;

import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeReguliereFactory {

    public CoupeReguliere createCoupeReguliere(float deepness, Line2D reference,
                                               int dimensionAwayFromReference, OutilReadOnly outil, Dimension dimensionPanneau)
    {
        Point origin = new Point();
        Point destination = new Point();
        int diferrenceWithReference = dimensionAwayFromReference;
        if(reference.getX1() == reference.getX2())
        {
            origin.y = (int)reference.getY1();
            destination.y = (int)reference.getY2();
            origin.x = (int)reference.getX1()+ diferrenceWithReference;
            destination.x = (int)reference.getX2()+ diferrenceWithReference;
            if (origin.x<0 || origin.x>dimensionPanneau.getWidth())
            {
                diferrenceWithReference = -dimensionAwayFromReference;
                origin.x=(int)reference.getX1()+diferrenceWithReference;
                destination.x=(int)reference.getX2()+diferrenceWithReference;
            }

        }
        else
        {
            origin.y = (int)reference.getY1()+diferrenceWithReference;
            destination.y = (int)reference.getY2()+diferrenceWithReference;
            origin.x = (int)reference.getX1();
            destination.x = (int)reference.getX2();
            if (origin.y<0 || origin.y>dimensionPanneau.getHeight()){
                diferrenceWithReference = -dimensionAwayFromReference;
                origin.y = (int)reference.getY1()+diferrenceWithReference;
                destination.y = (int)reference.getY2()+diferrenceWithReference;
            };
        }
        return new CoupeReguliere(deepness,origin,destination,
                outil, reference, dimensionAwayFromReference);
    }

    /*public CoupeReguliere createCoupeReguliereDeBordure(boolean isVertical, boolean isStartOfDimension,
                                                        Dimension dimensionPanneau)
    {
        Point origin = new Point();
        Point destination = new Point();
        if(isVertical)
        {
            origin.y=0;
            destination.y = dimensionPanneau.height;
            if(isStartOfDimension)
            {
                origin.x = 0;
                destination.x =0;
            }
            else
            {
                origin.x = dimensionPanneau.width;
                destination.x = dimensionPanneau.width;
            }
        }
        else
        {
            origin.x=0;
            destination.x = dimensionPanneau.width;
            if(isStartOfDimension)
            {
                origin.y = 0;
                destination.y =0;
            }
            else
            {
                origin.y = dimensionPanneau.height;
                destination.y = dimensionPanneau.height;
            }
        }
        return new CoupeReguliere(0,origin, destination, null, null, 0);
    }*/
}
