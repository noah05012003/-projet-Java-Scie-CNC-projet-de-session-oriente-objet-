package LogicielCNC.src.Domaine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Classe ZoneInterdite
 * */
public class ZoneInterdite implements ZoneInterditeReadOnly, Serializable {
    private Point2D positionXY;
    private Dimension dimensionZone;

    public ZoneInterdite(Point2D positionXY, Dimension dimensionZone) {
        this.positionXY = positionXY;
        this.dimensionZone = dimensionZone;

    }

    /**
     * Redefini la position de la zone interdite
     * @param newPositionXY la nouvelle position de la zone interdite
     * */
    public void setPositionXY(Point newPositionXY) {
        this.positionXY = newPositionXY;
    }

    /**
     * Redefini la dimension de la zone interdite
     * @param newDimensionZone la nouvelle dimension de la zone interdite
     * */
    public void setDimensionZone(Dimension newDimensionZone) {
        this.dimensionZone = newDimensionZone;
    }

    /**
     * Retourne la position de la zone interdite
     * */
    @Override
    public Point2D getPositionXY() {
        return this.positionXY;
    }

    /**
     * Retourne la dimension de la zone interdite
     * */
    @Override
    public Dimension getDimensionZone() {
        return this.dimensionZone;
    }
}
