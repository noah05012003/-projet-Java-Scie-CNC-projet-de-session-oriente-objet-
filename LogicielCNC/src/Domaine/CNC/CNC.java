package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.*;
import java.io.IOException;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Coupes.CoupeReguliere;
import LogicielCNC.src.Domaine.Coupes.Coupes;
import LogicielCNC.src.Domaine.Coupes.TypeCoupe;
import LogicielCNC.src.Domaine.Factory.PanneauFactory;
import LogicielCNC.src.Domaine.Fichier.ProjetReadOnly;
import LogicielCNC.src.Domaine.Action;
import LogicielCNC.src.Domaine.Panneau;


import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CNC {
    private CoupesManageur coupesManageur;
    private OutilsManageur outilsManageur;
    private ProjetsManageur projetsManageur;
    private PanneauManager panneauManager;
    private MathsManageur mathsManageur;
    private SaveManager saveManager;
    private UndoRedoManager undoRedoManager;
    private PanneauFactory panneauFactory;
    private Panneau panneau;

    public CNC() {
        this.coupesManageur = new CoupesManageur(this);
        this.outilsManageur = new OutilsManageur(this);
        this.projetsManageur = new ProjetsManageur(this);
        this.panneauManager = new PanneauManager(this);
        this.mathsManageur = new MathsManageur(this);
        this.saveManager = new SaveManager(this);
        this.undoRedoManager = new UndoRedoManager();
        lines =new ArrayList<>();

    }
    private List<Line2D>lines;

    //Méthode pour retourner les outils en read-only
    public List<OutilReadOnly> getOutil() {
        return outilsManageur.getOutil();
    }

    public OutilReadOnly getToolByName(String name)
    {
        return outilsManageur.getToolByName(name);
    }

    //Méthode pour retourner les coupes en read-only
    public List<CoupeReadOnly> getCoupe(){
        return  CoupesManageur.getCoupe();
    }

    public List<ZoneInterditeReadOnly> getZoneInterdite(){
        return panneauManager.getZoneInterdite();
    }

    public PanneauReadOnly getPanneau(){
        return panneauManager.getPanneau();
    }

    public CoupeReadOnly getCurrentCut()
    {
        return coupesManageur.getCurrentCut();
    }

    public CoupesManageur getCoupesManageur() {
        return coupesManageur;
    }

    public CoupeReadOnly ajouterCoupeReguliere(int lengthAwayFromBorder, Line2D reference, float deepness, String toolName) {
        return coupesManageur.addRegularCut(deepness,reference,lengthAwayFromBorder,toolName);
    }



    public CoupeReadOnly ajouteCoupeL(float profondeur , Point pointOrigine,
                                      Point pointOppose, CoupeReadOnly reference, Dimension dimension , String nomOutil){
        return coupesManageur.ajouterCoupeL(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil);
    }


    public CoupeReadOnly ajouteCoupeRectangulaire(float profondeur , Point pointOrigine,
                                                  Point pointOppose, CoupeReadOnly reference,  Dimension dimension , String nomOutil){
        return coupesManageur.ajouterCoupeRectangulaire(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil);
    }


    public CoupeReadOnly ajouterCoupeBordure(float profondeur ,  Dimension newDimension, String toolName){
        return coupesManageur.ajouterCoupeBordure(profondeur, newDimension, toolName);
    }

    //A voir...
    public CoupeReadOnly modifierCoupeReguliere(float deepness, float movement, CoupeReadOnly coupeReadOnly, String toolName){
        return coupesManageur.modifierCoupeReguliere(deepness, movement, coupeReadOnly, toolName);
    }

    public CoupeReadOnly modifierCoupeBordure(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName){
        return coupesManageur.modifyBorderCut(deepness, coupeReadOnly, dimension, toolName);
    }


    public CoupeReadOnly modifierCoupeL(float profondeur, Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension, String nomOutil, CoupeReadOnly coupe){
        return coupesManageur.modifierCoupeL(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
    }

    public CoupeReguliere findCoupeReguliereUnderProjection(Point pointclickedOppose, List<CoupeReadOnly> coupeReadOnlyList) {
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
    public CoupeReadOnly modifierCoupeRectangulaire(float profondeur, Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension, String nomOutil, CoupeReadOnly coupe){
        return coupesManageur.modifierCoupeRectangulaire(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
    }

    //A voir...
    public boolean supprimerCoupe(Coupes coupe){
        return coupesManageur.supprimerCoupe(coupe);
    }

    public CoupeReadOnly getNextCut()
    {
        return coupesManageur.getNextCut();
    }

    public int getTrueHeight(int height, int heightDrawn)
    {
        return panneauManager.getTrueHeight(height, heightDrawn);
    }

    public int getTrueWidth(int width, int widthDrawn)
    {
        return panneauManager.getTrueWidth(width, widthDrawn);
    }

    //Call la classe Panneau pour créer l'objet
    public void ajouterZoneInterdite(Point2D positionXY, Dimension dimensionZone){
        panneauManager.ajouterZoneInterdite(positionXY, dimensionZone);
    }


    //Call la classe Panneau pour effectuer la modification
    public void modifierZoneInterdite(ZoneInterditeReadOnly zone, Dimension newdimension){
        panneauManager.modifierZoneInterdite(zone, newdimension);
    }

    public void supprimerZoneInterdite(ZoneInterditeReadOnly zoneInterdite){
        panneauManager.supprimerZoneInterdite(zoneInterdite);
    }

    public void ajouterPanneau(int largeur, int longueur , int epaisseur){
         panneauManager.ajouterPanneau(largeur, longueur, epaisseur);
    }

    public void ajouterOutil(String nom, int largeur , int position){
        outilsManageur.ajouterOutil(nom, largeur, position);
    }

    public List<String> getToolsNames()
    {
        return outilsManageur.getToolsNames();
    }

    public int getPanelLength()
    {
        return panneauManager.getPanelLength();
    }

    public int getPanelHeight()
    {
        return panneauManager.getPanelHeight();
    }

    public Point giveTruePosition(Point point, int lengthDrawn, int heightDrawn)
    {
        return panneauManager.giveTruePosition(point, lengthDrawn, heightDrawn);
    }

    public Point giveDrawnPosition(Point point, int lengthDrawn, int heightDrawn)
    {
        return mathsManageur.giveDrawnPosition(point, lengthDrawn, heightDrawn);
    }


    //ajout des méthodes de MathsManageur:
    public Point getPouceEnMM(Point pointPixel){
        return mathsManageur.convertPouceEnMM(pointPixel);
    }

    public Point.Float getPouceEnMMFloat(Point.Float pointPouce){
        return mathsManageur.convertPouceEnMMFloat(pointPouce);
    }

    //ajout des méthodes de MathsManageur:
    public float getUneDimensionPouceEnMM(float dimension){
        return mathsManageur.getUneDimensionPouceEnMM(dimension);
    }


    public void supprimerOutil(String nom)
    {
        outilsManageur.supprimerOutil(nom);
    }

    public List<ProjetReadOnly> getProjet(){
        return projetsManageur.getProjet();
    }

public Dimension getPanneauAdessiner(){
        return panneauManager.getPanneauAdessiner();
}
public PanneauReadOnly getLastPanneau(){return panneauManager.getLastPanneau();}


    public void DeleteAllCoupes(){
        coupesManageur.DeleteAllCoupes();
    }
    //public Projet charger(String monFichier){}
    //public void sauvegarder(String monFichier){}
    //public void ajouterDonnée(String donnée){}


    public Line2D findLineNearPoint(List<Line2D> lines,Point point, double threshold){
        Point2D point2D = new Point2D.Double(point.getX(), point.getY());
        for(Line2D line : lines){
            if (isPointNearLine(line,point2D,threshold)){
                return line;
            }
        }
        return null;
    }
    public boolean isPointNearLine(Line2D line,Point2D point,double threshold){
        return line.ptSegDist(point) <=threshold;}

    public List<Line2D> getLines(){return lines;}
    public void addLine(Line2D line ){
        lines.add(line);
    }
public boolean isLinePresent(Line2D line){
        for(Line2D line1 : lines){
            if (areLinesEqual(line1,line)){return true;}
        }
    return false;}
    private boolean areLinesEqual(Line2D line1,Line2D line2){
        return (line1.getX1()==line2.getX1()&& line1.getY1()==line2.getY1 () &&
                line1.getX2()==line2.getX2() && line1.getY2()==line2.getY2())||(
                line1.getX1()==line2.getX2()&& line1.getY1()==line2.getY2 () &&
                        line1.getX2()==line2.getX1() && line1.getY2()==line2.getY1()

                );
    }

    public void addSavedCut(CoupeReadOnly coupeReadOnly)
    {
        coupesManageur.addSavedCut(coupeReadOnly);
    }

    public void addSavedTool(OutilReadOnly outilReadOnly)
    {
        outilsManageur.addSavedTool(outilReadOnly);
    }

    public void replacePanelBySavedOne(PanneauReadOnly panneauReadOnly)
    {
        panneauManager.replacePanelBySavedOne(panneauReadOnly);
    }

    public void saveInFile(File file)
    {
        saveManager.saveInFile(file);
    }

    public void loadInFile(File file)
    {
        saveManager.loadInFile(file);
    }

    public void createGCodeFile(File file)
    {
        saveManager.createGCodeFile(file);
    }

    public boolean areAllCutsFine()
    {
        return coupesManageur.areAllCutsFine();
    }

    public void deleteAllLines(){
        if(lines != null){lines.clear();}
    }
    public boolean RegularConflict(CoupeReadOnly coupe){
        return panneauManager.RegularConflict(coupe);
}
    public boolean BordureConflict(CoupeReadOnly coupe){
        return panneauManager.BordureConflict(coupe);
    }
    public boolean Lconflict(CoupeReadOnly coupe){
        return panneauManager.Lconflict(coupe);
    }
    public boolean RectConflict(CoupeReadOnly coupe){
        return panneauManager.RectConflict(coupe);
    }

    public ZoneInterditeReadOnly getLast(){
        return panneauManager.getLast();
    }

    private void saveStateForUndo() {
        undoRedoManager.saveState(getPanneau());
    }

    public void undo() {
        PanneauReadOnly previousState = undoRedoManager.undo(getPanneau());
        if (previousState != null) {
            this.panneau = panneauFactory.createPanneauCopy(previousState);
        }
    }

    public void redo() {
        PanneauReadOnly nextState = undoRedoManager.redo();
        if (nextState != null) {
            this.panneau = panneauFactory.createPanneauCopy(nextState);
        }
    }

    public void savePanneau(String filePath) throws IOException {
        undoRedoManager.saveToFile(getPanneau(), filePath);
    }

    public void loadPanneau(String filePath) throws IOException, ClassNotFoundException {
        var loadedPanneau = undoRedoManager.loadFromFile(filePath);
        this.panneau = panneauFactory.createPanneauCopy(loadedPanneau);
    }


//    // Méthode pour ajouter une coupe
//    public void addCutAction(Coupes cut) {
//        Action addAction = new AddCutAction(); // Créer une action d'ajout
//        undoRedoManager.addAction(addAction);  // Ajouter l'action à l'historique
//        addAction.execute();  // Exécuter l'action (ajouter la coupe)
//    }

}


/*



    public void deleteAllLines() {
        if (lines != null) {
            lines.clear();  // Removes all lines from the list
        }
    }*/

