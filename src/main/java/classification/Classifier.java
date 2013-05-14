package classification;

public interface Classifier {

    public String getName();

    public boolean train();

    public Concept classify(Instance instance);
}
