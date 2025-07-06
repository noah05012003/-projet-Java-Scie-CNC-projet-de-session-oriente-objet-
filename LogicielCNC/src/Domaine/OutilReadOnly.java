package LogicielCNC.src.Domaine;

import java.io.Serializable;

public interface OutilReadOnly extends Serializable {
    public String getNom();
    public int getLargeur();
    public  int getPosition();
}
