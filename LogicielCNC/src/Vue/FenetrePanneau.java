package LogicielCNC.src.Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetrePanneau extends FenetreAffichee{
    private JPanel PanelPanneau;
    private JLabel LongeurLabel;
    private JLabel LargeurLabel;
    private JLabel profondeurLabel;
    private JTextField longeurPanneau;
    private JTextField largeurPanneau;
    private JTextField profondeurPanneau;

    private JButton CreerPanneau;
    private JButton retourAcceuil;
    public FenetrePanneau(FenetrePrincipale fenetrePrincipale1) {
        super(fenetrePrincipale1);

    }

 public void setUpComponents(){
     PanelPanneau = new JPanel();
     PanelPanneau.setLayout(new BoxLayout(PanelPanneau, BoxLayout.PAGE_AXIS));
     retourAcceuil = new JButton("Retour à l'accueil");
     retourAcceuil.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             fenetrePrincipale.goToEntranceWindow();
         }
     });
     LongeurLabel = new JLabel("Insérez la largeur ");
     longeurPanneau = new JTextField();
     PanelPanneau.add(LongeurLabel);
     PanelPanneau.add(longeurPanneau);


     LargeurLabel = new JLabel("Insérez la longueur ");
     largeurPanneau = new JTextField();
     PanelPanneau.add(LargeurLabel);
     PanelPanneau.add(largeurPanneau);


     profondeurLabel = new JLabel("Insérez la profondeur ");
     profondeurPanneau = new JTextField();
     PanelPanneau.add(profondeurLabel);
     PanelPanneau.add(profondeurPanneau);


     CreerPanneau = new JButton  ("Créer le Panneau");
     CreerPanneau.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             String inputLongueur = longeurPanneau.getText();
             String inputLargeur = largeurPanneau.getText();
             String inputProfondeur = profondeurPanneau.getText();
             if (!inputLongueur.isEmpty() && !inputLargeur.isEmpty() && !inputProfondeur.isEmpty() ) {
                try {
                    int longueur =Integer.parseInt(inputLongueur);

                    int largeur =Integer.parseInt(inputLargeur);
                    int profondeur =Integer.parseInt(inputProfondeur);
                    //if (longueur > 0 &&   longueur < 1000 && largeur > 0 && largeur < 1000 && profondeur > 0 && profondeur < 100)
                    if (longueur > 0 && largeur > 0 && profondeur > 0 && profondeur < 100) {
                        fenetrePrincipale.getControleurLarman().ajouterPanneau(longueur,largeur,profondeur);
                        fenetrePrincipale.updateshow(longueur,largeur,profondeur);
                        fenetrePrincipale.repaint();
                        fenetrePrincipale.getControleurLarman().DeleteAllCoupes();
                    }else {
                        JOptionPane.showMessageDialog(PanelPanneau,"Entrer un numéro positive et < 1000","entree invalide", JOptionPane.ERROR_MESSAGE );
                    }}
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(PanelPanneau, "Entrer des valeurs valides", "entree invalide", JOptionPane.ERROR_MESSAGE);

                    }}
                    else{
                        JOptionPane.showMessageDialog(PanelPanneau, "Remplir tous les champs", " entree manquante", JOptionPane.WARNING_MESSAGE);


                }
             }

     });

     PanelPanneau.add(CreerPanneau);
     PanelPanneau.add(retourAcceuil);

     add(PanelPanneau);
 }
}
