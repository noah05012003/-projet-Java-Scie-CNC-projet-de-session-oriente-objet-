package LogicielCNC.src.Vue.Affichage;

import LogicielCNC.src.Domaine.ControleurLarman;
import LogicielCNC.src.Domaine.Coupes.*;
import LogicielCNC.src.Vue.FenetreCoupe;
import LogicielCNC.src.Vue.FenetreInterdite;
import LogicielCNC.src.Vue.FenetrePrincipale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;

public class DrawingPanel extends JPanel implements MouseWheelListener {

    private Rectangle2D rectangle2;
    private ControleurLarman controleurLarman;
    private FenetreInterdite fenetreInterdite;
    private FenetrePrincipale fenetrePrincipale;
    private FenetreCoupe fenetreCoupe;
    private Dessinateur dessinateur;
    private  double facteurZoom = 1.0;
    private double mouseX = 0;
    private double mouseY = 0;
    private int limite_zoom = 0;
    Point centerpoint;
    Point pointclicked;
    Point pointclickedOppose;
    Point referenceCoupeRectangulaire = null;
    private boolean firstPointset = false;
    private Line2D selectedLine = null;
    private  double threshold =2.0;
    private List <Line2D> lines;
    private CoupeReadOnly selected = null;
    private List<CoupeReadOnly> coupeReadOnlyList;
    private int dimensionX;
    private int dimensionY;
    private boolean surCoupe = false;
    private  CoupeReadOnly selectedCoupe = null;
    private  Point offset;
    private Rectangle coupeSurvol;
    private boolean grilleActive = false;
    private boolean magnetisme = false;
    private int taille_cellule = 20;
    private boolean dragging = false;
    private List <Point> refs = new ArrayList<>();




    public DrawingPanel(FenetrePrincipale fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        this.fenetreCoupe = fenetrePrincipale.getFenetreCoupe();
        this.fenetreInterdite=fenetrePrincipale.getFenetreInterdite();
        CoupeClickListener clickListener = new CoupeClickListener();
        CoupeDragListener dragListener = new CoupeDragListener();
        CoupeReleaseListener releaseListener = new CoupeReleaseListener();
        this.addMouseListener(releaseListener);
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
        setBackground(new Color(245, 245, 220));
        prepareMouseClick();
        this.addMouseWheelListener(this);
    }

    //https://stackoverflow.com/questions/63583595/java-graphics2d-zoom-on-mouse-location
    //Pour le zoom




    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();

        //Zoom in
        if(e.getWheelRotation()<0)
        {
            setZoomFactor(facteurZoom+0.1);
            limite_zoom += 1;
        }
        //Zoom out
        if (e.getWheelRotation()>0 )
        {
            if (limite_zoom == 0) {
                return;
            } else {
                setZoomFactor(facteurZoom - 0.1);
                limite_zoom -= 1;
            }
        }
        repaint();
    }

    public void setZoomFactor(double factor){
        facteurZoom = factor;
        if (facteurZoom < 0.01) {
            facteurZoom = 0.01;
        }
    }

    private void prepareMouseClick()
    {
        boolean isInside = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                centerpoint=e.getPoint();

                if (rectangle2.contains(centerpoint)) {
                    fenetreInterdite.receivezonecenter(centerpoint);
                }
                //Ok....
                if (!firstPointset) {
                    pointclicked = e.getPoint();
                    for (CoupeReadOnly coupe : coupeReadOnlyList) {
                        if (coupe.getType() == TypeCoupe.COUPE_L) {
                            CoupeL coupeL = (CoupeL) coupe;
                            if (pointsSimilaires(coupeL.getPointOppose(), pointclicked, 2)) {
                                referenceCoupeRectangulaire = pointclicked;
                                fenetreCoupe.receivePointreference(referenceCoupeRectangulaire);
                                System.out.println("Nous avons un point de référence");
                            }
                        }
                    }
                    fenetreCoupe.receivePointClicked(pointclicked);
                    firstPointset = true;
                } else {
                    pointclickedOppose = e.getPoint();
                    for (CoupeReadOnly coupe : coupeReadOnlyList) {
                        if (coupe.getType() == TypeCoupe.COUPE_L) {
                            CoupeL coupeL = (CoupeL) coupe;
                            if (pointsSimilaires(coupeL.getPointOppose(), pointclickedOppose, 2)) {
                                referenceCoupeRectangulaire = pointclickedOppose;
                                fenetreCoupe.receivePointreference(referenceCoupeRectangulaire);
                                System.out.println("Nous avons un point de référence 2");
                            }
                        }
                    }
                    referenceCoupeRectangulaire = pointclickedOppose;
                    fenetreCoupe.receivePointClickedOppose(pointclickedOppose);
                    firstPointset = false;
                }
                lines = fenetrePrincipale.getLines();
                selectedLine=fenetrePrincipale.findLineNearPoint(lines,pointclicked,threshold);
                if(selectedLine!=null){
                    repaint();
                }

                //  if (!firstPointset) {
                if (rectangle2.contains(e.getPoint())) {


                    if (fenetrePrincipale.needsABorder()) {
                        sendrefIfClose(pointclicked);
                        System.out.println("Nous sommes sur une bordure");
                    }
                    if (pointclicked != null) {
                        System.out.println("Nous avons un premier point :");
                        System.out.println("X :" + pointclicked.x  + "Y :" + pointclicked.y);
                    }
                    if(pointclickedOppose != null){
                        System.out.println("Nous avons un deuxieme point :");
                        System.out.println("X :" + pointclickedOppose.x +  "Y :" + pointclickedOppose.y);
                    }
                    // pointclickedOppose = e.getPoint();

                    // Rechercher une coupe régulière croisée par la projection
                    CoupeReguliere coupeReguliere = findCoupeReguliereUnderProjection(pointclickedOppose);
                    if (coupeReguliere != null) {
                        System.out.println("Une CoupeReguliere a été trouvée sous la projection !");
                        System.out.println("Point Origine : " + coupeReguliere.getPointOrigine());
                        System.out.println("Point Destination : " + coupeReguliere.getPointDestination());
                    } else {
                        System.out.println("Aucune CoupeReguliere n'est croisée par la projection.");
                    }
                    repaint();
                }
            }



        });
        //Ok...
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

                Point survol = e.getPoint();
                verifiererPosition(e.getPoint());

                if(rectangle2.contains(e.getPoint())) {

                    double redimX = survol.getX();
                    double redimY = survol.getY();


                    SendSurvolPoint(redimX,redimY);
                    System.out.println("La souris est à la position " + redimX + "," + redimY);
                }


            }
        });


    }

    private class CoupeReleaseListener extends MouseAdapter{

        public void mouseReleased(MouseEvent e){
            if(selectedCoupe != null && magnetisme){
                int oppX =(int) selectedCoupe.getPointOppose().getX();
                int oppY=(int) selectedCoupe.getPointOppose().getY();
                int newoppX = Math.round((float) oppX/taille_cellule)*taille_cellule-1;
                int newoppY = Math.round((float) oppY/taille_cellule)*taille_cellule-1;
                if (selectedCoupe.getType() == TypeCoupe.COUPE_L) {
                    CoupeL coupeL = (CoupeL) selectedCoupe;
                    fenetrePrincipale.modifierCoupeL(
                            selectedCoupe.getProfondeur(),
                            selectedCoupe.getPoint(),
                            new Point(newoppX,newoppY),
                            coupeL.getReference(),
                            selectedCoupe.getDimensionFinale(),
                            selectedCoupe.getToolName(),
                            selectedCoupe
                    );
                } else if (selectedCoupe.getType() == TypeCoupe.COUPE_RECTANGULAIRE) {
                    CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) selectedCoupe;

                    // Update CoupeRectangulaire details
                    fenetrePrincipale.modifierCoupeRectangulaire(
                            selectedCoupe.getProfondeur(),
                            selectedCoupe.getPoint(),
                            new Point(newoppX,newoppY),
                            coupeRectangulaire.getReference(),
                            selectedCoupe.getDimensionFinale(),
                            selectedCoupe.getToolName(),
                            selectedCoupe
                    );
                }

                // Repaint the component to reflect the changes
                repaint();
            } else if (selectedCoupe != null && !magnetisme){
                if (selectedCoupe.getType() == TypeCoupe.COUPE_L) {
                    Point current = (e.getPoint());
                    int dx = (int)(current.getX() - offset.getX());
                    int dy = (int)(current.getY() - offset.getY());

                    selectedCoupe.getPointOppose().translate(dx,dy);
                    selectedCoupe.getPoint().translate(dx,dy);
                    //Bon pour une coupe régulière le code est trop compliqué...


                    if(selectedCoupe.getType()== TypeCoupe.COUPE_L){
                        CoupeL coupeL = (CoupeL) selectedCoupe;
                        fenetrePrincipale.modifierCoupeL(selectedCoupe.getProfondeur(),selectedCoupe.getPoint(),selectedCoupe.getPointOppose(),coupeL.getReference(), selectedCoupe.getDimensionFinale(),selectedCoupe.getToolName(),selectedCoupe);
                    }else if(selectedCoupe.getType() == TypeCoupe.COUPE_RECTANGULAIRE){
                        CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) selectedCoupe;
                        fenetrePrincipale.modifierCoupeRectangulaire(selectedCoupe.getProfondeur(),selectedCoupe.getPoint(),selectedCoupe.getPointOppose(), coupeRectangulaire.getReference(), selectedCoupe.getDimensionFinale(),selectedCoupe.getToolName(),selectedCoupe);
                    }
                    offset = current;
                    repaint();
                }
            }
        }}




    //Ok....
    public void verifiererPosition(Point point) {
        coupeReadOnlyList = fenetrePrincipale.getListCoupe();
        for(CoupeReadOnly coupeReadOnly : coupeReadOnlyList){

            if(coupeReadOnly.getType() == TypeCoupe.COUPE_BORDURE){
                coupeSurvol = new Rectangle((int) rectangle2.getX(), (int) rectangle2.getY(),coupeReadOnly.getDimensionFinale().width,coupeReadOnly.getDimensionFinale().height);
                if(coupeSurvol.contains(point)){
                    surCoupe = true;
                    dimensionX = coupeReadOnly.getDimensionFinale().width;
                    dimensionY = coupeReadOnly.getDimensionFinale().height;
                    sendDimension(dimensionX,dimensionY);
                    System.out.println("Les dimensions de la coupe où se trouve la souris sont " + coupeReadOnly.getDimensionFinale().width + "en largeur et " + coupeReadOnly.getDimensionFinale().height + "en hauteur");

                }else{
                    surCoupe = false;
                }
                repaint();

            }else if (coupeReadOnly.getType() == TypeCoupe.COUPE_L || coupeReadOnly.getType() == TypeCoupe.COUPE_RECTANGULAIRE){
                coupeSurvol = new Rectangle(coupeReadOnly.getPointOppose().x,coupeReadOnly.getPointOppose().y,coupeReadOnly.getDimensionFinale().width,coupeReadOnly.getDimensionFinale().height);
                if(coupeSurvol.contains(point)){
                    surCoupe = true;
                    dimensionX = coupeReadOnly.getDimensionFinale().width;
                    dimensionY = coupeReadOnly.getDimensionFinale().height;
                    sendDimension(dimensionX,dimensionY);
                    System.out.println("Les dimensions de la coupe où se trouve la souris sont " + coupeReadOnly.getDimensionFinale().width + "en largeur et " + coupeReadOnly.getDimensionFinale().height + "en hauteur");

                }else{
                    surCoupe = false;
                }
                repaint();
                System.out.println("Le nombre de coupes dans le liste est de :" + coupeReadOnlyList.size());
            }

            //Point pointOppose = coupeReadOnly.getPointOppose();

        }
        repaint();

    }

    public boolean isGrilleActive() {
        return  this.grilleActive;
    }

    public void setMagnetisme(boolean b) {
        this.magnetisme = b;
    }

    public void setGrilleMagnetique(boolean b) {
        this.grilleActive = b;
        repaint();
    }
public boolean getmag(){return this.magnetisme;}
    public int getTaille_cellule() {
        return  this.taille_cellule;
    }

    public void setTailleCellule(int taillePx) {
        this.taille_cellule = taillePx;
    }

    //Ok...
    private  class CoupeClickListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            Point clicked = e.getPoint();
            for(CoupeReadOnly coupe : coupeReadOnlyList) {
                Rectangle coupeBounds = new Rectangle(coupe.getPointOppose().x,coupe.getPointOppose().y,coupe.getDimensionFinale().width,coupe.getDimensionFinale().height);
                if(coupeBounds.contains(clicked)){
                    selectedCoupe = coupe;
                    offset = clicked;
                }
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selectedCoupe = null;
        }
    }

    //Ok...
    private class CoupeDragListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e){
            if(selectedCoupe != null ){
                Point current = (e.getPoint());
                int dx = (int)(current.getX() - offset.getX());
                int dy = (int)(current.getY() - offset.getY());

                selectedCoupe.getPointOppose().translate(dx,dy);
                selectedCoupe.getPoint().translate(dx,dy);
                //Bon pour une coupe régulière le code est trop compliqué...


                if(selectedCoupe.getType()== TypeCoupe.COUPE_L){
                    CoupeL coupeL = (CoupeL) selectedCoupe;
                    fenetrePrincipale.modifierCoupeL(selectedCoupe.getProfondeur(),selectedCoupe.getPoint(),selectedCoupe.getPointOppose(),coupeL.getReference(), selectedCoupe.getDimensionFinale(),selectedCoupe.getToolName(),selectedCoupe);
                }else if(selectedCoupe.getType() == TypeCoupe.COUPE_RECTANGULAIRE){
                    CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) selectedCoupe;
                    fenetrePrincipale.modifierCoupeRectangulaire(selectedCoupe.getProfondeur(),selectedCoupe.getPoint(),selectedCoupe.getPointOppose(), coupeRectangulaire.getReference(), selectedCoupe.getDimensionFinale(),selectedCoupe.getToolName(),selectedCoupe);
                }
                offset = current;
                repaint();
            }
        }
    }

    // Retourne le point cliqué



    public void sendDimension(int dx , int dy){
        fenetrePrincipale.receiveDimension(dx,dy);
    }

    public Point getPointClicked()
    {
        return pointclicked;
    }

    public Point getPointclickedOppose() {
        return pointclickedOppose;
    }

    public void SendSurvolPoint(double pointX, double pointY){
        fenetrePrincipale.receiveSurvolPoint(pointX,pointY);
    }

    private void sendrefIfClose(Point clickedPoint){
        double threshold = 5 * facteurZoom;
        List<Line2D>lines = fenetrePrincipale.getLines();
        Line2D selectedLine =fenetrePrincipale.findLineNearPoint(lines,clickedPoint, threshold);
        if (selectedLine != null) {
            boolean isVertical =Math.abs(selectedLine.getX1()-selectedLine.getX2())<threshold;
            boolean isPositiveDirection = isVertical
                    ? clickedPoint.y < selectedLine.getY1()
                    : clickedPoint.x < selectedLine.getX1();
            repaint();

        }
        fenetrePrincipale.sendreference(selectedLine);
    }


/*private void sendPointClicked(Point pointclicked , Point pointclickedOppose)
{
    fenetrePrincipale.sendPointClicked(pointclicked,pointclickedOppose);
}*/


    /*private void sendBorderIfClose(Point point)
    {
        double distanceAwayFromBorder = 5 * facteurZoom;
        if (point.x >= rectangle2.getX()* facteurZoom && point.x <= rectangle2.getX() * facteurZoom +distanceAwayFromBorder)
        {
            fenetrePrincipale.makeBorderCutFromReference(true,true);
        }
        else if(point.x <= rectangle2.getX()+rectangle2.getWidth()
                && point.x >= rectangle2.getX()-distanceAwayFromBorder + rectangle2.getWidth())
        {
            fenetrePrincipale.makeBorderCutFromReference(true,false);
        }
        else if (point.y >= rectangle2.getY() && point.y <= rectangle2.getY()+distanceAwayFromBorder)
        {
            fenetrePrincipale.makeBorderCutFromReference(false,true);
        }
        else if(point.y <= rectangle2.getY()+rectangle2.getHeight()
                && point.y >= rectangle2.getY()-distanceAwayFromBorder + rectangle2.getHeight())
        {
            fenetrePrincipale.makeBorderCutFromReference(false,false);
        }
    }*/

    /*private void sendCornerIfClose(Point point)
    {
        double distanceAwayFromBorder = 10 * facteurZoom;

        /*if (!isPointACorner(point)){
            System.out.println("Point clicked is not a corner");
            fenetrePrincipale.sendCornerFromBorder(pointclicked);

        //Coin gauche haut
        if (point.x >= rectangle2.getX()* facteurZoom &&
                point.x <= rectangle2.getX() * facteurZoom +distanceAwayFromBorder
            && point.y >= rectangle2.getY() &&
                point.y <= rectangle2.getY()+distanceAwayFromBorder)
        {
            fenetrePrincipale.sendPointClicked(new Point(0,0));
        }
        //Coin droit haut
        else if(point.x <= rectangle2.getX()+rectangle2.getWidth()
                && point.x >= rectangle2.getX()-distanceAwayFromBorder + rectangle2.getWidth()
                && point.y >= rectangle2.getY() &&
                point.y <= rectangle2.getY()+distanceAwayFromBorder)
        {
            fenetrePrincipale.sendPointClicked(new Point((int) (rectangle2.getX()+rectangle2.getWidth()),0));
        }
        //Coin gauche bas
        else if (point.x >= rectangle2.getX()* facteurZoom &&
                point.x <= rectangle2.getX() * facteurZoom +distanceAwayFromBorder
                && point.y <= rectangle2.getY()+rectangle2.getHeight()
                && point.y >= rectangle2.getY()-distanceAwayFromBorder + rectangle2.getHeight())
        {
            fenetrePrincipale.sendPointClicked(new Point(0, (int) (rectangle2.getY()+rectangle2.getHeight())));
        }
        //Coin droit bas
        else if(point.x <= rectangle2.getX()+rectangle2.getWidth()
                && point.x >= rectangle2.getX()-distanceAwayFromBorder + rectangle2.getWidth()
                && point.y <= rectangle2.getY()+rectangle2.getHeight()
                && point.y >= rectangle2.getY()-distanceAwayFromBorder + rectangle2.getHeight())
        {
            fenetrePrincipale.sendPointClicked(new Point((int) (rectangle2.getX()+rectangle2.getWidth()),
                    (int) (rectangle2.getY()+rectangle2.getHeight())));
        }

    }*/


    // Obtenir les coordonnees des coins du panneau
    public List<Point> getCoordonnesCorners()
    {
        Rectangle rectangle = rectangle2.getBounds();
        Point location = rectangle.getLocation();

        List<Point> corners = new ArrayList<>();
        corners.add(new Point(location.x, location.y));
        corners.add(new Point(location.x + rectangle.width, location.y));
        corners.add(new Point(location.x, location.y + rectangle.height));
        corners.add(new Point(location.x + rectangle.width, location.y + rectangle.height));
        return corners;
    }

    // Verifie si le point est proche d'un coin d'une coupe rectangulaire
   /* public boolean isPointNearCutCorner(Point pointReference) {
        List<CoupeReadOnly> corners = getCoupeCoordonneesCorners(controleurLarman.getCoupe());
        for (Coupes coupe : corners) {

        }
        return false;
    }*/



    //Obtiens les coordonnes des coins des coupes rectangulaires dans le panneau
   /* public List<Point> getCoupeCoordonneesCorners(List<Coupes> listeCoupe){
        List <Point> corners = new ArrayList<>();

        for(Coupes coupe : listeCoupe){
            if (coupe.getType() == TypeCoupe.COUPE_RECTANGULAIRE) {
                Rectangle rectangle = new Rectangle(coupe.getPoint().x, coupe.getPoint().y, coupe.getPointDestination().x - coupe.getPoint().x, coupe.getPointDestination().y - coupe.getPoint().y);
                Point location = rectangle.getLocation();
                corners.add(new Point(location.x, location.y));
                corners.add(new Point(location.x + rectangle.width, location.y));
                corners.add(new Point(location.x, location.y + rectangle.height));
                corners.add(new Point(location.x + rectangle.width, location.y + rectangle.height));
            }
        }
        return corners;
    }*/

    public boolean isPointACorner(Point point)
    {
        List<Point> corners = getCoordonnesCorners();
        for(Point corner : corners)
        {
            if(point.x >= corner.x  && point.x <= corner.x + 5
                    && point.y >= corner.y && point.y <= corner.y + 5)
            {
                return true;
            }
        }
        return false;
    }



    @Override
    protected void paintComponent(Graphics g)
    {
        if (fenetrePrincipale != null){
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            AffineTransform at = graphics2D.getTransform();
            at.translate(mouseX, mouseY);
            at.scale(facteurZoom, facteurZoom);
            at.translate(-mouseX, -mouseY);
            graphics2D.setTransform(at);




            if (dessinateur == null) {
                dessinateur = new Dessinateur(fenetrePrincipale.getControleurLarman(), new Dimension(getWidth(), getHeight()), this);
            }

            else {
                // mettre a jour les dimensions de pannel dans la classe dessinateur,  used when we resize the window
                dessinateur.setDimensions(new Dimension(getWidth(), getHeight()));
            }
            fenetrePrincipale.deleteAllLines();
            dessinateur.dessine(g);
            rectangle2.setRect(rectangle2.getX() * facteurZoom, rectangle2.getY() * facteurZoom,
                    rectangle2.getWidth() * facteurZoom, rectangle2.getHeight() * facteurZoom);
            //calculateGridIntersections(this.getWIDTH(),this.getHEIGHT(),taille_cellule);

        }
    }

    /*
    * //double x = (mouseX * facteurZoom) - mouseX;
            //double y = (mouseY * facteurZoom) - mouseY;
            double x = mouseX / facteurZoom;
            double y = mouseX / facteurZoom;
            double x2 = ((getWidth() - rectangle2.getWidth() * facteurZoom) / 2) / facteurZoom;
            double y2 = ((getHeight() - rectangle2.getHeight() * facteurZoom) / 2) / facteurZoom;
            if(x2 > 0) x -= x2;
            if(y2 > 0) y -= y2;
            rectangle2.setRect(rectangle2.getX() + x,rectangle2.getY() + y,
                    rectangle2.getWidth() * facteurZoom, rectangle2.getHeight()* facteurZoom);
    * */

    public CoupeReadOnly getCurrentCut()
    {
        return fenetrePrincipale.getCurrentCut();
    }

    public void setRectangle(Rectangle2D rectangle2D)
    {
        this.rectangle2 = rectangle2D;
    }

    public void paintAgain()
    {
        repaint();
    }

    public double getFacteurZoom()
    {
        return facteurZoom;
    }

    public double getMouseX()
    {
        return mouseX;
    }

    public double getMouseY()
    {
        return mouseY;
    }

    public Rectangle2D getRectangle2()
    {
        return rectangle2;
    }

    public int getHEIGHT()
    {
        return dessinateur.getHeight();
    }

    public int getWIDTH()
    {
        return dessinateur.getWidth();
    }

    public Point getPointDrawnOnBoard(Point point)
    {
        return fenetrePrincipale.getPointOnDrawnBoard(point);
    }

    public FenetrePrincipale getFenetrePrincipale() {return fenetrePrincipale;}

    //Mathode pour les nouvelles références:

    public Line2D getSelectedLine() {
        return selectedLine;
    }

    public CoupeReguliere findCoupeReguliereUnderProjection(Point pointclickedOppose) {
        if (pointclickedOppose == null || coupeReadOnlyList == null || coupeReadOnlyList.isEmpty()) {
            return null; // Aucun point ou liste vide
        }

        Line2D projection = new Line2D.Double(
                pointclickedOppose.x,
                pointclickedOppose.y,
                pointclickedOppose.x,
                Integer.MAX_VALUE // Une ligne verticale infinie vers le bas
        );

        for (CoupeReadOnly coupe : coupeReadOnlyList) {
            if (coupe.getType() == TypeCoupe.COUPE_REGULIERE) {
                CoupeReguliere coupeReguliere = (CoupeReguliere) coupe;
                Line2D reference = new Line2D.Double(
                        coupeReguliere.getPointOrigine().x,
                        coupeReguliere.getPointOrigine().y,
                        coupeReguliere.getPointDestination().x,
                        coupeReguliere.getPointDestination().y
                );

                // Vérifier si la projection croise la ligne de référence
                if (reference.intersectsLine(projection)) {
                    return coupeReguliere; // Retourner la première coupe trouvée
                }
            }
        }

        return null; // Aucune coupe régulière croisée
    }

    public static boolean pointsSimilaires(Point p1, Point p2, double epsilon) {
        // Calcul de la distance euclidienne entre les deux points
        double distance = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
        // Vérifie si la distance est inférieure ou égale à epsilon
        return distance <= epsilon;
    }




}
