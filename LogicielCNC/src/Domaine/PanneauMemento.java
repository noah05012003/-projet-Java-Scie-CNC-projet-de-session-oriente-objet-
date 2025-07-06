package LogicielCNC.src.Domaine;

import java.io.Serializable;

public class PanneauMemento implements Serializable {
    private final ControleurLarman originator;
    private final Panneau state;

    public PanneauMemento(ControleurLarman originator, Panneau state) {
        this.originator = originator;
        this.state = state;
    }

}
