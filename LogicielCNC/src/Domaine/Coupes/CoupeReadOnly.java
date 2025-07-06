package LogicielCNC.src.Domaine.Coupes;

import java.awt.*;
import java.io.Serializable;

public interface CoupeReadOnly extends Serializable {
    public float getProfondeur();         // Get the depth of the cut
    public Point getPoint(); // Get the start point of the cut
    public  Point getPointOppose();
    public Point getPointOrigine();
    public Point getPointDestination();
    public int getToolWidth();
    public boolean hasTool();
    public TypeCoupe getType();
    public boolean isSameCut(CoupeReadOnly coupeReadOnly);
    public Dimension getDimensionFinale();
    public String getToolName();
    public int getlengthawayfromborder();
}
