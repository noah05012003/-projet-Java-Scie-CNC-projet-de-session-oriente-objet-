package LogicielCNC.src.Domaine;
import LogicielCNC.src.Domaine.CNC.CNC;
import LogicielCNC.src.Domaine.CNC.OutilsManageur;
import LogicielCNC.src.Domaine.CNC.PanneauManager;
import LogicielCNC.src.Domaine.CNC.UndoRedoManager;
import LogicielCNC.src.Domaine.Coupes.CoupeReadOnly;
import LogicielCNC.src.Domaine.Coupes.CoupeReguliere;
import LogicielCNC.src.Domaine.Coupes.Coupes;
import LogicielCNC.src.Domaine.Coupes.TypeCoupe;
import LogicielCNC.src.Domaine.Factory.PanneauFactory;
import LogicielCNC.src.Domaine.Fichier.ProjetReadOnly;
import LogicielCNC.src.Vue.Affichage.Dessinateur;
import  LogicielCNC.src.Vue.FenetrePrincipale;


import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ControleurLarman {

     private final CNC cnc;
     private final FenetrePrincipale fenetrePrincipale;
     private PanneauReadOnly panneau;
//     private final PanneauFactory panneauFactory;
//     private final OutilsManageur outilsManageur;
//     private final PanneauManager panneauManager;
     private UndoRedoManager undoRedoManager;
     private PanneauFactory panneauFactory;

     //private Projet projetCNC;

     //Constructeur
     public ControleurLarman() {
         //this.panneauManager = panneauManager;
         //this.undoRedoManager = undoRedoManager;
         //this.panneauFactory = panneauFactory;
         cnc = new CNC();
          fenetrePrincipale = new FenetrePrincipale(this);

     };

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

     public int getTrueHeight(int height, int heightDrawn)
     {
          return cnc.getTrueHeight(height, heightDrawn);
     }

     public int getTrueWidth(int width, int widthDrawn)
     {
          return cnc.getTrueWidth(width, widthDrawn);
     }

     public Point getTruePosition(Point point, int lengthDrawn, int heightDrawn)
     {
          return cnc.giveTruePosition(point,lengthDrawn,heightDrawn);
     }

     public Point getDrawnPosition(Point point, int lengthDrawn, int heightDrawn)
     {
          return cnc.giveDrawnPosition(point,lengthDrawn,heightDrawn);
     }

     //Récupère les objets Outil en read-only
     public List<OutilReadOnly> getOutil(){
          return cnc.getOutil();
     }

     public int getPanelLength()
     {
          return cnc.getPanelLength();
     }

     public int getPanelHeight()
     {
          return cnc.getPanelHeight();
     }

     public PanneauReadOnly getPanneau(){
          return cnc.getPanneau();
     }

     //Retourne les objets coupe en read-only
     public List<CoupeReadOnly> getCoupe()
     {
          return  cnc.getCoupe();
     }

     /*public CoupeReadOnly makeBorderCutFromReference(boolean isVertical, boolean isStartOfDimension)
     {
          return cnc.makeBorderCutFromReference(isVertical, isStartOfDimension);
     }

     public List<CoupeReadOnly> makeBorderCutsFromCornerReference(Point point)
     {
          return cnc.makeBorderCutsFromCornerReference(point);
     }*/

     //Retourne la coupe courante
     public CoupeReadOnly getCurrentCut()
     {
          return cnc.getCurrentCut();
     }

     //On doit récupérer les coordonnées de la zone à créer.... A modifier
     public void ajouterZoneInterdite(Point2D positionXY , Dimension dimensionZone)
     {
          cnc.ajouterZoneInterdite(positionXY,dimensionZone);
     }
     public void SupprimerZone(ZoneInterditeReadOnly zone){
          cnc.supprimerZoneInterdite(zone);
     }
     //A modifier...on doit récupérer  l'objet zoneInterdite dans la liste des zones et modifier ses paramètres
     public void modifierZoneInterdite(ZoneInterditeReadOnly zone, Dimension dimensionZone)
     {
               cnc.modifierZoneInterdite(zone,dimensionZone);
     }


     public CoupeReadOnly modifierCoupeReguliere(float deepness, float movement,
                                        CoupeReadOnly coupeReadOnly, String toolName){
          return cnc.modifierCoupeReguliere(deepness, movement, coupeReadOnly, toolName);
     }

     public CoupeReadOnly modifierCoupeBordure(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName){
          return cnc.modifierCoupeBordure(deepness, coupeReadOnly, dimension, toolName);
     }

     /*public CoupeReadOnly modifierCoupeIrreguliere(float deepness, CoupeReadOnly coupeReadOnly, Dimension dimension, String toolName, TypeCoupe typeCoupe)
     {
          return cnc.modifierCoupeIrreguliere(deepness, coupeReadOnly, dimension, toolName, typeCoupe);
     }*/

     public CoupeReadOnly modifierCoupeL(float profondeur, Point pointOrigine, Point pointOppose, CoupeReadOnly reference, Dimension dimension, String nomOutil, CoupeReadOnly coupe){
          return cnc.modifierCoupeL(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
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
          return cnc.modifierCoupeRectangulaire(profondeur, pointOrigine, pointOppose, reference, dimension, nomOutil, coupe);
        }

     //Méthode qui supprime une coupe dans la liste de coupe
     public void supprimerCoupe(CoupeReadOnly coupeReguliere) {

          cnc.supprimerCoupe((Coupes) coupeReguliere);
     }

     public CoupeReadOnly getNextCut()
     {
          return cnc.getNextCut();
     }

     public void ajouterOutil(String nom , int largeur , int position)
     {

          cnc.ajouterOutil(nom,largeur,position);
     }

     public List<String> getToolsNames()
     {
          return cnc.getToolsNames();
     }

     public void ajouterPanneau(int largeur, int longueur, int epaisseur)
     {
          cnc.ajouterPanneau(largeur, longueur, epaisseur);
     }

     public List<ZoneInterditeReadOnly> getZoneInterdite(){
          return cnc.getZoneInterdite();
     }

     //Récupère les objets projet en read-only
     public List<ProjetReadOnly> getProjet()
     {
          return  cnc.getProjet();
     }

     //Méthode qui supprimer l'outil dans la liste d'outil par l'utilisateur
     public void supprimerOutil(String nom)
     {

          cnc.supprimerOutil(nom);
     }

     public CoupeReadOnly ajouterCoupeReguliere(int lengthAwayFromBorder, Line2D reference,
                                                float deepness, String toolName) {
          return cnc.ajouterCoupeReguliere(lengthAwayFromBorder,reference,deepness,toolName);
     }

     public CoupeReadOnly ajouterCoupeL(float profondeur , Point pointOrigine,
                                        Point pointOppose, CoupeReadOnly reference, Dimension dimension , String nomOutil){
          return cnc.ajouteCoupeL(profondeur,pointOrigine,pointOppose, reference, dimension,nomOutil);
     }

     public CoupeReadOnly ajouterCoupeRectangulaire(float profondeur , Point pointOrigine,
                                                    Point pointOppose, CoupeReadOnly reference, Dimension dimension , String nomOutil){
          return cnc.ajouteCoupeRectangulaire(profondeur,pointOrigine,pointOppose, reference, dimension,nomOutil);
     }

     public CoupeReadOnly ajouterCoupeBordure(float profondeur ,  Dimension newDimnesion, String toolName){
          return cnc.ajouterCoupeBordure(profondeur,newDimnesion,toolName);
     }

     public Dimension getPanneauAdessiner()
     {
          return cnc.getPanneauAdessiner();
     }

     //ajout des méthodes de MathsManageur:
     public Point getPouceEnMM(Point pointPixel){
          return cnc.getPouceEnMM(pointPixel);
     }

     public Point.Float getPouceEnMMFloat(Point.Float pointPouce){
          return cnc.getPouceEnMMFloat(pointPouce);
     }

     //ajout des méthodes de MathsManageur:
     public float getUneDimensionPouceEnMM(float dimension){
          return cnc.getUneDimensionPouceEnMM(dimension);
     }
      public void DeleteAllCoupes(){

          cnc.DeleteAllCoupes();
      }

     public boolean areAllCutsFine()
     {
          return cnc.areAllCutsFine();
     }


     public Line2D findLineNearPoint(List<Line2D> lines,Point point,double threshold){
          return cnc.findLineNearPoint(lines,point,threshold);
     }
     /*public boolean isLinePresent(Line2D line){
          return cnc.isLinePresent(line);}*/
     public boolean isLinePresent(Line2D line){
             return cnc.isLinePresent(line);}


     public void addLine (Line2D line){
          cnc.addLine(line);

     }
     public List<Line2D> getLines(){return cnc.getLines();}
     //  public  boolean isPointNearLine(Line2D lines , Point2D point , double threshold){return cnc.findLineNearPoint(lines, point,threshold);}
     public void deleteAllLines(){cnc.deleteAllLines();
     }

     /* public void addLine(Line2D line) {
          cnc.addLine(line);
     }
     public List<Line2D> getLines() {
          return cnc.getLines();
     }*/

     public void saveInFile(File file)
     {
          cnc.saveInFile(file);
     }

     public void loadInFile(File file)
     {
          cnc.loadInFile(file);
     }

     public void createGCodeFile(File file)
     {
          cnc.createGCodeFile(file);
     }

     public boolean RegularConflict(CoupeReadOnly coupe){
          return cnc.RegularConflict(coupe);
     }
     public boolean BordureConflict(CoupeReadOnly coupe){
          return cnc.BordureConflict(coupe);
     }
     public boolean Lconflict(CoupeReadOnly coupe){
          return cnc.Lconflict(coupe);
     }
     public boolean RectConflict (CoupeReadOnly coupe){
          return cnc.RectConflict(coupe);
     }
     public ZoneInterditeReadOnly getLast(){
          return cnc.getLast();}






}


