package LogicielCNC.src.Domaine;

/* interface Action qui définira les méthodes execute() et undo() pour
chaque action qu'on veut effectuer.*/

public interface Action {
    void execute();
    void undo();
}
