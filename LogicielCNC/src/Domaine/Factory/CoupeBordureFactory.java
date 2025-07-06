package LogicielCNC.src.Domaine.Factory;

import LogicielCNC.src.Domaine.Coupes.CoupeBordure;
import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;

//Pas avec les coupes irrégulières, demandent des paramaètres différents
public class CoupeBordureFactory {

    public CoupeBordure createCoupeBordure(float profondeur , Dimension newDimension, OutilReadOnly tool)
    {
        return new CoupeBordure(profondeur,  newDimension, tool);
    }
}
