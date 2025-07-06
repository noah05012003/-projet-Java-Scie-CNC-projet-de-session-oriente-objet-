package LogicielCNC.src.Domaine;
import LogicielCNC.src.Domaine.AddCutAction;

public class SavePanneauAction implements Action {
    private PanneauMemento memento;
    private AddCutAction addCutAction;

    public SavePanneauAction(PanneauMemento memento) {
        this.memento = memento;
        this.addCutAction = addCutAction;
    }

    @Override
    public void execute() {
        addCutAction.execute();
    }

    @Override
    public void undo() {
        addCutAction.undo();
    }
}


