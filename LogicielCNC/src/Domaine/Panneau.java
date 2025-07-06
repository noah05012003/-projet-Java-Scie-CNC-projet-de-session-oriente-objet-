package LogicielCNC.src.Domaine;

import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Coupes.Coupes;


import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

public class Panneau implements PanneauReadOnly {
    private int largeur;
    private int longueur;
    private int epaisseur;
    private List<ZoneInterdite> zonesInterdites;
    private ZoneInterdite zone_interdite;
    private List<Coupes> coupe ;

    public Panneau(int largeur, int longueur, int epaisseur) {
        this.largeur = largeur;
        this.longueur = longueur;
        this.epaisseur = epaisseur;
        this.zonesInterdites = new ArrayList<ZoneInterdite>();
       // this.coupe = new ArrayList<Coupes>();
    }

    @Override
    public int getLargeur() {
        return this.largeur;
    }

    @Override
    public int getLongueur() {
        return this.longueur;
    }

    @Override
    public int getEpaisseur() {
        return this.epaisseur;
    }

    public void ajouterZoneInterdite(Point2D positionXY, Dimension dimensionZone) {
        this.zone_interdite = new ZoneInterdite(positionXY, dimensionZone);
        this.zonesInterdites.add(this.zone_interdite);
    }

    // A modifier..
    public void modifierZoneInterdite(ZoneInterditeReadOnly zoneReadOnly, Dimension newDimension) {
        if (zoneReadOnly == null) {
            throw new IllegalArgumentException("The provided ZoneInterditeReadOnly cannot be null.");
        }

        // Locate the corresponding ZoneInterdite object in the list
        for (ZoneInterdite zone : zonesInterdites) {
            // Check if the current ZoneInterdite matches the ZoneInterditeReadOnly reference
            if (zone.equals(zoneReadOnly)) {
                // Update the ZoneInterdite with the new dimensions
                zone.setDimensionZone(newDimension);
                return; // Update completed
            }
        }

        // If no matching zone was found, throw an exception or handle the error
        throw new IllegalArgumentException("No matching ZoneInterdite found for the provided ZoneInterditeReadOnly.");
    }

    // A modifier..
    public void supprimerZoneInterdite(ZoneInterditeReadOnly zoneInterdite) {
        this.zonesInterdites.remove(zoneInterdite);
    }

    public List<ZoneInterditeReadOnly> getZonesInterdites() {
        List<ZoneInterditeReadOnly> zonesInterditesReadOnly = new ArrayList<ZoneInterditeReadOnly>();
        for (ZoneInterdite zoneInterdite : zonesInterdites ) {
            zonesInterditesReadOnly.add((ZoneInterditeReadOnly) zoneInterdite);
        }
        return  Collections.unmodifiableList(zonesInterditesReadOnly);
    }

    public boolean RegularConflict(CoupeReadOnly coupe){
        if(zonesInterdites.isEmpty()){
            return false;
        }
        for(ZoneInterdite zoneInterdite : zonesInterdites){
            if (coupe.getPointOrigine().getX()>= (zoneInterdite.getPositionXY().getX()-(zoneInterdite.getDimensionZone().getWidth())/2)&&
                coupe.getPointOrigine().getX() <= zoneInterdite.getPositionXY().getX()+(zoneInterdite.getDimensionZone().getWidth())/2)
                return true;
            if( coupe.getPointOrigine().getY() >= (zoneInterdite.getPositionXY().getY()-(zoneInterdite.getDimensionZone().getHeight())/2)&&
                    coupe.getPointOrigine().getY() <= zoneInterdite.getPositionXY().getY()+(zoneInterdite.getDimensionZone().getHeight())/2)
                return true;
        }
        return false;
    }

    public  boolean BordureConflict(CoupeReadOnly coupe){
        if  (zonesInterdites.isEmpty()){
            return false;
        }
        Point pcenter= new Point();
        double rectHeight= coupe.getDimensionFinale().getHeight();
        double rectWidth= coupe.getDimensionFinale().getWidth();
        double xCenter =(this.largeur-rectWidth)/2;
       double yCenter =(this.longueur-rectHeight)/2;
        Line2D line1  =    new Line2D.Double(xCenter,yCenter,xCenter+rectWidth,yCenter);
        Line2D line2 = new Line2D.Double(xCenter,yCenter,xCenter,yCenter+rectHeight);
        Line2D line3 =        new Line2D.Double(xCenter+rectWidth,yCenter,xCenter+rectWidth,yCenter+rectHeight);
        Line2D line4 =       new Line2D.Double(xCenter,yCenter+rectHeight,xCenter+rectWidth,yCenter+rectHeight);
        for(ZoneInterdite zoneInterdite : zonesInterdites){
            Point2D position = zoneInterdite.getPositionXY();
            Dimension dimension=zoneInterdite.getDimensionZone();
            Rectangle2D zone = new Rectangle2D.Double(position.getX()-(dimension.width/2),position.getY()-(dimension.height/2),dimension.width,dimension.height);
            if (zone.intersectsLine(line1) || zone.intersectsLine(line2)|| zone.intersectsLine(line3)|| zone.intersectsLine(line4)) {
                return true; // Conflict detected
            }
    }return false ;}

    public boolean Lconflict(CoupeReadOnly coupe){
        if  (zonesInterdites.isEmpty()){
            return false;
        }
        boolean etat=false;
        Point2D pointOrigine = coupe.getPoint();
        Point2D pointOppose = coupe.getPointOppose();
        double x1 = pointOrigine.getX();
        double y1 = pointOrigine.getY();
        double x2 = pointOppose.getX();
        double y2 = pointOppose.getY();
        // Create lines representing the cut (only two lines: horizontal and vertical edges)
        Line2D line1 = new Line2D.Double(x2,y2,x1,y2); // Horizontal cut line
        Line2D line2 = new Line2D.Double(x2,y2,x2,y1);
        for(ZoneInterdite zoneInterdite : zonesInterdites){
            Point2D position = zoneInterdite.getPositionXY();
            Dimension dimension=zoneInterdite.getDimensionZone();
            Rectangle2D zone = new Rectangle2D.Double(position.getX()-(dimension.width/2),position.getY()-(dimension.height/2),dimension.width,dimension.height);
            if (zone.intersectsLine(line1) || zone.intersectsLine(line2)) {
                return true; // Conflict detected
            }
        }
        return false;
    }

    public boolean RectConflict(CoupeReadOnly coupe){
        if  (zonesInterdites.isEmpty()){
            return false;
        }
        Point2D pointOrigine = coupe.getPoint();
        Point2D pointDestinantion =coupe.getPointOppose();
        double x1 = pointOrigine.getX();
        double y1 = pointOrigine.getY();
        double x2 = pointDestinantion.getX();
        double y2 = pointDestinantion.getY();
        Line2D line1 = new Line2D.Double(x1,y1,x2,y1); // Horizontal cut line
        Line2D line2 = new Line2D.Double(x2,y1,x2,y2);
        Line2D line3 = new Line2D.Double(x2,y2,x1,y2); // Horizontal cut line
        Line2D line4 = new Line2D.Double(x1,y2,x1,y1);
        for(ZoneInterdite zoneInterdite : zonesInterdites){
            Point2D position = zoneInterdite.getPositionXY();
            Dimension dimension=zoneInterdite.getDimensionZone();
            Rectangle2D zone = new Rectangle2D.Double(position.getX()-(dimension.width/2),position.getY()-(dimension.height/2),dimension.width,dimension.height);

            if (zone.intersectsLine(line1) || zone.intersectsLine(line2)|| zone.intersectsLine(line3)|| zone.intersectsLine(line4)) {
                return true; // Conflict detected
            }
        }
        return false;
    }

    public  ZoneInterditeReadOnly getLast(){
        if(zonesInterdites != null && !zonesInterdites.isEmpty()){
            return zonesInterdites.get(zonesInterdites.size()-1);
        }
        return null;
    }


}
