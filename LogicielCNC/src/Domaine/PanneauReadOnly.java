package LogicielCNC.src.Domaine;

import java.io.Serializable;

public interface PanneauReadOnly extends Serializable {
    public int getLargeur();
    public int getLongueur();
    public int getEpaisseur();
}
