package LogicielCNC.src.Domaine;
import javax.swing.*;
import java.util.Stack;

import javax.swing.*;
import java.io.*;
import java.util.Stack;
import LogicielCNC.src.Domaine.Action;
import LogicielCNC.src.Domaine.PanneauMemento;

public class UndoRedo {
    private Stack<Action> undoStack = new Stack<>();
    private Stack<Action> redoStack = new Stack<>();


    public void reset(Action panneau) {
        undoStack.clear();
        redoStack.clear();
    }


    // Ajouter action à la pile undo
    public void addAction(Action action) {
        undoStack.push(action);
        redoStack.clear();// Vide la pile redo lors d'une nouvelle action
    }

    // Annuler une dernière action
    public void undo() {
        if (canUndo()) {
            Action a = undoStack.pop();
            //a.undo();
            redoStack.push(a);
        }
    }
    // Rétablir la dernière action annulée
    public void redo() {
       // if (canRedo()) {
            Action b = redoStack.pop();
            b.execute();
            undoStack.push(b); // Ajouter à la pile undo
       // }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }   // Vérifier si on peut annuler une action

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }    // Vérifier si on peut rétablir une action

    public Panneau clone(Panneau p){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Panneau clone = (Panneau) ois.readObject();
            ois.close();
            return clone;
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMemento(PanneauMemento panneau) {
        Action saveAction = new SavePanneauAction(panneau);
        addAction(saveAction);  // Ajouter l'action à l'historique
    }

}

