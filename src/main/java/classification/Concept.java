package classification;

public interface Concept {

    public String getName();

    public Concept getParent();

    public Instance[] getInstances();
}