package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.Coupes.CoupeL;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeLFactory {
    public CoupeL createCoupeL(float profondeur, Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension, OutilReadOnly tool)
    {
        return new CoupeL(profondeur,point, pointOppose, reference, dimension, tool);
    }
}
