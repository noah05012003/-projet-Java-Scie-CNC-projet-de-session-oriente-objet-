package LogicielCNC.src.Vue;

import LogicielCNC.src.Vue.Affichage.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreGrille extends FenetreAffichee {
    private JButton returnButton;
    private JButton activerButton;
    private JButton desactiverButton;
    private JButton activerMagnetisme;
    private JSpinner spinnerTailleGrille;
    private boolean grilleActive = false;
    private boolean magnetisme = false;

    public FenetreGrille(FenetrePrincipale fenetrePrincipale) {
        super(fenetrePrincipale);
    }

    @Override
    protected void setUpComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Configuration du bouton retour
        returnButton = new JButton("Retour à l'accueil");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToEntranceWindow();
            }
        });

        // Configuration du spinner pour la taille de la grille
        spinnerTailleGrille = new JSpinner(new SpinnerNumberModel(20, 5, 100, 5));
        spinnerTailleGrille.setPreferredSize(new Dimension(1, 5));
        spinnerTailleGrille.addChangeListener(e -> {
            if (grilleActive) {
                updateGrilleSize((int)spinnerTailleGrille.getValue());
            }
        });

        // Configuration du bouton d'activation
        activerButton = new JButton("Activer la grille");
        activerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
                d.setMagnetisme(false);
                grilleActive = true;
                activerButton.setEnabled(false);
                desactiverButton.setEnabled(true);
                activerMagnetisme.setEnabled(true);
                activerGrille((int)spinnerTailleGrille.getValue());
            }
        });

        // Configuration du bouton de désactivation
        desactiverButton = new JButton("Désactiver la grille");
        desactiverButton.setEnabled(false);
        desactiverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
                d.setMagnetisme(false);
                grilleActive = false;
                activerButton.setEnabled(true);
                desactiverButton.setEnabled(false);
                activerMagnetisme.setEnabled(false);
                desactiverGrille();
            }
        });

        activerMagnetisme = new JButton("Activer magnetisme");
        activerMagnetisme.addActionListener(e -> {
            magnetisme = true;
            DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
            d.setGrilleMagnetique(true);
            activerMagnetisme.setEnabled(false);
            d.setMagnetisme(true); // Nouvelle méthode à ajouter dans DrawingPanel
            System.out.println("Magnétisme activé !");
        });


        // Ajout des composants à la fenêtre
        add(returnButton);
        add(new JLabel("Taille de la grille:"));
        add(spinnerTailleGrille);
        add(activerButton);
        add(activerMagnetisme);
        add(desactiverButton);
    }

    private void activerGrille(double taille) {
        // TODO: Implémenter l'activation de la grille
        DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
        int taille_px = convertirEnPixels(taille);
        d.setGrilleMagnetique(true);
        d.setTailleCellule(taille_px);
        d.repaint();
    }

    private void desactiverGrille() {
        // TODO: Implémenter la désactivation de la grille
        DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
        d.setGrilleMagnetique(false);
        d.repaint();
    }

    private void updateGrilleSize(double taille) {
        // TODO: Implémenter la mise à jour de la taille de la grille
        int taille_px = convertirEnPixels(taille);
        DrawingPanel d = (DrawingPanel)fenetrePrincipale.getDrawingPanel();
        d.setTailleCellule(taille_px);
        d.repaint();
    }

    private int convertirEnPixels(double pouces){
        final double PIXELS_PAR_POUCES = 2.54;
        return (int) (pouces * PIXELS_PAR_POUCES);
    }
}