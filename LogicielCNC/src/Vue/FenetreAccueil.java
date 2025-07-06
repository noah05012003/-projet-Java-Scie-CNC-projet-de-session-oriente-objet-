package LogicielCNC.src.Vue;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreAccueil extends FenetreAffichee {
    private JPanel pannelDimension;
    private JLabel lengthText;
    private JLabel heightText;

    public FenetreAccueil(FenetrePrincipale fenetrePrincipale1) {
        super(fenetrePrincipale1);
    }


    protected void setUpComponents()
    {
        setLayout(new GridLayout(2,1));
        setUpDimensionPanel();
        setUpPannelButtons();
    }

    private void setUpDimensionPanel()
    {

        pannelDimension = new JPanel();

        pannelDimension.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(5,5,5,5)));
        Font labelFontTitle = new Font("Arial",Font.BOLD,13);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Dimensions du panneau 📏");
        titledBorder.setTitleFont(labelFontTitle);
        pannelDimension.setBorder(BorderFactory.createTitledBorder(titledBorder));


        pannelDimension.setLayout(new BoxLayout(pannelDimension,BoxLayout.Y_AXIS));
        pannelDimension.setSize(1000,100);
        pannelDimension.setBackground(new Color(135,206,235));
        pannelDimension.setLayout(new GridLayout(3,1));
        Font labelFont = new Font("Arial",Font.BOLD,12);
        lengthText = new JLabel("Largeur: " + fenetrePrincipale.getPanelLength() + "mm");
        heightText = new JLabel("Longueur: " + fenetrePrincipale.getPanelHeight() + "mm");
        lengthText.setFont(labelFont);
        heightText.setFont(labelFont);
        pannelDimension.add(Box.createVerticalStrut(5));
        pannelDimension.add(lengthText);
        pannelDimension.add(heightText);
        add(pannelDimension);
    }



    private void setUpPannelButtons()
    {
        JPanel pannelButtons = new JPanel();
        pannelButtons.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(5,5,5,5)));
        pannelButtons.setSize(1000,100);
        pannelButtons.setBackground(new Color(135,206,235));
        pannelButtons.setLayout(new GridLayout(3,2));
        JButton cutButton = new JButton("Créer des coupes 🪚");
        cutButton.addActionListener(e -> fenetrePrincipale.goToCutWindow());

        JButton newBoardButton = new JButton("Créer un Panneau 🟫");
        newBoardButton.addActionListener(e -> fenetrePrincipale.goTOPanneauWindow());
        JButton forbiddenZonesButton = new JButton("Ajouter des zones interdites 🚫");
        JButton toolsButton = new JButton("Créer des outils 🧰");
        toolsButton.addActionListener(e -> fenetrePrincipale.goToToolsWindow());
        JButton gridButton = new JButton("Grille Magnétique 🧲");
        gridButton.addActionListener(e -> fenetrePrincipale.goToGrilleWindow());
        newBoardButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {fenetrePrincipale.goTOPanneauWindow();}

        });

        toolsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToToolsWindow();
            }
        });
        JButton saveButton = new JButton("Sauvegarder 💾");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToSaveWindow();
            }
        });


        forbiddenZonesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToZoneWindow();
            }
        });




        pannelButtons.add(cutButton);
        pannelButtons.add(newBoardButton);
        pannelButtons.add(forbiddenZonesButton);
        pannelButtons.add(toolsButton);
        pannelButtons.add(gridButton);
        pannelButtons.add(saveButton);
        add(pannelButtons);


    }

    public void updateDisplayedDimensions() {
        lengthText.setText("Largeur: " + fenetrePrincipale.getPanelLength());
        heightText.setText("Longueur: " + fenetrePrincipale.getPanelHeight());

        pannelDimension.revalidate();
        pannelDimension.repaint();  // Ensures the panel refreshes immediately
    }


}
