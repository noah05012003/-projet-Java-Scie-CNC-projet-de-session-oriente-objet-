package LogicielCNC.src.Domaine.CNC;

import LogicielCNC.src.Domaine.Coupes.*;
import LogicielCNC.src.Domaine.OutilReadOnly;
import LogicielCNC.src.Domaine.PanneauReadOnly;

import java.awt.*;
import java.io.*;
import java.util.List;

public class SaveManager {
    private CNC cnc;

    public SaveManager(CNC cnc)
    {
        this.cnc = cnc;
    }

    public void saveInFile(File file)
    {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            for (CoupeReadOnly coupeReadOnly : cnc.getCoupe())
            {
                out.writeObject(coupeReadOnly);
            }
            for (OutilReadOnly outilReadOnly : cnc.getOutil())
            {
                out.writeObject(outilReadOnly);
            }
            out.writeObject(cnc.getPanneau());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadInFile(File file)
    {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            Object object = in.readObject();
            while (object instanceof CoupeReadOnly)
            {
                cnc.addSavedCut((CoupeReadOnly) object);
                object = in.readObject();
            }
            while (object instanceof OutilReadOnly)
            {
                cnc.addSavedTool((OutilReadOnly) object);
                object = in.readObject();
            }
            cnc.replacePanelBySavedOne((PanneauReadOnly) object);
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGCodeFile(File file)
    {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject("O0036 (Practice)\n" +
                    "N10 G00 G17 G40 G80\n" +
                    "N20 G90 G94 G98\n" +
                    "N30 G54\n" +
                    "N40 G43 T04 H04 M0 (.250 FEM)\n" +
                    "N50 51000 M3\n" +
                    "N60 G00 X0.0000 Y0.0000 Z0.0000\n");
            addPanelToGCode(out);
            addCutsToGcode(out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addPanelToGCode(ObjectOutputStream out) throws IOException {
        PanneauReadOnly panneauReadOnly = cnc.getPanneau();
        int length = panneauReadOnly.getLongueur();;
        int width = panneauReadOnly.getLargeur();
        float deepness = panneauReadOnly.getEpaisseur();
        out.writeObject("X0 Y0\n" +
                "Y"+length +"\n" +
                "X"+ width +"\n" +
                "Y0\n" +
                "X0\n" +
                "Z"+ deepness+"\n" +
                "Y"+length +"\n" +
                "Z0\n" +
                "Z"+ deepness+"\n" +
                "X"+ width +" Y"+length +"\n" +
                "Z0\n" +
                "Z"+ deepness+"\n" +
                "Y0 \n" +
                "Z0\n" +
                "Z"+ deepness +"\n" +
                "X0 Y0\n");
    }

    private void addCutsToGcode(ObjectOutputStream out) throws IOException
    {
        List<CoupeReadOnly> cuts = cnc.getCoupe();
        for(int cpt=0; cpt< cuts.size(); cpt++)
        {
            if(cuts.get(cpt).getClass() == CoupeReguliere.class)
            {
                addRegularCutGcode(out, (CoupeReguliere) cuts.get(cpt));
            }
            else if(cuts.get(cpt).getClass() == CoupeRectangulaire.class)
            {
                addRectangleCutGcode(out, (CoupeRectangulaire) cuts.get(cpt));
            }
            else if(cuts.get(cpt).getClass() == CoupeBordure.class)
            {
                addBorderCutGcode(out, (CoupeBordure) cuts.get(cpt));
            }
            else
            {
                addLCutGcode(out, (CoupeL) cuts.get(cpt));
            }
        }
    }

    private void addRegularCutGcode(ObjectOutputStream out, CoupeReguliere coupeReguliere) throws IOException {
        Point origine = coupeReguliere.getPointOrigine();
        Point dest = coupeReguliere.getPointDestination();
        float boardDeepness = cnc.getPanneau().getEpaisseur();
        float deepness = boardDeepness - coupeReguliere.getProfondeur();
        out.writeObject("X" +origine.x + " Y"+ origine.y + "\n" +
                "Z"+deepness + "\n" +
                "X" +dest.x + " Y"+ dest.y + "\n" +
                "Z" + boardDeepness +"\n" +
                "X" +origine.x + " Y"+ origine.y + "\n");
    }

    private void addRectangleCutGcode(ObjectOutputStream out, CoupeRectangulaire coupeRectangulaire) throws IOException {
        Point origine = coupeRectangulaire.getPoint();
        Point opposee = coupeRectangulaire.getPointOppose();
        float boardDeepness = cnc.getPanneau().getEpaisseur();
        float deepness = boardDeepness - coupeRectangulaire.getProfondeur();
        out.writeObject("X" +origine.x + " Y"+ origine.y + "\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "Y"+opposee.y +"\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "X"+opposee.x +"\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "Y" + origine.y + "\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "X" + origine.x +"\n" +
                "Z"+deepness + "\n" +
                "Y"+opposee.y +"\n" +
                "X"+opposee.x +"\n" +
                "Y" + origine.y + "\n" +
                "X" + origine.x +"\n" +
                "Z"+boardDeepness+"\n");
    }

    private void addBorderCutGcode(ObjectOutputStream out, CoupeBordure coupeBordure) throws IOException {
        Point origine = new Point(coupeBordure.getDimensionFinale().width, coupeBordure.getDimensionFinale().height);
        Point opposee = new Point(cnc.getPanneau().getLargeur() - origine.x, cnc.getPanneau().getLongueur() - origine.y);
        float boardDeepness = cnc.getPanneau().getEpaisseur();
        float deepness = boardDeepness - coupeBordure.getProfondeur();
        out.writeObject("X" +origine.x + " Y"+ origine.y + "\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "Y"+opposee.y +"\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "X"+opposee.x +"\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "Y" + origine.y + "\n" +
                "Z"+deepness + "\n" +
                "Z"+boardDeepness + "\n" +
                "X" + origine.x +"\n" +
                "Z"+deepness + "\n" +
                "Y"+opposee.y +"\n" +
                "X"+opposee.x +"\n" +
                "Y" + origine.y + "\n" +
                "X" + origine.x +"\n" +
                "Z"+boardDeepness+"\n");
    }

    private void addLCutGcode(ObjectOutputStream out, CoupeL coupeL) throws IOException{
        Point origine = coupeL.getPoint();
        Point opposee = coupeL.getPointOppose();
        float boardDeepness = cnc.getPanneau().getEpaisseur();
        float deepness = boardDeepness - coupeL.getProfondeur();
        out.writeObject("X"+origine.x + " Y"+origine.y + "\n" +
                "X"+opposee.x+"\n" +
                "Z"+deepness+"\n" +
                "X"+origine.x + "\n" +
                "Z"+boardDeepness + "\n" +
                "Y"+opposee.y+"\n" +
                "Z"+deepness+"\n" +
                "Y"+origine.y + "\n" +
                "Z"+boardDeepness + "\n");
    }
}
