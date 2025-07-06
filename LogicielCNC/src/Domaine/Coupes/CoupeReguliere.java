package LogicielCNC.src.Domaine.Coupes;

import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeReguliere extends Coupes {
    private Point pointOrigine; // Start point
    private Point pointDestination;   // End point
    private Line2D reference;
    private int lengthAwayFromReference;

    public CoupeReguliere(float profondeur, Point pointOrigine, Point pointDestination, OutilReadOnly outil,
                          Line2D reference, int lengthAwayFromBorder) {
        super(profondeur, TypeCoupe.COUPE_REGULIERE, outil); // Set type using enum
        this.pointOrigine = pointOrigine;
        this.pointDestination = pointDestination;
        this.reference = reference;
        this.lengthAwayFromReference = lengthAwayFromBorder;
    }

    @Override
    public Point getPointOrigine() {
        return pointOrigine; // Accessor for the start point
    }

    @Override
    public Point getPointDestination() {
        return pointDestination; // Accessor for the end point
    }

    @Override
    public Point getPoint() {
        return  null;
    }

    @Override
    public Point getPointOppose(){
       return  null;
    }

    @Override
    public boolean isPointInside(Point point) {
        return false;
    }

    @Override
    public Dimension getDimensionFinale() {
        return  null;
    }
    public int getlengthawayfromborder(){return lengthAwayFromReference;}

    @Override
    public boolean isSameCut(CoupeReadOnly coupeReadOnly) {
        return coupeReadOnly.getType() == TypeCoupe.COUPE_REGULIERE &&
                getProfondeur() == coupeReadOnly.getProfondeur() &&
                getPointDestination().x == coupeReadOnly.getPointDestination().x &&
                getPointDestination().y == coupeReadOnly.getPointDestination().y &&
                getPointOrigine().x == coupeReadOnly.getPointOrigine().x &&
                getPointOrigine().y == coupeReadOnly.getPointOrigine().y;
    }

    public boolean isVertical()
    {
        return pointDestination.x == pointOrigine.x;
    }

    public void addLengthAwayFromReference(int newLength)
    {
        if((isVertical() && pointOrigine.x < reference.getX1()) ||
                (!isVertical() && pointOrigine.y < reference.getY2()))
        {
            newLength = -newLength;
        }

        this.lengthAwayFromReference += newLength;
    }

    public int getLengthAwayFromReference()
    {
        return lengthAwayFromReference;
    }

    public Line2D getReference()
    {
        return reference;
    }
/*
*
    // Setter for profondeur
    public void setProfondeur(int profondeur) {
        super.setProfondeur(profondeur); // You might need to handle any additional logic if needed
    }
* */

}
