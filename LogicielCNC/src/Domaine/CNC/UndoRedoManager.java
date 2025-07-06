package LogicielCNC.src.Domaine.CNC;
import LogicielCNC.src.Domaine.CNC.PanneauManager;
import LogicielCNC.src.Domaine.PanneauReadOnly;
import LogicielCNC.src.Domaine.Panneau;

import java.io.*;
import java.util.Stack;



public class UndoRedoManager {
    private final Stack<PanneauReadOnly> undoStack = new Stack<>();
    private final Stack<PanneauReadOnly> redoStack = new Stack<>();

    public void saveState(PanneauReadOnly state){
        undoStack.push(deepCopy(state));
        redoStack.clear();
    }

    public PanneauReadOnly undo(PanneauReadOnly currentState){
        if (!undoStack.isEmpty()){
            redoStack.push(deepCopy(currentState));
            return undoStack.pop();
        }
        return null;
    }

    public PanneauReadOnly redo() {
        if (!redoStack.isEmpty()) {
            PanneauReadOnly state = redoStack.pop();
            undoStack.push(deepCopy(state));
            return state;
        }
        return null;
    }

    private PanneauReadOnly deepCopy(PanneauReadOnly panneau) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(panneau);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (PanneauReadOnly) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur lors de la copie", e);
        }
    }

    public void saveToFile(PanneauReadOnly panneau, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(panneau);
        }
    }

    public PanneauReadOnly loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Panneau) ois.readObject();
        }
    }
}



