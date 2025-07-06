package LogicielCNC.src.Vue;

import LogicielCNC.src.Domaine.*;
import LogicielCNC.src.Domaine.CNC.UndoRedoManager;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Coupes.CoupeReguliere;
import LogicielCNC.src.Domaine.Coupes.TypeCoupe;
import LogicielCNC.src.Vue.Affichage.DrawingPanel;
import LogicielCNC.src.Domaine.CNC.CNC;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.List;

public class FenetrePrincipale extends JFrame {
    private JPanel mainJpanel;
    private FenetreAffichee fenetreAffichee;
    private FenetreAccueil fenetreAccueil;
    private FenetreCoupe fenetreCoupe;
    private FenetrePanneau fenetrePanneau;
    private FenetreOutil fenetreOutil;
    private FenetreInterdite fenetreInterdite;
    private FenetreSauvegarde fenetreSauvegarde;
    private  FenetreGrille fenetreGrille;
    private DrawingPanel drawingPanel;
    private CardLayout cardLayout;
    private ControleurLarman controleurLarman;
    private int largeur = 600;
    private int longeur = 500;
    private int epaisseur = 10;
    private CNC cnc;
    private final UndoRedoManager undoRedoManager;





    public FenetrePrincipale(ControleurLarman controleurLarman)
    {
        this.controleurLarman = controleurLarman;
        this.undoRedoManager = new UndoRedoManager();
        this.cnc = new CNC();
        controleurLarman.ajouterPanneau(largeur, longeur, epaisseur);
        controleurLarman.ajouterOutil("outil par défaut", 2, 0);
        this.setSize(2000, 1500);
        setUpComponents();
        this.setVisible(true);
    }

    public void updateshow(int longeur, int largeur, int profondeur) {
        this.longeur=longeur;
        SwingUtilities.invokeLater(()-> fenetreAccueil.updateDisplayedDimensions());
        this.largeur=largeur;
        SwingUtilities.invokeLater(()-> fenetreAccueil.updateDisplayedDimensions());
        this.epaisseur=profondeur;
        SwingUtilities.invokeLater(()-> fenetreAccueil.updateDisplayedDimensions());
    }
    public ControleurLarman getControleurLarman() {
        return controleurLarman;
    }

    public boolean needsABorder()
    {
        return fenetreAffichee.getClass() == FenetreCoupe.class && fenetreCoupe.needsABorder();
    }

    public boolean needsAPoint()
    {
        return fenetreAffichee.getClass() == FenetreCoupe.class && fenetreCoupe.needsAPoint();
    }

    public void receiveSurvolPoint(double pointX,double pointY){
        fenetreCoupe.SurvolPoint(pointX,pointY);
    }

    public void receiveDimension(int dimX , int dimY){
            fenetreCoupe.DimSurvol(dimX,dimY);
    }


    void setUpComponents()
    {
        fenetreAccueil = new FenetreAccueil(this);
        fenetreAffichee = fenetreAccueil;
        fenetreInterdite = new FenetreInterdite(this);
        fenetreCoupe = new FenetreCoupe(this);
        fenetreOutil = new FenetreOutil(this);
        drawingPanel = new DrawingPanel(this);
        fenetrePanneau = new FenetrePanneau(this);
        fenetreSauvegarde = new FenetreSauvegarde(this);
        fenetreGrille = new FenetreGrille(this);
        cardLayout = new CardLayout();
        mainJpanel = new JPanel(cardLayout);
        this.add(mainJpanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //https://stackoverflow.com/questions/24724382/how-to-divide-a-frame-into-two-parts
        JSplitPane splitPane = new JSplitPane();
        splitPane.setSize(800, 750);
        splitPane.setDividerSize(0);
        splitPane.setDividerLocation(400);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainJpanel.add(fenetreAccueil, "entranceWindow");
        mainJpanel.add(fenetreCoupe, "cutWindow");
        mainJpanel.add(fenetreOutil, "toolsWindow");
        mainJpanel.add(fenetrePanneau,"panneauWindow");
        mainJpanel.add(fenetreSauvegarde,"saveWindow");
        mainJpanel.add(fenetreInterdite,"zoneinterditeWindow");
        mainJpanel.add(fenetreGrille,"grilleWindow");
        splitPane.setLeftComponent(mainJpanel);
        splitPane.setRightComponent(drawingPanel);
        add(splitPane);

    }

    public void addTool(String nom, int largeur, int position)
    {
        controleurLarman.ajouterOutil(nom, largeur, position);
        fenetreCoupe.updateToolsList();
    }

    public void deleteTool(String nom)
    {
        controleurLarman.supprimerOutil(nom);
        fenetreCoupe.updateToolsList();
    }

    /*public void makeBorderCutFromReference(boolean isVertical, boolean isStartOfDimension)
    {
        fenetreCoupe.receiveSingleReference(controleurLarman.makeBorderCutFromReference
                (isVertical, isStartOfDimension));
    }*/


    public  List<CoupeReadOnly> getListCoupe(){
       return controleurLarman.getCoupe();
    }

    public List<String> getToolsNames()
    {
        return controleurLarman.getToolsNames();
    }

    public void goTOPanneauWindow(){
        fenetreAffichee = fenetrePanneau;
        cardLayout.show(mainJpanel,"panneauWindow");
    }

    public void goToCutWindow()
    {
        fenetreAffichee = fenetreCoupe;
        cardLayout.show(mainJpanel, "cutWindow");
    }

    public void goToToolsWindow() {
        fenetreAffichee = fenetreOutil;
        cardLayout.show(mainJpanel, "toolsWindow");
    }

    public void goToEntranceWindow()
    {
        fenetreAffichee = fenetreAccueil;
        cardLayout.show(mainJpanel, "entranceWindow");
    }

    public void goToSaveWindow()
    {
        fenetreAffichee = fenetreSauvegarde;
        cardLayout.show(mainJpanel, "saveWindow");
    }
    public void goToGrilleWindow(){
        fenetreAffichee = fenetreGrille;
        cardLayout.show(mainJpanel,"grilleWindow");
    }

public void goToZoneWindow(){
        fenetreAffichee = fenetreInterdite;
        cardLayout.show(mainJpanel, "zoneinterditeWindow");
}
    public float getPanelLength()
    {
        return controleurLarman.getPanelLength();
    }

    public float getPanelHeight()
    {
        return controleurLarman.getPanelHeight();
    }

    public Point getTruePoint(Point point)
    {
        return controleurLarman.getTruePosition(point, drawingPanel.getWIDTH(), drawingPanel.getHEIGHT());
    }

    public Point getPointOnDrawnBoard(Point point)
    {
        return controleurLarman.getDrawnPosition(point, drawingPanel.getWIDTH(), drawingPanel.getHEIGHT());
    }

    public int getTrueHeight(int drawnHeight)
    {
        return controleurLarman.getTrueHeight(drawnHeight, drawingPanel.getHEIGHT());
    }

    public int getTrueWidth(int drawnWidth)
    {
        return controleurLarman.getTrueWidth(drawnWidth, drawingPanel.getWIDTH());
    }



    public CoupeReadOnly createBorderCut(int longeur, int largeur, float deepness ,String toolName){
        Dimension newDimension = new Dimension(longeur,largeur);
        CoupeReadOnly cut = controleurLarman.ajouterCoupeBordure(deepness,newDimension,toolName);
        rewdraw();
        return cut;}

    public CoupeReadOnly createRegularCut(int lengthAwayFromBorder, Line2D reference,
                                          float deepness, String toolName)
    {
        CoupeReadOnly cut = controleurLarman.ajouterCoupeReguliere(lengthAwayFromBorder, reference, deepness, toolName);
        rewdraw();
        return cut;
    }

    //Méthode qui crée une coupeL
    public  CoupeReadOnly createLCut(float profondeur , Point pointOrigine, Point pointOppose,CoupeReadOnly reference,
                                     Dimension dimension , String nomOutil) {

        CoupeReadOnly cut = controleurLarman.ajouterCoupeL(profondeur,pointOrigine,
                pointOppose,reference, dimension,nomOutil);
        rewdraw();
        return  cut;
    }

    //Méthode qui crée une coupeRectangulaire
    public  CoupeReadOnly createRectangularCut(float profondeur , Point pointOrigine, Point pointOppose, CoupeReadOnly reference,
                                               Dimension dimension , String nomOutil){

        CoupeReadOnly cut = controleurLarman.ajouterCoupeRectangulaire(profondeur,pointOrigine,
                pointOppose, reference, dimension,nomOutil);
        rewdraw();
        return cut;
    }

    public void eraseCut(CoupeReadOnly coupeReadOnly)
    {
        controleurLarman.supprimerCoupe(coupeReadOnly);
        rewdraw();
    }

    public CoupeReadOnly getNextCut()
    {
        CoupeReadOnly cut = controleurLarman.getNextCut();
        rewdraw();
        return cut;
    }



    public CoupeReadOnly getCurrentCut()
    {
        return controleurLarman.getCurrentCut();
    }
    public int getTaille_cellule(){
        return drawingPanel.getTaille_cellule();
    }
    public boolean getmag(){
        return drawingPanel.getmag();
    }
    public CoupeReadOnly modifyRegularCut(float deepness, float movement, CoupeReadOnly coupeReadOnly, String toolName)
    {
        CoupeReadOnly cut = controleurLarman.modifierCoupeReguliere( deepness, movement, coupeReadOnly, toolName);
        rewdraw();
        return cut;
    }

    public CoupeReadOnly modifierCoupeBordure(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName){
        CoupeReadOnly cut = controleurLarman.modifierCoupeBordure(deepness, coupeReadOnly, dimension, toolName);
        rewdraw();
        return cut;
    }

    /* public CoupeReadOnly modifierCoupeIrreguliere(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName, TypeCoupe typeCoupe)
    {
        CoupeReadOnly cut = controleurLarman.modifierCoupeIrreguliere(deepness, coupeReadOnly, dimension, toolName, typeCoupe);
        rewdraw();
        return cut;
    }*/

    public CoupeReadOnly modifierCoupeRectangulaire(float profondeur, Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension, String nomOutil, CoupeReadOnly coupe) {
        CoupeReadOnly cut = controleurLarman.modifierCoupeRectangulaire(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
        rewdraw();
        return cut;
    }

    public CoupeReadOnly modifierCoupeL(float profondeur, Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension, String nomOutil, CoupeReadOnly coupe) {
        CoupeReadOnly cut = controleurLarman.modifierCoupeL(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
        rewdraw();
        return cut;
    }

    public CoupeReguliere findCoupeReguliereUnderProjection(Point Origine,Point pointclickedOppose, List<CoupeReadOnly> coupeReadOnlyList) {
        if (pointclickedOppose == null || coupeReadOnlyList == null || coupeReadOnlyList.isEmpty()) {
            return null; // Aucun point ou liste vide
        }

        if (Origine.y > pointclickedOppose.y) {
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
        } else {
            Line2D projection = new Line2D.Double(
                    pointclickedOppose.x,
                    pointclickedOppose.y,
                    pointclickedOppose.x,
                    Integer.MIN_VALUE // Une ligne verticale infinie vers le haut
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
        }




        return null; // Aucune coupe régulière croisée
    }

    //Le dessinateur s'occuper de faire ça avec la liste de chaque objet... voir le Wiki Atelier 2
    public List<CoupeReadOnly> getAllCoupes()
    {
        return controleurLarman.getCoupe();
    }

    public void rewdraw()
    {
        drawingPanel.paintAgain();
    }

    //ajout des méthodes de MathsManageur:
    public Point getPouceEnMM(Point pointPixel){
        return controleurLarman.getPouceEnMM(pointPixel);
    }

    public Point.Float getPouceEnMMFloat(Point.Float pointPouce){
        return controleurLarman.getPouceEnMMFloat(pointPouce);
    }

    //ajout des méthodes de MathsManageur:
    public float getUneDimensionPouceEnMM(float dimension){
        return controleurLarman.getUneDimensionPouceEnMM(dimension);
    }



    //Méthode qui renvoie la fenetreCoupe
    public FenetreCoupe getFenetreCoupe(){
        return fenetreCoupe;
    }
    public FenetreInterdite getFenetreInterdite(){return fenetreInterdite;}

    public List<Line2D>getLines(){
        return controleurLarman.getLines();
    }
    public Line2D findLineNearPoint(List<Line2D> lines,Point point,double threshold){
        return controleurLarman.findLineNearPoint(lines,point,threshold);
    }
   // public  boolean isPointNearLine(Line2D lines , Point2D point ,double threshold){return controleurLarman.findLineNearPoint(lines, point,threshold);}
    public void deleteAllLines(){controleurLarman.deleteAllLines();}

    public void sendreference (Line2D line){
        fenetreCoupe.receiveSingleReference2(line);
}
   /*  public void sendreference(Line2D line){
        fenetreCoupe.receiveSingleReference2(line);
    }*/

    public void saveInFile(File file)
    {
        controleurLarman.saveInFile(file);
    }

    public void loadInFile(File file)
    {
        controleurLarman.loadInFile(file);
        fenetreCoupe.updateToolsList();
        rewdraw();
    }

    public void createGCodeFile(File file)
    {
        controleurLarman.createGCodeFile(file);
    }

    public boolean areAllCutsFine()
    {
        return controleurLarman.areAllCutsFine();
    }



    public ZoneInterditeReadOnly getLast(){
       return controleurLarman.getLast();
    }

    public JComponent getDrawingPanel() {
        return  drawingPanel;
    }


   /* public void sendPointClicked(Point pointclicked, Point pointclickedOppose) {
        fenetreCoupe.receivePointClicked(pointclicked, pointclickedOppose);
    }*/
}
