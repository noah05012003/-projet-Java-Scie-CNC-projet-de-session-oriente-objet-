package LogicielCNC.src.Vue;

import javax.swing.*;

public abstract class FenetreAffichee extends JPanel {
    protected FenetrePrincipale fenetrePrincipale;

    public FenetreAffichee(FenetrePrincipale fenetrePrincipale1){
        fenetrePrincipale = fenetrePrincipale1;
        setSize(400,750);
        setUpComponents();
        this.setVisible(true);
    }

    protected abstract void setUpComponents();
}
