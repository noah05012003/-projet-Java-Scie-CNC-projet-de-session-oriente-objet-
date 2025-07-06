package LogicielCNC.src.Domaine.CNC;

import java.awt.*;
import java.util.ArrayList;

public class MathsManageur {
    private final float POUCES_EN_MM = 25.4f;
    private CNC cnc;

    public MathsManageur(CNC cnc){
        this.cnc = cnc;
    }

    // converti les pouces en MM
    public Point convertPouceEnMM(Point pointPixel) {
        double resolutionPPI = Toolkit.getDefaultToolkit().getScreenResolution(); // Utilise la résolution de l’écran pour calculer le nombre de pixels par pouce (par défaut souvent 96 PPI)

        double facteurConversion = POUCES_EN_MM / resolutionPPI; //Formule pour convertir
        //convertion
        int xMm = (int) (pointPixel.x * facteurConversion);
        int yMm = (int) (pointPixel.y * facteurConversion);

        return new Point(xMm, yMm);
    }

    //convertir les pouces en mm.float
    public Point.Float convertPouceEnMMFloat(Point.Float pointPixel) {

        double resolutionPPI = Toolkit.getDefaultToolkit().getScreenResolution();
        float facteurConversion = (float) (POUCES_EN_MM / resolutionPPI);
        float xMm = pointPixel.x * facteurConversion;
        float yMm = pointPixel.y * facteurConversion;

        return new Point.Float(xMm, yMm);
    }

    public float getUneDimensionPouceEnMM(float dimension){
        return dimension * POUCES_EN_MM;
    }

    public Point giveDrawnPosition(Point point, int lengthDrawn, int heightDrawn)
    {
        float multiplicator = (float) lengthDrawn/ cnc.getPanelLength();
        point.x = (int) (point.x * multiplicator);
        multiplicator = (float) heightDrawn/ cnc.getPanelHeight();
        point.y = (int) (point.y * multiplicator);
        return point;
    }
}
