package LogicielCNC.src.Vue;

import LogicielCNC.src.Domaine.Outil;
import LogicielCNC.src.Domaine.OutilReadOnly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreOutil extends FenetreAffichee{
    private JPanel panelOutil;
    private JTextField nomOutil;
    private JTextField largeurOutil;
    private  JTextField positionOutil;
    private JLabel statutLabel;
    private JLabel outilLabel;

    private JComboBox<OutilReadOnly> outilComboBox;
    private DefaultComboBoxModel<OutilReadOnly> outilComboBoxModel;

    private JPanel pannelButton;

    private  JButton creerOutil;
    private  JButton retourAccueil;
    private JButton supprimerOutil;

    public FenetreOutil(FenetrePrincipale fenetrePrincipale1){
        super(fenetrePrincipale1);
    }
    public void setUpComponents(){

        panelOutil = new JPanel();
        panelOutil.setLayout(new BoxLayout(panelOutil, BoxLayout.PAGE_AXIS));

        retourAccueil = new JButton("Retour à l'accueil");
        retourAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToEntranceWindow();
            }
        });
        panelOutil.add(retourAccueil);

        //Ajout de la liste des outils
        outilLabel = new JLabel("Liste des outils disponibles");
        outilComboBoxModel = new DefaultComboBoxModel<>();
        outilComboBox = new JComboBox<>(outilComboBoxModel);
        outilComboBox.setPreferredSize(new Dimension(100, 25));
        panelOutil.add(outilLabel);
        panelOutil.add(outilComboBox);

        //Methode chargee d'afficher uniquement le nom de l'outil creer dans la liste deroulante
        outilComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                if(value instanceof OutilReadOnly){
                    setText(((OutilReadOnly) value).getNom());
                }
                return this;
            }
        });

        statutLabel = new JLabel("Entrez les informations pour la creation de l'outil");
        panelOutil.add(statutLabel);

        //Ajout des champs pour la creation de l'outil
        JLabel nomOutilLabel = new JLabel("Nom de l'outil");
        nomOutil = new JTextField();
        panelOutil.add(nomOutilLabel);
        panelOutil.add(nomOutil);

        JLabel largeurOutilLabel = new JLabel("Largeur de l'outil");
        largeurOutil = new JTextField();
        panelOutil.add(largeurOutilLabel);
        panelOutil.add(largeurOutil);

        JLabel positionOutilLabel = new JLabel("Position de l'outil");
        positionOutil = new JTextField();
        panelOutil.add(positionOutilLabel);
        panelOutil.add(positionOutil);

        //Ajout des boutons
        creerOutil = new JButton("Créer l'outil");
        creerOutil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTool();
            }
        });

        supprimerOutil = new JButton("Supprimer l'outil");
        supprimerOutil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTool();
            }
        });
        panelOutil.add(creerOutil);
        panelOutil.add(supprimerOutil);
        add(panelOutil);
    }

    private void createTool()
    {
        try {
            String nom = nomOutil.getText();
            int largeur = Integer.parseInt(largeurOutil.getText());
            int position = Integer.parseInt(positionOutil.getText());

            if (nom.isEmpty() || largeur <= 0 || position <= 0) {
                statutLabel.setText("Veuillez remplir tous les champs");
            }
            fenetrePrincipale.addTool(nom, largeur, position);

            outilComboBoxModel.addElement(new Outil(nom, largeur, position));

            nomOutil.setText("");
            largeurOutil.setText("");
            positionOutil.setText("");
            statutLabel.setText("Outil" + nom + " créé avec succès");
        } catch (Exception exception) {
            statutLabel.setText("Erreur lors de la création de l'outil");
        }
    }

    private void deleteTool()
    {
        OutilReadOnly outil = (OutilReadOnly) outilComboBox.getSelectedItem();
        if (outil != null && outilComboBoxModel.getSize() > 0) {
            if (outilComboBoxModel.getSize() == 1) {
                fenetrePrincipale.deleteTool(outil.getNom());
                outilComboBoxModel.removeElement(outil);
                outilComboBox.setSelectedIndex(-1);
                statutLabel.setText("Outil supprimé avec succès");
            } else {
                fenetrePrincipale.getControleurLarman().supprimerOutil(outil.getNom());
                outilComboBoxModel.removeElement(outil);
                statutLabel.setText("Outil supprimé avec succès");
            }
        } else {
            outilComboBox.setSelectedIndex(-1);
            outilComboBox.repaint();
            statutLabel.setText("Aucun outil disponible");
        }
        outilComboBox.repaint();
    }
}
