package LogicielCNC.src.Vue;

import LogicielCNC.src.Domaine.ZoneInterditeReadOnly;

import javax.management.StringValueExp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

public class FenetreInterdite extends FenetreAffichee {
    private Point2D centerpoint;
    private JPanel ZonePannel;
    private JTextField center_x;
    private JTextField center_y;
    private JLabel center;
    private JLabel longeur;
    private JLabel largeur;
    private JTextField longeurz;
    private JTextField largeurz;
    private  JButton  creezone;
    private JButton ajouterzone;
    private JButton retour;
    private double point_x;
    private double point_y;
    private JButton SupprimerZone;
    private  int areacount =1;
    private JLabel retaillerLongueruLabel;
    private JLabel retaillargeurLabel;
    private JTextField retaillerLongeurField;
    private JTextField retaillargeurField;
    private JButton retaillerZone;



    private JComboBox<ZoneInterditeReadOnly>ZoneBox;
    private ZoneInterditeReadOnly current;


    public FenetreInterdite(FenetrePrincipale fenetrePrincipale1) {
        super(fenetrePrincipale1);

    }
    public void setUpComponents(){

        ZoneBox = new JComboBox<>();
        ZonePannel = new JPanel();
        ZonePannel.setLayout(new BoxLayout(ZonePannel, BoxLayout.PAGE_AXIS));
        retour = new JButton("Retour à l'accueil");
        creezone = new JButton("Créer une zone interdite");
        retour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToEntranceWindow();
            }
        });

        center = new JLabel("Coordonnées du centre");
        center_x = new JTextField();
        center_y = new JTextField();
        longeur = new JLabel("Entrez la longueur de votre zone");
        longeurz = new JTextField();
        largeur = new JLabel("Entrez la largeur de votre zone");
        largeurz = new JTextField();
        ajouterzone = new JButton("Ajouter");
        SupprimerZone = new JButton("Supprimer la zone interdite");
        retaillerLongueruLabel=new JLabel("Nouvelle longueur");
        retaillargeurLabel=new JLabel("Nouvelle largeur");
        retaillerLongeurField=new JTextField();
        retaillargeurField=new JTextField();
        retaillerZone = new JButton("Retailler");

        retaillerLongeurField.setVisible(false);
        retaillargeurField.setVisible(false);
        retaillerLongueruLabel.setVisible(false);
        retaillargeurLabel.setVisible(false);
        retaillerZone.setVisible(false);

        center.setVisible(false);
        center_x.setVisible(false);
        center_y.setVisible(false);
        longeur.setVisible(false);
        longeurz.setVisible(false);
        largeur.setVisible(false);
        largeurz.setVisible(false);
        ajouterzone.setVisible(false);
        SupprimerZone.setVisible(false);
        ZoneBox.setVisible(false);

        ZonePannel.add(retour);
        ZonePannel.add(center);

        ZonePannel.add(center_x);
        ZonePannel.add(center_y);
        ZonePannel.add(largeur);
        ZonePannel.add(largeurz);
        ZonePannel.add(longeur);
        ZonePannel.add(longeurz);
        ZonePannel.add(creezone);
        ZonePannel.add(ajouterzone);
        ZonePannel.add(ZoneBox);
        ZonePannel.add(SupprimerZone);
        ZonePannel.add(retaillerLongueruLabel);
        ZonePannel.add(retaillerLongeurField);
        ZonePannel.add(retaillargeurLabel);
        ZonePannel.add(retaillargeurField);
        ZonePannel.add(retaillerZone);

        this.add(ZonePannel);
        SupprimerZone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = (ZoneInterditeReadOnly) ZoneBox.getSelectedItem();
                if(current != null){
                    ZoneBox.removeItem(current);
                    fenetrePrincipale.getControleurLarman().SupprimerZone(current);
                    fenetrePrincipale.repaint();
                }
                else{
                    JOptionPane.showMessageDialog(
                            FenetreInterdite.this,
                            "Veuillez sélectionner une zone à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        creezone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makevisible(true);
            }
        });

        ajouterzone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // deleteing();
                String inputLongeur = longeurz.getText();
                String inputLargeur = largeurz.getText();
                float length = Float.parseFloat(inputLongeur);
                float width = Float.parseFloat(inputLargeur);




                fenetrePrincipale.getControleurLarman().ajouterZoneInterdite(centerpoint, new Dimension((int)length,(int)width));
                ZoneBox.addItem(fenetrePrincipale.getLast());
                fenetrePrincipale.repaint();
                deleteing();
                resetdata();
            }
        });

        retaillerZone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = (ZoneInterditeReadOnly) ZoneBox.getSelectedItem();
                if (current != null) {
                    String newLengthText = retaillerLongeurField.getText();
                    String newWidthText = retaillargeurField.getText();

                    try {
                        int newLength = Integer.parseInt(newLengthText);
                        int newWidth = Integer.parseInt(newWidthText);

                        fenetrePrincipale.getControleurLarman().modifierZoneInterdite(current, new Dimension(newLength, newWidth));
                        fenetrePrincipale.repaint();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                FenetreInterdite.this,
                                "Veuillez entrer des dimensions valides.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            FenetreInterdite.this,
                            "Veuillez sélectionner une zone à retailler.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });





    }
    public void receivezonecenter(Point2D centerpoint){

        point_x = centerpoint.getX();
        point_y = centerpoint.getY();
        center_x.setText(String.valueOf(point_x));
        center_y.setText(String.valueOf(point_y));
                //return centerpoint;
        this.centerpoint=centerpoint;
    }
    public void makevisible(boolean visible){
        creezone.setVisible(false);
        center.setVisible(true);
        center_x.setVisible(true);
        center_y.setVisible(true);
        longeur.setVisible(true);
        longeurz.setVisible(true);
        largeur.setVisible(true);
        largeurz.setVisible(true);
        ajouterzone.setVisible(true);

    }
    public void resetdata(){
        creezone.setVisible(true);
        center.setVisible(false);
        center_x.setVisible(false);
        center_x.setText("");
        center_y.setVisible(false);
        center_y.setText("");
        longeur.setVisible(false);
        longeurz.setVisible(false);
        longeurz.setText("");
        largeur.setVisible(false);
        largeurz.setVisible(false);
        largeurz.setText("");
        ajouterzone.setVisible(false);
    }
    public void deleteing() {
        // Show components for selecting and deleting zones
        ZoneBox.setVisible(true);
        SupprimerZone.setVisible(true);
        retaillerLongueruLabel.setVisible(true);
        retaillargeurLabel.setVisible(true);
        retaillerLongeurField.setVisible(true);
        retaillargeurField.setVisible(true);
        retaillerZone.setVisible(true);
    }
}
