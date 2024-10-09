package mapping;

import java.util.ArrayList;
import util.VerbAction;

public class Mapping {
    String className;
    ArrayList<VerbAction> verbAction = new ArrayList<>();

    public Mapping() {}
    public Mapping(String className, ArrayList<VerbAction> verbAction) {
        this.className = className;
        this.verbAction = verbAction;
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public ArrayList<VerbAction> getVerbAction() {
        return verbAction;
    }
    public void setVerbAction(ArrayList<VerbAction> verbAction) {
        this.verbAction = verbAction;
    }
}