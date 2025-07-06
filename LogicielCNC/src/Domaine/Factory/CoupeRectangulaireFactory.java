package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Coupes.CoupeRectangulaire;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;
import java.awt.geom.Line2D;

public class CoupeRectangulaireFactory {
        public CoupeRectangulaire createCoupeRectangulaire(float profondeur , Point point, Point pointOppose, CoupeReadOnly reference, Dimension dimension, OutilReadOnly tool)
        {
            return new CoupeRectangulaire(profondeur, point,pointOppose, reference, dimension,tool);
        }
}
