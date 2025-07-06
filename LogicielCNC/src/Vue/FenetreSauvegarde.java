package LogicielCNC.src.Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FenetreSauvegarde extends FenetreAffichee {
    private  JButton returnButton;

    private JFileChooser jFileChooser;
    private JButton saveButton;
    private JButton loadButton;
    private JButton gcodeButton;
    private JLabel statusLabel;

    public FenetreSauvegarde(FenetrePrincipale fenetrePrincipale1) {
        super(fenetrePrincipale1);
    }

    @Override
    protected void setUpComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        jFileChooser = new JFileChooser();
        returnButton = new JButton("Retour à l'accueil");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToEntranceWindow();
            }
        });
        saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInFiles();
            }
        });
        loadButton = new JButton("Charger un Fichier");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });
        gcodeButton = new JButton("Exporter un Gcode");
        gcodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gcodeFunction();
            }
        });

        for (JButton button : new JButton[]{returnButton, saveButton, loadButton, gcodeButton}) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(button);
            buttonPanel.add(Box.createHorizontalGlue());
            this.add(buttonPanel);

            // Ajouter un espacement vertical entre les boutons
            Dimension dimension = new Dimension(0,10);
            this.add(Box.createRigidArea(dimension));
        }
        this.statusLabel = new JLabel();
        this.add(returnButton);
        this.add(saveButton);
        this.add(loadButton);
        this.add(gcodeButton);
        this.add(statusLabel);
    }

    private void saveInFiles()
    {
        if(fenetrePrincipale.areAllCutsFine())
        {
            if(jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                File file = jFileChooser.getSelectedFile();
                fenetrePrincipale.saveInFile(file);
                statusLabel.setText("Sauvegarde réussie");
            }
        }
        else
        {
            statusLabel.setText("Impossible de sauvegarder tant qu'il y a une coupe invalide");
        }
    }

    private void loadFile()
    {
        if(jFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = jFileChooser.getSelectedFile();
            if(file.getName().endsWith(".cnc"))
            {
                fenetrePrincipale.loadInFile(file);
            }
            statusLabel.setText("Type de fichier impossible");
        }
    }

    private void gcodeFunction()
    {
        if(fenetrePrincipale.areAllCutsFine())
        {
            if(jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                File file = jFileChooser.getSelectedFile();
                fenetrePrincipale.createGCodeFile(file);
                statusLabel.setText("Création GCode réussi");
            }
        }
        else
        {
            statusLabel.setText("Impossible de créer GCode tant qu'il y a une coupe invalide");
        }
    }
}
