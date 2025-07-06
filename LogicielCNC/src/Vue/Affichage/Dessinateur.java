package LogicielCNC.src.Vue.Affichage;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import LogicielCNC.src.Domaine.*;
import LogicielCNC.src.Domaine.Coupes.*;
import LogicielCNC.src.Vue.FenetreCoupe;
import LogicielCNC.src.Vue.FenetrePrincipale;

public class Dessinateur {
    private final ControleurLarman controleurLarman;
    private Dimension dimension;
   // private final int BIG_SIZE = 700;
    //final int SMALL_SIZE = 500;
    private int width = 0;
    private int height = 0;
    private DrawingPanel drawingPanel;

    public Dessinateur(ControleurLarman controleurLarman, Dimension dimension, DrawingPanel drawingPanel) {
        this.controleurLarman = controleurLarman;
        this.dimension = dimension;
        this.drawingPanel = drawingPanel;
    }


    //Méthode Chargée d'effectuer les dessins de nos objets
    public void dessine(Graphics g) {
        dessinePanneau(g);
        dessineZoneInterdite(g);
        dessineCoupe(g);
        dessineLine(g);
        dessineGrille(g);
    }




    public void setDimensions(Dimension newDimension) {
       this.dimension = newDimension;
    }

    //
    public void dessinePanneau(Graphics g) {
        Dimension panneau = controleurLarman.getPanneauAdessiner();
        this.height=panneau.height;
        this.width=panneau.width;

        if (panneau != null) {
            Graphics2D g2d = (Graphics2D) g;
            Color panneaucolor = new Color(139, 69, 19);
            g2d.setPaint(panneaucolor);
            double rectangleX = 0;
            double rectangleY = 0;
           // System.out.println("coucou les babies.");
            Rectangle2D rectangle = new Rectangle2D.Double(rectangleX, rectangleY, panneau.width, panneau.height);
            g2d.fill(rectangle);

            // Draw and check lines directly inside this method
            g2d.setColor(panneaucolor); // Set the line color to the same as the panneau

            // Define all four lines of the rectangle
            Line2D[] lines = new Line2D[]{
                    new Line2D.Double(rectangleX, rectangleY, rectangleX, rectangleY + panneau.height),  // Left side
                    new Line2D.Double(rectangleX + panneau.width, rectangleY, rectangleX + panneau.width, rectangleY + panneau.height),  // Right side
                    new Line2D.Double(rectangleX, rectangleY, rectangleX + panneau.width, rectangleY),  // Top side
                    new Line2D.Double(rectangleX, rectangleY + panneau.height, rectangleX + panneau.width, rectangleY + panneau.height)  // Bottom side
            };

            // Iterate over each line to check if it's already present
            for (Line2D line : lines) {
                if (!controleurLarman.isLinePresent(line)) {
                    //System.out.println("Adding line from (" + line.getX1() + ", " + line.getY1() + ") to (" + line.getX2() + ", " + line.getY2() + ")");
                    controleurLarman.addLine(line);
                }
            }

            // Optionally, set the rectangle for further use (e.g., storing it in the panel)
            drawingPanel.setRectangle(rectangle);

            //System.out.println("Rectangle X: " + rectangleX + ", Rectangle Y: " + rectangleY);
        }
        else {
            System.out.println("No panneau dimensions available.");
        }
}



    // \Méthode pour dessiner la zone interdite
    public void dessineZoneInterdite(Graphics g) {
        //On retourne la liste de coupe en read-only

        List<ZoneInterditeReadOnly> Zone = controleurLarman.getZoneInterdite();
        for (ZoneInterditeReadOnly zoneInterditeReadOnly : Zone) {
            Point2D position = zoneInterditeReadOnly.getPositionXY();
            Dimension dimension = zoneInterditeReadOnly.getDimensionZone();
            g.setColor(Color.BLACK);
            g.fillRect((int)position.getX()-(int)(dimension.width/2), (int)position.getY()-(int)(dimension.height/2), dimension.width, dimension.height);


           // Les zones interdites sont en rouge
        }
    }

    // Méthode pour dessiner la coupe
    public void dessineCoupe(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        FenetrePrincipale fenetrePrincipale = drawingPanel.getFenetrePrincipale();
        FenetreCoupe fenetreCoupe = fenetrePrincipale.getFenetreCoupe();


        //On retourne la liste de coupe en read-only
        List<CoupeReadOnly> coupeReadOnlyList = controleurLarman.getCoupe();

        for (CoupeReadOnly coupes : coupeReadOnlyList) {
            CoupeReadOnly reference = drawingPanel.getFenetrePrincipale().getFenetreCoupe().getSelectedReference();
            if(coupes == drawingPanel.getCurrentCut()) {
                if (coupes == reference)
                    graphics2D.setPaint(Color.GREEN);
                else
                    graphics2D.setPaint(Color.WHITE);
            }
            /*else if (coupes == drawingPanel.getFenetrePrincipale().getFenetreCoupe().getSelectedReference()) {
                graphics2D.setPaint(Color.GREEN);
            }*/
            else
            {
                if (coupes == reference)
                    graphics2D.setPaint(Color.GREEN);
                else
                    graphics2D.setPaint(Color.BLACK);
            }

            if (coupes.hasTool()){
                graphics2D.setStroke(new BasicStroke(coupes.getToolWidth()));
            }

            TypeCoupe typeCoupe = coupes.getType();

            if (typeCoupe == TypeCoupe.COUPE_REGULIERE) {

                Point pointOrigine = coupes.getPointOrigine();
                Point pointDestination = coupes.getPointDestination();

                pointOrigine = drawingPanel.getPointDrawnOnBoard(new Point(pointOrigine.x, pointOrigine.y));
                pointDestination = drawingPanel.getPointDrawnOnBoard(new Point(pointDestination.x, pointDestination.y));
                int horizontalBoost = (drawingPanel.getWidth() - width)/2;
                int verticalBoost = (drawingPanel.getHeight() - height)/2;
               // pointOrigine.x += horizontalBoost;
                //pointDestination.x += horizontalBoost;
               // pointOrigine.y += verticalBoost;
                //pointDestination.y += verticalBoost;

                //On dessine la coupe entre les deux points
                if (coupes.hasTool())
                {
                    graphics2D.setStroke(new BasicStroke(coupes.getToolWidth()));
                }
                Line2D line = new Line2D.Double(pointOrigine,pointDestination);
                if(controleurLarman.RegularConflict(coupes)){

                    graphics2D.setPaint(Color.RED);
                    float[] dashingPattern1 = {2f, 2f};
                    Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);

                    graphics2D.setStroke(stroke1);
                }

                if(! controleurLarman.isLinePresent(line)){
                    controleurLarman.addLine(line);
                }
                graphics2D.drawLine(pointOrigine.x, pointOrigine.y, pointDestination.x, pointDestination.y);

            //Cas coupe Bordure
            }else if(typeCoupe == TypeCoupe.COUPE_BORDURE){
                CoupeBordure coupeBordure =(CoupeBordure) coupes;
                Dimension newDimension =coupeBordure.getDimensionFinale();
                if(controleurLarman.BordureConflict(coupeBordure)){
                    System.out.println(" coucou les babies");
                    graphics2D.setPaint(Color.RED);
                    float[] dashingPattern1 = {2f, 2f};
                    Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);

                    graphics2D.setStroke(stroke1);
                }

                int rectWidth = newDimension.width;
                int rectHeight = newDimension.height;

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                int xCenter =(panelWidth-rectWidth)/2;
                int yCenter =(panelHeight-rectHeight)/2;

                if (coupes.hasTool()){
                    graphics2D.setStroke(new BasicStroke(coupes.getToolWidth()));
                }
                Line2D[]lines =new Line2D[] {
                        new Line2D.Double(xCenter,yCenter,xCenter+rectWidth,yCenter),
                        new Line2D.Double(xCenter,yCenter,xCenter,yCenter+rectHeight),
                        new Line2D.Double(xCenter+rectWidth,yCenter,xCenter+rectWidth,yCenter+rectHeight),
                        new Line2D.Double(xCenter,yCenter+rectHeight,xCenter+rectWidth,yCenter+rectHeight)
                };
                for(Line2D line : lines) {
                    graphics2D.draw(line);
                    if(!controleurLarman.isLinePresent(line)){
                        controleurLarman.addLine(line);
                    }
                }
            //Coupe en L
            } else if (typeCoupe == TypeCoupe.COUPE_L) {
                CoupeL coupeL = (CoupeL) coupes;
                Point pointOrigine = coupeL.getPoint();
                Point pointOppose = coupeL.getPointOppose();
                //Point point1 = new Point(pointOppose.x,pointOrigine.y);
                //Point point2 = new Point(pointOrigine.x,pointOppose.y);

                int invalide = 0;
                int invalide_outil = 0;

                int x1 = pointOrigine.x;
                int y1 = pointOrigine.y;
                int x2 = pointOppose.x;
                int y2 = pointOppose.y;

                if (controleurLarman.Lconflict(coupeL)) {
                    System.out.println(" coucou les babies");
                    graphics2D.setPaint(Color.RED);
                    float[] dashingPattern1 = {2f, 2f};
                    Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);

                    graphics2D.setStroke(stroke1);
                    //graphics2D.drawLine(x2,y2,x1,y2);
                    //graphics2D.drawLine(x2,y2,x2,y1);
                } else if (coupeL.getReference() != null) {
                    for (CoupeReadOnly coupeReadOnly : coupeReadOnlyList) {
                        if (coupeReadOnly.getType() == TypeCoupe.COUPE_REGULIERE) {
                            if (coupeL.getReference() == coupeReadOnly) {
                                invalide = 0;
                                break;
                            } else {
                                invalide = 1;
                            }
                        }
                    }
                }



                if (Objects.equals(coupeL.getToolName(), "outil par défaut")) {
                    invalide_outil = 0;
                } //else {
                    for (OutilReadOnly outilReadOnly1 : controleurLarman.getOutil()) {
                        if (Objects.equals(coupeL.getToolName(), outilReadOnly1.getNom())) {
                            invalide_outil = 0;
                            break;
                        } else {
                            invalide_outil = 1;
                        }
                    }
               // }

                if (invalide == 1 || invalide_outil == 1 ) {
                graphics2D.setPaint(Color.RED);
                float[] dashingPattern1 = {2f, 2f};
                Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
                graphics2D.setStroke(stroke1);
                //graphics2D.drawLine(x2,y2,x1,y2);
                //graphics2D.drawLine(x2,y2,x2,y1);
                }
                graphics2D.drawLine(x2,y2,x1,y2);
                graphics2D.drawLine(x2,y2,x2,y1);
            //Coupe rectangulaire
            }else if(typeCoupe == TypeCoupe.COUPE_RECTANGULAIRE) {
                CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) coupes;
                Point pointOrigine = coupeRectangulaire.getPoint();
                Point pointOppose = coupeRectangulaire.getPointOppose();

                Dimension dimension2 = new Dimension(coupeRectangulaire.getDimensionFinale());
                 System.out.println("Reference coupe rectangulaire" + coupeRectangulaire.getReference().getPoint() + " " + coupeRectangulaire.getReference().getPointOppose());
                 int invalide = 0;
                 int x1 = pointOrigine.x;
                 int y1 = pointOrigine.y;
                 int x2 = pointOppose.x;
                 int y2 = pointOppose.y;
                if(controleurLarman.RectConflict(coupeRectangulaire)){
                    System.out.println(" coucou les babies");
                    graphics2D.setPaint(Color.RED);
                    float[] dashingPattern1 = {2f, 2f};
                    Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
                    graphics2D.setStroke(stroke1);

                }

                if (Objects.equals(coupeRectangulaire.getReference().getToolName(), "depanne") ){
                    graphics2D.drawLine(x1,y1,x2,y1);
                    graphics2D.drawLine(x2,y1,x2,y2);
                    graphics2D.drawLine(x2,y2,x1,y2);
                    graphics2D.drawLine(x1,y2,x1,y1);



                }

                 else if (coupeRectangulaire.getReference() != null) {
                     for (CoupeReadOnly coupeReadOnly : coupeReadOnlyList) {
                         if (coupeReadOnly.getType() == TypeCoupe.COUPE_L) {
                             if (coupeRectangulaire.getReference() == coupeReadOnly) {
                                 invalide = 0;
                                 break;
                             } else {
                                 invalide = 1;
                             }
                         }
                     }
                 }
                 invalide = 1;
                 for (OutilReadOnly outilReadOnly : controleurLarman.getOutil()) {
                     if (outilReadOnly.getNom() == coupeRectangulaire.getToolName()) {
                            invalide = 0;
                     }
                 }

                 if (invalide == 1)
                 {
                     graphics2D.setPaint(Color.RED);
                     float[] dashingPattern1 = {2f, 2f};
                     Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,  BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
                     graphics2D.setStroke(stroke1);
                 }

                 graphics2D.drawLine(x1,y1,x2,y1);
                 graphics2D.drawLine(x2,y1,x2,y2);
                 graphics2D.drawLine(x2,y2,x1,y2);
                 graphics2D.drawLine(x1,y2,x1,y1);


            }
        }

    }




    //Logique de l'ajustement des points dans le panneau
    private  Point ajusterLaPosition(Point point){
        Point pointAjuster = drawingPanel.getPointDrawnOnBoard(new Point(point.x, point.y));
        int horizontalBoost = (drawingPanel.getWidth()-width)/2;
        int verticalBoost = (drawingPanel.getHeight()-height)/2;
        pointAjuster.x += horizontalBoost;
        pointAjuster.y += verticalBoost;

        return  pointAjuster;
    }

    //Afficher les points cliqués
    private  void dessinerPoint(Graphics2D g,Point point, Color couleur){
        Point pointAjuster = ajusterLaPosition(point);
        Color couleurOriginale = g.getColor();
        Stroke strokeOriginal = g.getStroke();

        g.setColor(couleur);
        int taille = 8;
        g.fillOval(pointAjuster.x - taille/2,pointAjuster.y - taille/2,taille,taille);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.drawOval(pointAjuster.x - taille/2,pointAjuster.y - taille/2,taille,taille);

        g.setColor(couleurOriginale);
        g.setStroke(strokeOriginal);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    public void dessineLine(Graphics g){
        Line2D line = drawingPanel.getSelectedLine();
        if (line!=null){
            double startX= line.getX1();
            double startY = line.getY1();
            double endX= line.getX2();
            double endY = line.getY2();
            double centerX=(startX + endX)/2;
            double centerY=(startY + endY)/2;

            double LineWidth = Math.abs(endX-startX);
            double LineHeight = Math.abs(endY-startY);
            double ellipseWidth = Math.max(LineWidth, 20);
            double ellipseHeight = Math.max(LineHeight, 20);
            double topLeftX =centerX - ellipseWidth/2;
            double topLeftY =centerY - ellipseHeight/2;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(1));
            //g2d.drawOval((int) topLeftX, (int) topLeftY, (int) ellipseWidth, (int) ellipseHeight);

        }
    }

    private void dessineGrille(Graphics g) {
        if (drawingPanel.isGrilleActive()){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.DARK_GRAY);
            int tailleCellule = drawingPanel.getTaille_cellule();

            int width = controleurLarman.getPanelLength();
            int height = controleurLarman.getPanelHeight();

            // Dessiner les lignes verticales
            for (int x = 0; x <= width; x += tailleCellule) {
                g2d.drawLine(x, 0, x, height);
            }

            // Dessiner les lignes horizontales
            for (int y = 0; y <= height; y += tailleCellule) {
                g2d.drawLine(0, y, width, y);
            }


        }
    }
}


