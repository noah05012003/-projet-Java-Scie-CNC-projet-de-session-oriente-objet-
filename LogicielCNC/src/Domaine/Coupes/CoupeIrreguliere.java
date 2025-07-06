package LogicielCNC.src.Domaine.Coupes;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;
import java.awt.geom.Line2D;

public abstract class CoupeIrreguliere extends Coupes {
    private Point point; // Start point
    private Point pointOppose;
    private Dimension dimension;
    private CoupeReadOnly reference;

    public CoupeIrreguliere(float profondeur, Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension,
                            OutilReadOnly outilReadOnly, TypeCoupe typeCoupe) {
        super(profondeur, typeCoupe,outilReadOnly); // Set type using enum
        this.point = point;
        this.pointOppose = pointOppose;
        this.dimension = dimension;
        this.reference = reference;
    }

    //Accesseurs(Getters):
    @Override
    public float getProfondeur() {
        return super.getProfondeur();
    }

    @Override
    public Point getPoint() {
        return point;
    }

    public Point getPointOppose() {
        return pointOppose;
    }

    public Dimension getDimensionFinale()
    {
        return dimension;
    }

    public CoupeReadOnly getReference()
    {
        return reference;
    }

    //Mutateurs (Setters):
    public void setPoint(Point point) {this.point = point;}

    public void setPointOppose(Point point) {this.pointOppose = point;}

    public void setReference(CoupeReadOnly reference) {this.reference = reference;}


    @Override
    public void setProfondeur(int profondeur) {
        super.setProfondeur(profondeur);
    }

    @Override
    public TypeCoupe getType() {
        return super.getType();
    }


}