package LogicielCNC.src.Domaine.Coupes;

import LogicielCNC.src.Domaine.OutilReadOnly;

import java.awt.*;

public abstract class Coupes implements CoupeReadOnly {
    protected float profondeur;
    public TypeCoupe type;
    protected OutilReadOnly tool;

    public Coupes(float profondeur, TypeCoupe type, OutilReadOnly tool) {
        this.profondeur = profondeur;
        this.type = type;
        this.tool = tool;
    }

    public float getProfondeur() {
        return profondeur;
    }

    public int getToolWidth()
    {
        return tool.getLargeur();
    }

    public String getToolName(){
        return tool.getNom();
    }

    public boolean hasTool()
    {
        return tool != null;
    }

    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }

    public TypeCoupe getType() {
        return type;
    }

    public abstract boolean isPointInside(Point point);

    public abstract boolean isSameCut(CoupeReadOnly coupeReadOnly);





    /*
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
     */
}
