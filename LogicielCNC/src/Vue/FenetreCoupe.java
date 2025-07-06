package LogicielCNC.src.Vue;

import LogicielCNC.src.Domaine.*;
import LogicielCNC.src.Domaine.CNC.CNC;
import LogicielCNC.src.Domaine.CNC.CoupesManageur;
import LogicielCNC.src.Domaine.Coupes.*;
import LogicielCNC.src.Vue.Affichage.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.List;

public class FenetreCoupe extends FenetreAffichee {

    private final String PICK_BORDER_TEXT = "Cliquez sur une coupe ou une référence : ";


    //Utile pour plus tard
    private CoupeReadOnly currentCut;
    private CoupeReadOnly reference;
    private List<CoupeReadOnly> listeReference;
    private List<CoupeReadOnly> liste_coupe;
    private boolean currentlyHasCorner = false;
    private boolean borderPickedVertical = false;
    private Point cornerPicked;
    private Point pointOpposeClicked;
    private Point oppose;
    private Point ref_RectCut;
    private TypeCoupe currentType;
    private Line2D nvreference=null;
    private JPanel mainPanel;
    private JButton returnButton;
    private JButton droiteButton;
    private JButton rectangleButton;
    private JButton lButton;
    private JButton retaillerButton;
    private JButton changerOutil;
    private JButton undoButton; // Annuler derniere action
    private JButton redoButton; //Rétablir derniere action
    private JButton enregistrerButton;
    private JButton eraseButton;
    private JButton changeReferenceButton;
    private JLabel survolPointLabel;
    private JLabel survolPointX;
    private JTextField survolPointXTextField;
    private JLabel survolPointY;
    private JTextField survolPointYTextField;
    private JLabel survolDimensionLabel;
    private JLabel survolLargeur;
    private JTextField survolLargeurTextField;
    private JLabel survolHauteur;
    private JTextField survolHauteurTextField;
    private JLabel typesDeCoupeLabel;
    private JLabel statusLabel; // JLabel pour afficher le statut
    private JLabel statusLabel2;
    private JLabel statusLabel3;
    private JTextField deepnessField;
    private JLabel firstCreateCutLabel;
    private JTextField firstCreateCutField;
    private JLabel secondCreateCutLabel;
    private JTextField secondCreateCutField;
    private JLabel toolLabel;
    private JComboBox<OutilReadOnly> createToolComboBox;
    private JComboBox<CoupeReadOnly> referenceComboBox;
    private DefaultComboBoxModel<CoupeReadOnly> referenceModel;
    private JButton cancelButton;
    private boolean isReferenceSelected = false;
    private JLabel modifyLabel;
    private JLabel changereferenceLabel;
    private JButton changeCutButton;
    private JLabel newDeepnessLabel;
    private JTextField newDeepnessTextField;
    private JLabel firstModifyLabel;
    private JTextField firstModifyTextField;
    private JLabel secondModifyLabel;
    private JTextField secondModifyTextField;
    private JLabel thirdModifyLabel;
    private JTextField thirdModifyTextField;
    private JLabel fourthModifyLabel;
    private JTextField fourthModifyTextField;
    private boolean mag = false;
    private int Cellule = 20;

    private JLabel changeToolLabel;
    private JComboBox<OutilReadOnly> changeToolComboBox;
    private JButton modifyButton;

    private JLabel lengthType;
    private JRadioButton mmButton;
    private JRadioButton inchesButton;

    private double x;
    private double y;
    private int dx;
    private  int dy;
    private ControleurLarman controleurLarman;
    private CNC cnc;
    private DrawingPanel drawingPanel;

    public FenetreCoupe(FenetrePrincipale fenetrePrincipale1) {
        super(fenetrePrincipale1);



    }
public void receiveSingleReference2(Line2D line) {
        this.nvreference = line;
        borderPickedVertical = nvreference.getX1()==nvreference.getX2();
        if (borderPickedVertical){  statusLabel.setText("Nouvelle référence : Coupe verticale à x = " + nvreference.getX1());
        }else{statusLabel.setText("Nouvelle référence : Coupe horizontale à y = " + nvreference.getY1());
    }
    changeCreateText();
    enregistrerButton.setVisible(true);
    }
    /*
    */

    public boolean needsABorder()
    {
        return currentType == TypeCoupe.COUPE_REGULIERE && nvreference == null;
    }

    public boolean needsAPoint()
    {
        return (currentType == TypeCoupe.COUPE_RECTANGULAIRE || currentType == TypeCoupe.COUPE_L)
                && !(currentlyHasCorner);
    }


    public void receivePointClicked(Point point){

        this.cornerPicked = point;
        statusLabel.setText("Le point référence à été cliqué");

    }

    public void receivePointClickedOppose(Point point){
        this.pointOpposeClicked = point;
        statusLabel.setText("Le point Opposé à été cliqué");
        changeCreateText();
        enregistrerButton.setVisible(true);
    }
    public void getmag(){
        mag=fenetrePrincipale.getmag();
    }
    public void getcellule(){
        Cellule=fenetrePrincipale.getTaille_cellule();
    }
    public void receivePointreference(Point point){
        this.ref_RectCut = point;
        statusLabel.setText("Le point référence Coupe rectangulaire à été cliqué");
    }

    public void SurvolPoint(double pointX , double pointY){

        x = pointX;
        y = pointY;
        survolPointXTextField.setText(String.valueOf(x));
        survolPointYTextField.setText(String.valueOf(y));
    }

    public void DimSurvol(int dimX , int dimY){
        dx = dimX;
        dy = dimY;
        survolLargeurTextField.setText(String.valueOf(dx));
        survolHauteurTextField.setText(String.valueOf(dy));

    }


    private void changeCreateText()
    {
        if(currentType == TypeCoupe.COUPE_REGULIERE)
        {
            if(borderPickedVertical)
            {
                firstCreateCutLabel.setText("Choisissez la largeur de votre piece");
            }
            else
            {
                firstCreateCutLabel.setText("Choisissez la hauteur de votre piece");
            }
        }
        else
        {
            /*if(currentType == TypeCoupe.COUPE_RECTANGULAIRE)
            {
                firstCreateCutLabel.setText("Hauteur de votre rectangle");
                secondCreateCutLabel.setText("Largeur de votre rectangle");

            }else if(currentType == TypeCoupe.COUPE_L){
                firstCreateCutLabel.setText("Hauteur de votre coupe L");
                secondCreateCutLabel.setText("Largeur de votre coupe L");
            }*/
            if (currentType == TypeCoupe.COUPE_BORDURE) //Pour retailler
            {
                firstCreateCutLabel.setText("Retailler horizontalement");
                secondCreateCutLabel.setText("Retailler verticalement");
            }
            if (currentType != TypeCoupe.COUPE_L && currentType != TypeCoupe.COUPE_RECTANGULAIRE) {
                secondCreateCutLabel.setVisible(true);
                secondCreateCutField.setVisible(true);
            }
        }
        if (currentType != TypeCoupe.COUPE_L && currentType != TypeCoupe.COUPE_RECTANGULAIRE) {
            firstCreateCutLabel.setVisible(true);
            firstCreateCutField.setVisible(true);
        }
    }

    public void updateToolsList()
    {
        //Methode chargee d'afficher uniquement le nom de l'outil creer dans la liste deroulante
        createToolComboBox.setModel(new DefaultComboBoxModel(fenetrePrincipale.getToolsNames().toArray()));
        changeToolComboBox.setModel(new DefaultComboBoxModel(fenetrePrincipale.getToolsNames().toArray()));
    }


    @Override
    protected void setUpComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        returnButton = new JButton("Retour à l'accueil");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fenetrePrincipale.goToEntranceWindow();
            }
        });
        mainPanel.add(returnButton);


        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        undoButton.addActionListener(e -> controleurLarman.undo());
        redoButton.addActionListener(e -> controleurLarman.redo());

        mainPanel.add(buttonPanel);
        //mainPanel.add(redoButton)


        typesDeCoupeLabel = new JLabel("Choisissez un type de coupe :");
        mainPanel.add(typesDeCoupeLabel);

        //bouton pour choisir le type de coupe:
        droiteButton = new JButton("Coupe Réguliere");
        rectangleButton = new JButton("Coupe Rectangle");
        lButton = new JButton("Coupe en L");
        retaillerButton = new JButton("Coupe Bordure");
        mainPanel.add(droiteButton);
        mainPanel.add(rectangleButton);
        mainPanel.add(lButton);
        mainPanel.add(retaillerButton);
        // Set up event listener:
        droiteButton.addActionListener(e -> selectCutType(TypeCoupe.COUPE_REGULIERE));
        rectangleButton.addActionListener(e -> selectCutType(TypeCoupe.COUPE_RECTANGULAIRE));
        lButton.addActionListener(e -> selectCutType(TypeCoupe.COUPE_L));
        retaillerButton.addActionListener(e -> selectCutType(TypeCoupe.COUPE_BORDURE));

        add(mainPanel);

        enregistrerButton = new JButton("Enregistrer la coupe");
        enregistrerButton.setVisible(false);

        // Initialisation de JLabel pour le statutstatusLabel
        statusLabel = new JLabel("");
        statusLabel2 = new JLabel("");
        statusLabel3 = new JLabel("Profondeur: ");
        deepnessField = new JTextField("0.5");
        firstCreateCutLabel = new JLabel();
        firstCreateCutLabel.setVisible(false);
        firstCreateCutField = new JTextField();
        firstCreateCutField.setVisible(false);
        secondCreateCutLabel = new JLabel();
        secondCreateCutLabel.setVisible(false);
        secondCreateCutField = new JTextField();
        secondCreateCutField.setVisible(false);
        mainPanel.add(statusLabel);
        mainPanel.add(statusLabel2);
        mainPanel.add(statusLabel3);
        mainPanel.add(deepnessField);
        mainPanel.add(firstCreateCutLabel);
        mainPanel.add(firstCreateCutField);
        mainPanel.add(secondCreateCutLabel);
        mainPanel.add(secondCreateCutField);
        toolLabel = new JLabel("Choisissez un outil:");
        createToolComboBox = new JComboBox<>();
        mainPanel.add(toolLabel);
        mainPanel.add(createToolComboBox);

        mainPanel.add(enregistrerButton);

        cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetCutData();
            }
        });
        mainPanel.add(cancelButton);


        modifyLabel = new JLabel("Sélectionner une coupe:  ");
        changeCutButton = new JButton("Changer de coupe");
        changeCutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCut();
            }
        });

        changereferenceLabel = new JLabel("Sélectionner une référence : ");
        referenceModel = new DefaultComboBoxModel<>();
        referenceComboBox = new JComboBox<>(referenceModel);
        referenceComboBox.setPreferredSize(new Dimension(100, 25));
        mainPanel.add(changereferenceLabel);
        changereferenceLabel.setVisible(false);
        mainPanel.add(modifyLabel);
        mainPanel.add(referenceComboBox);
        referenceComboBox.setVisible(false);


        referenceComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
               listeReference = fenetrePrincipale.getAllCoupes();
               if (value instanceof  CoupeReadOnly){
                   String position = String.valueOf(listeReference.indexOf(value));
                   int positionInt = Integer.parseInt(position) + 1;
                   if (String.valueOf(positionInt).equals("0"))
                       setText("Aucun coupe");
                   else
                       setText("Coupe " + String.valueOf(positionInt));
               }
               return this;
            }
        });

        changeReferenceButton = new JButton("Changer de référence");
        changeReferenceButton.setVisible(true);
        mainPanel.add(changeReferenceButton);
        changeReferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isReferenceSelected = true;
            }
        });

        newDeepnessLabel = new JLabel("Nouvelle profondeur");
        newDeepnessTextField = new JTextField();
        newDeepnessTextField.setText("0.5");

        firstModifyLabel = new JLabel("");
        firstModifyTextField = new JTextField();

        secondModifyLabel = new JLabel("");
        secondModifyTextField = new JTextField();

        thirdModifyLabel = new JLabel("");
        thirdModifyTextField = new JTextField();

        fourthModifyLabel = new JLabel("");
        fourthModifyTextField = new JTextField();

        modifyButton = new JButton("Enregistrer la modification");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyCut();
            }
        });

        mainPanel.add(modifyLabel);
        mainPanel.add(changeCutButton);
        mainPanel.add(newDeepnessLabel);
        mainPanel.add(newDeepnessTextField);
        mainPanel.add(firstModifyLabel);
        mainPanel.add(firstModifyTextField);
        mainPanel.add(secondModifyLabel);
        mainPanel.add(secondModifyTextField);
        makeModifTextFieldVisible(false);
        changeToolLabel = new JLabel("Changer d'outil:");
        changeToolComboBox = new JComboBox<>();
        liste_coupe = new java.util.ArrayList<CoupeReadOnly>();
        updateToolsList();
        mainPanel.add(changeToolLabel);
        mainPanel.add(changeToolComboBox);
        mainPanel.add(modifyButton);

        enregistrerButton.addActionListener(e -> {
            performEnregistrerButton();
        });
        eraseButton = new JButton("Effacer une coupe");
        eraseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eraseCut();
            }
        });
        mainPanel.add(eraseButton);

        lengthType = new JLabel("Type de données entrées");
        ButtonGroup buttonGroup = new ButtonGroup();
        mmButton = new JRadioButton("MM");
        mmButton.setSelected(true);
        inchesButton = new JRadioButton("Pouces");
        buttonGroup.add(mmButton);
        buttonGroup.add(inchesButton);
        mainPanel.add(lengthType);
        mainPanel.add(mmButton);
        mainPanel.add(inchesButton);

        survolPointLabel = new JLabel("La souris est à la position :");
        survolPointX = new JLabel("En X : ");
        survolPointXTextField = new JTextField();
        survolPointY = new JLabel("En Y :");
        survolPointYTextField = new JTextField();
        mainPanel.add(survolPointLabel);
        mainPanel.add(survolPointX);
        mainPanel.add(survolPointXTextField);
        mainPanel.add(survolPointY);
        mainPanel.add(survolPointYTextField);


        survolDimensionLabel = new JLabel("Les dimensions de la coupe survolée: ");
        survolLargeur = new JLabel("En Largeur : ");
        survolLargeurTextField = new JTextField();
        survolHauteur = new JLabel("En Hauteur :");
        survolHauteurTextField = new JTextField();
        mainPanel.add(survolDimensionLabel);
        mainPanel.add(survolLargeur);
        mainPanel.add(survolLargeurTextField);
        mainPanel.add(survolHauteur);
        mainPanel.add(survolHauteurTextField);
        add(mainPanel);

    }



    public CoupeReadOnly getCurrentReference(int index) {
        return liste_coupe.get(index);
    }

    public CoupeReadOnly getSelectedReference(){
        if (referenceComboBox.getSelectedIndex() == 0 && !isReferenceSelected)
            return null;
        return (CoupeReadOnly) referenceComboBox.getSelectedItem();

    }

    private void changeCut()
    {
        currentCut = fenetrePrincipale.getNextCut();
        if (currentCut != null)
        {
            if(currentCut.getType() == TypeCoupe.COUPE_REGULIERE)
            {
                borderPickedVertical = currentCut.getPointOrigine().x == currentCut.getPointDestination().x;
            }
            changeModifText();
        }
        else
        {
            makeModifTextFieldVisible(false);
        }
    }


    private void changeModifText()
    {
        //On changera ça pour prendre le type
        makeModifTextFieldVisible(true);
        if(currentCut.getType() == TypeCoupe.COUPE_REGULIERE)
        {
            if(borderPickedVertical)
            {
                firstModifyLabel.setText("Déplacer vers la gauche");
                secondModifyLabel.setText("Déplacer vers la droite");
            }
            else
            {
                firstModifyLabel.setText("Déplacer vers le haut");
                secondModifyLabel.setText("Déplacer vers le bas");
            }
        }
        else if (currentCut.getType() == TypeCoupe.COUPE_BORDURE ){

            firstModifyLabel.setText("Entrer une nouvelle longueur :");
            secondModifyLabel.setText("Entrer une nouvelle largeur :");

        }
        else
        {
            firstModifyLabel.setText("Entrer une nouvelle longueur :");
            secondModifyLabel.setText("Entrer une nouvelle largeur :");

        }
    }

    private void makeModifTextFieldVisible(boolean shouldBeVisible)
    {
        firstModifyLabel.setVisible(shouldBeVisible);
        firstModifyTextField.setVisible(shouldBeVisible);
        secondModifyLabel.setVisible(shouldBeVisible);
        secondModifyTextField.setVisible(shouldBeVisible);
        thirdModifyLabel.setVisible(shouldBeVisible);
        thirdModifyTextField.setVisible(shouldBeVisible);
        fourthModifyLabel.setVisible(shouldBeVisible);
        fourthModifyTextField.setVisible(shouldBeVisible);
    }

    private void selectCutType(TypeCoupe type) {
        // Logic to handle different types of cuts
        resetCutData();
        currentType = type;
        statusLabel2.setText("Vous êtes en mode: " + type);
        statusLabel2.setVisible(true);
        if(currentType == TypeCoupe.COUPE_REGULIERE)
        {
            statusLabel.setText(PICK_BORDER_TEXT);
            changereferenceLabel.setVisible(true);
            referenceComboBox.setVisible(true);
        }
        else if(currentType == TypeCoupe.COUPE_L || currentType == TypeCoupe.COUPE_RECTANGULAIRE)
        {
            statusLabel.setText("Cliquez sur deux points opposés :");
            changeCreateText();
            changereferenceLabel.setVisible(true);
            referenceComboBox.setVisible(true);
        }
        else if(currentType == TypeCoupe.COUPE_BORDURE)
        {
            statusLabel.setText("Entrez les dimensions des bordures :");
            changeCreateText();
            changereferenceLabel.setVisible(true);
            referenceComboBox.setVisible(true);
            enregistrerButton.setVisible(true);
        }
    }

    private void modifyCut() {
        if (currentCut != null) {
            int up_down = 0;
            if (currentCut.getType() == TypeCoupe.COUPE_REGULIERE) {
               int ll=currentCut.getlengthawayfromborder();
                float movement = 0;
                String upText = firstModifyTextField.getText();
                String downText = secondModifyTextField.getText();
                if (!upText.isEmpty() && tellIfStringIsNumber(upText)) {
                    movement = -Integer.parseInt(upText); // Mouvement vers le haut
                    up_down = 1;
                } else if (!downText.isEmpty() && tellIfStringIsNumber(downText)) {
                    movement = Integer.parseInt(downText); // Mouvement vers le bas
                    up_down = -1;
                } else {
                    movement = 0; // Aucun mouvement
                    up_down = 0;
                }

                float deepness = Float.valueOf(newDeepnessTextField.getText());
                if (inchesButton.isSelected()) {
                    deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                    movement = fenetrePrincipale.getUneDimensionPouceEnMM(movement);
                }
                getcellule();
                getmag();
                if(mag){

                    System.out.println("Initial movement: " + movement);

                    float range = (movement +ll)% Cellule;
                    if(Math.abs(range)< Cellule/2){
                        movement=movement-range;
                    }else{
                        movement=movement+(Cellule-Math.abs(range));
                    }

                    System.out.println("Initial movement: " + movement);
                }
                CoupeReadOnly temp =  currentCut;
                currentCut = fenetrePrincipale.modifyRegularCut(deepness, movement,
                        currentCut, String.valueOf(changeToolComboBox.getSelectedItem()));
               for (CoupeReadOnly coupe : liste_coupe) {
                   float y;
                   float y2;
                   if (coupe.getType() == TypeCoupe.COUPE_L) {
                       CoupeL coupeL = (CoupeL) coupe;
                       if (coupeL.getReference() == temp) {
                           coupeL.setReference(currentCut);
                           if (coupeL.getPoint().y < coupeL.getPointOppose().y) { // Point supérieur
                               if (up_down == 1) {
                                   y = coupeL.getPoint().y + movement;
                                   y2 = coupeL.getPointOppose().y + movement;
                               } else {
                                   y = coupeL.getPoint().y + movement;
                                   y2 = coupeL.getPointOppose().y + movement;
                               }
                           } else { // Point inférieur
                               y = coupeL.getPoint().y + movement;
                               y2 = coupeL.getPointOppose().y + movement;
                           }
                           coupeL.setPoint(new Point(coupeL.getPoint().x, (int) y));
                           coupeL.setPointOppose(new Point(coupeL.getPointOppose().x, (int) y2));


                           for (CoupeReadOnly coupe1 : liste_coupe) {
                               if (coupe1.getType() == TypeCoupe.COUPE_RECTANGULAIRE) {
                                   CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) coupe1;
                                   if (coupeRectangulaire.getReference() == coupeL) {
                                        coupeRectangulaire.setReference(coupeL);
                                       if (coupeRectangulaire.getPoint().y < coupeRectangulaire.getPointOppose().y) { // Point supérieur
                                           if (up_down == 1) {
                                               y = coupeRectangulaire.getPoint().y + movement;
                                               y2 = coupeRectangulaire.getPointOppose().y + movement;
                                           } else {
                                               y = coupeRectangulaire.getPoint().y + movement;
                                               y2 = coupeRectangulaire.getPointOppose().y + movement;
                                           }
                                       } else { // Point inférieur
                                           if (up_down == 1) {
                                               y = coupeRectangulaire.getPoint().y + movement;
                                               y2 = coupeRectangulaire.getPointOppose().y + movement;
                                           } else {
                                               y = coupeRectangulaire.getPoint().y + movement;
                                               y2 = coupeRectangulaire.getPointOppose().y + movement;
                                           }
                                       }
                                       coupeRectangulaire.setPoint(new Point(coupeRectangulaire.getPoint().x, (int) y));
                                       coupeRectangulaire.setPointOppose(new Point(coupeRectangulaire.getPointOppose().x, (int) y2));

                                   }
                               }
                           }
                       }
                           System.out.println("Coupe modifiée");
                           System.out.println("CoupeL modifiee " + "Nouvelle reference: " + currentCut.getPointOrigine());
                   }
               }
            } else if (currentCut.getType() == TypeCoupe.COUPE_BORDURE) {
                if (tellIfStringIsNumber(firstModifyTextField.getText()) &&
                        tellIfStringIsNumber(secondModifyTextField.getText())) {
                    int length = Integer.parseInt(firstModifyTextField.getText());
                    int width = Integer.parseInt(secondModifyTextField.getText());
                    float deepness = Float.valueOf(newDeepnessTextField.getText());
                    if (inchesButton.isSelected()) {
                        deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                        width = (int) fenetrePrincipale.getUneDimensionPouceEnMM(width);
                        length = (int) fenetrePrincipale.getUneDimensionPouceEnMM(length);
                    }
                    Dimension dimension = new Dimension(width, length);
                    currentCut = fenetrePrincipale.modifierCoupeBordure(deepness, currentCut, dimension,
                            String.valueOf(changeToolComboBox.getSelectedItem()));
                }
            } else if (currentCut.getType() == TypeCoupe.COUPE_RECTANGULAIRE) {
                if (tellIfStringIsNumber(firstModifyTextField.getText()) &&
                        tellIfStringIsNumber(secondModifyTextField.getText())) {
                    int length = Integer.parseInt(firstModifyTextField.getText());
                    int width = Integer.parseInt(secondModifyTextField.getText());
                    float deepness = Float.valueOf(newDeepnessTextField.getText());
                    if (inchesButton.isSelected()) {
                        deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                        width = (int) fenetrePrincipale.getUneDimensionPouceEnMM(width);
                        length = (int) fenetrePrincipale.getUneDimensionPouceEnMM(length);
                    }
                    Dimension dimension = new Dimension(width, length);
                    if (currentCut.getPointOppose().x > currentCut.getPoint().x) {
                        if (currentCut.getPointOppose().y > currentCut.getPoint().y) {
                            // Superieur Gauche
                            oppose = new Point(currentCut.getPoint().x + width, currentCut.getPoint().y + length);
                        } else {
                            // Inferieur gauche
                            oppose = new Point(currentCut.getPoint().x + width, currentCut.getPoint().y - length);
                        }

                    } else {
                        if (currentCut.getPointOppose().y > currentCut.getPoint().y) {
                            // Superieur Droit
                            oppose = new Point(currentCut.getPoint().x - width, currentCut.getPoint().y + length);
                        } else {
                            // Inferieur Droit
                            oppose = new Point(currentCut.getPoint().x - width, currentCut.getPoint().y - length);
                        }
                    }
                    CoupeReadOnly reference = getSelectedReference();
                    currentCut = fenetrePrincipale.modifierCoupeRectangulaire(deepness, currentCut.getPoint(), oppose, reference, dimension,
                             String.valueOf(changeToolComboBox.getSelectedItem()), currentCut);
                }
            } else if (currentCut.getType() == TypeCoupe.COUPE_L) {
                if (tellIfStringIsNumber(firstModifyTextField.getText()) &&
                        tellIfStringIsNumber(secondModifyTextField.getText())) {
                    int length = Integer.parseInt(firstModifyTextField.getText());
                    int width = Integer.parseInt(secondModifyTextField.getText());
                    float deepness = Float.valueOf(newDeepnessTextField.getText());
                    if (inchesButton.isSelected()) {
                        deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                        width = (int) fenetrePrincipale.getUneDimensionPouceEnMM(width);
                        length = (int) fenetrePrincipale.getUneDimensionPouceEnMM(length);
                    }
                    Dimension dimension = new Dimension(width, length);

                    if(currentCut.getPointOppose().x > currentCut.getPoint().x){
                        if(currentCut.getPointOppose().y > currentCut.getPoint().y){
                            oppose = new Point(currentCut.getPoint().x + width , currentCut.getPoint().y + length);
                        }else {
                            oppose = new Point(currentCut.getPoint().x + width,currentCut.getPoint().y - length);
                        }
                    }else{
                        if(currentCut.getPointOppose().y > currentCut.getPoint().y){
                            oppose = new Point(currentCut.getPoint().x - width, currentCut.getPoint().y + length);
                        }else {
                            oppose = new Point(currentCut.getPoint().x - width,currentCut.getPoint().y - length);
                        }
                    }
                    reference = fenetrePrincipale.findCoupeReguliereUnderProjection(currentCut.getPoint(),
                            oppose, liste_coupe);
                    Point origine_modifie = reference.getPointOrigine();
                    Point oppose_modifier = currentCut.getPointOppose();
                    currentCut = fenetrePrincipale.modifierCoupeL(deepness, origine_modifie , oppose_modifier, reference,dimension,
                            String.valueOf(changeToolComboBox.getSelectedItem()), currentCut);
                }
            }
        }
    }

    private boolean tellIfStringIsNumber(String string)
    {
        //https://medium.com/javarevisited/how-to-check-if-a-string-is-numeric-to-avoid-numberformatexception-f07950c47c61#:~:text=The%20isNumeric()%20method%20takes,way%20to%20check%20numeric%20values.
        return string != null && string.matches("[0-9.]+.");
    }

    private void eraseCut()
    {
        if(currentCut!=null)
        {
            fenetrePrincipale.eraseCut(currentCut);
            referenceModel.removeElement(currentCut);
            liste_coupe.remove(currentCut);
            changeCut();
        }
    }

    private void performEnregistrerButton() {
        if(currentType==TypeCoupe.COUPE_REGULIERE)
        {
            if(!firstCreateCutField.getText().isEmpty() &&
                    tellIfStringIsNumber(firstCreateCutField.getText()) &&
                    !deepnessField.getText().isEmpty() && tellIfStringIsNumber(deepnessField.getText()))
            {
                int lenghtAwayFromBorder = Integer.parseInt(firstCreateCutField.getText());
                float deepness = Float.valueOf(deepnessField.getText());
                if(inchesButton.isSelected())
                {
                    deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                    lenghtAwayFromBorder = (int) fenetrePrincipale.getUneDimensionPouceEnMM(lenghtAwayFromBorder);
                }
                currentCut = fenetrePrincipale.createRegularCut(lenghtAwayFromBorder, nvreference,
                        deepness, String.valueOf(createToolComboBox.getSelectedItem()));
                referenceModel.addElement(currentCut);
                liste_coupe.add(currentCut);

                if (currentCut != null)
                {
                    System.out.println("Enregistrement de la coupe");
                    resetCutData();
                    changeModifText();
                }
                else
                {
                    System.out.println("Impossible");
                }
            }
        }else if (currentType==TypeCoupe.COUPE_BORDURE) {
            if (!firstCreateCutField.getText().isEmpty() && tellIfStringIsNumber(firstCreateCutField.getText()) && !secondCreateCutField.getText().isEmpty() &&
                    tellIfStringIsNumber(secondCreateCutField.getText()) && !deepnessField.getText().isEmpty() && tellIfStringIsNumber(deepnessField.getText())) {
                int longueur = Integer.parseInt((firstCreateCutField.getText()));
                int largeur = Integer.parseInt((secondCreateCutField.getText()));
                float deepness = Float.valueOf(newDeepnessTextField.getText());
                if (inchesButton.isSelected()) {
                    longueur = (int) fenetrePrincipale.getUneDimensionPouceEnMM(longueur);
                    largeur = (int) fenetrePrincipale.getUneDimensionPouceEnMM(largeur);
                    deepness = fenetrePrincipale.getUneDimensionPouceEnMM(deepness);
                }
                currentCut = fenetrePrincipale.createBorderCut(longueur, largeur, deepness, String.valueOf(createToolComboBox));
                referenceModel.addElement(currentCut);
                liste_coupe.add(currentCut);
            }
            if (currentCut != null) {
                System.out.println("Enregistrement de la coupe BOURDURE");
                resetCutDatabordure();
                changeModifText();
            } else {
                System.out.println("Impossible");
            }
        }
        else if((currentType == TypeCoupe.COUPE_L || currentType == TypeCoupe.COUPE_RECTANGULAIRE) && cornerPicked != null && pointOpposeClicked != null){
            //On rend les entrées des JTextField invisbles
            firstCreateCutField.setVisible(false);
            secondCreateCutField.setVisible(false);
            deepnessField.setVisible(false);

            if(cornerPicked != null) {
                int hauteur = 50;
                int largeur = 50;
                float profondeur = 1;

                if(inchesButton.isSelected()) {
                    hauteur = (int) fenetrePrincipale.getUneDimensionPouceEnMM(hauteur);
                    largeur = (int) fenetrePrincipale.getUneDimensionPouceEnMM(largeur);
                    profondeur = fenetrePrincipale.getUneDimensionPouceEnMM(profondeur);
                }

                if (currentType == TypeCoupe.COUPE_L) {
                    reference = fenetrePrincipale.findCoupeReguliereUnderProjection(cornerPicked, pointOpposeClicked, liste_coupe);
                    currentCut = fenetrePrincipale.createLCut(profondeur,cornerPicked, pointOpposeClicked, reference, new Dimension(largeur,hauteur),String.valueOf(createToolComboBox.getSelectedItem()));
                    System.out.println("Reference de la Coupe L : " + reference.getPointDestination() + " " + reference.getPointOrigine());
                    referenceModel.addElement(currentCut);
                    liste_coupe.add(currentCut);
                } else if(currentType == TypeCoupe.COUPE_RECTANGULAIRE) {
                    CoupeReadOnly ref_temp = null;
                    for (CoupeReadOnly coupe : liste_coupe) {
                        if (coupe.getType() == TypeCoupe.COUPE_L) {
                            CoupeL coupeL = (CoupeL) coupe;
                            if (pointsSimilaires(coupeL.getPointOppose(), ref_RectCut, 2)) {
                                ref_temp = coupeL;
                                System.out.println("Points similaires et reference trouvee");
                            }
                        }
                    }
                    if (ref_temp == null) {
                        Point opp = new Point(cornerPicked.x + 50, cornerPicked.y - 50);
                        Dimension dimension = new Dimension(50, 50);
                        float f = 1.0F;
                        Outil o = new Outil("depanne", 1, 0);
                       ref_temp = new CoupeL(f, cornerPicked, opp, null, dimension, o);
                    }                   // CoupeReadOnly reference = fenetrePrincipale.findCoupeReguliereUnderProjection(cornerPicked, pointOpposeClicked, liste_coupe);
                    currentCut = fenetrePrincipale.createRectangularCut(profondeur,cornerPicked,pointOpposeClicked, ref_temp, new Dimension(largeur,hauteur),String.valueOf(createToolComboBox.getSelectedItem()));
                  //  CoupeRectangulaire coupeRectangulaire = (CoupeRectangulaire) currentCut;
                   // System.out.println("Reference de la Coupe Rectangulaire : " + coupeRectangulaire.getReference().getPointOppose() + " " + coupeRectangulaire.getPoint());
                    if (ref_temp.getType() == TypeCoupe.COUPE_L) {
                        System.out.println("Reference de la Coupe Rectangulaire : " + ref_temp.getPointOppose() + " " + ref_temp.getPoint());
                    }
                    referenceModel.addElement(currentCut);
                    liste_coupe.add(currentCut);
                }
            }

            if(currentCut != null){
                System.out.println("Enregistrement de la coupe " + (currentType == TypeCoupe.COUPE_L ? "L" : "Rectangulaire"));
                resetCutData();
                changeModifText();
                }
                else {
                    System.out.println("Impossible de créer la coupe...");
                }
        }
    }

    private void resetCutDatabordure()
    {
        resetCutData();
        secondCreateCutField.setText("");
        secondCreateCutLabel.setVisible(false);
        secondCreateCutField.setVisible(false);
    }

    private void resetCutData()
    {
        enregistrerButton.setVisible(false);
        nvreference= null;
        statusLabel2.setVisible(false);
        firstCreateCutLabel.setVisible(false);
        firstCreateCutField.setText("");
        firstCreateCutField.setVisible(false);
        secondCreateCutLabel.setVisible(false);
        secondCreateCutField.setText("");
        secondCreateCutField.setVisible(false);
        statusLabel.setText("");
        currentType = null;
        currentlyHasCorner = false;
        cornerPicked = null;
    }




    /*public void receiveCornerReference(List<CoupeReadOnly> list)
    {
        this.reference = list.get(0);
        this.secondReference = list.get(1);
        statusLabel.setText("Les références ont été choisis.");
        changeCreateText(); // Met à jour les instructions pour les champs de création
        enregistrerButton.setVisible(true);
    }*/





    public Point getCornerPicked(){ return  cornerPicked; }
    public TypeCoupe getCurrentType() {return  currentType;}




    public static boolean pointsSimilaires(Point p1, Point p2, double epsilon) {
        // Calcul de la distance euclidienne entre les deux points
        double distance = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
        // Vérifie si la distance est inférieure ou égale à epsilon
        return distance <= epsilon;
    }


}
