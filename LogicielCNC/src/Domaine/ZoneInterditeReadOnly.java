package LogicielCNC.src.Domaine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

public interface ZoneInterditeReadOnly {
    public Point2D getPositionXY();
    public Dimension getDimensionZone();
}
