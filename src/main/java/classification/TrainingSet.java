package classification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class TrainingSet implements Serializable {

    private static final long serialVersionUID = 4754213130190809633L;

    private boolean verbose = false;

    private HashMap<Integer, Instance> instanceSet;
    private HashSet<Concept> conceptSet;
    private HashSet<String> attributeNameSet;

    public TrainingSet() {
        instanceSet = new HashMap<Integer, Instance>();
    }

    public TrainingSet(Instance[] instances) {
        int instanceId = 0;

        instanceSet = new HashMap<Integer, Instance>();
        conceptSet = new HashSet<Concept>();
        attributeNameSet = new HashSet<String>();

        Concept concept;
        for (Instance instance : instances) {
            instanceSet.put(instanceId, instance);

            concept = instance.getConcept();
            if (!conceptSet.contains(concept)) {
                conceptSet.add(concept);
            }

            for (Attribute attribute : instance.getAtrributes()) {
                if (attribute != null) {
                    attributeNameSet.add(attribute.getName());
                }
            }

            instanceId++;
        }

        if (verbose) {
            System.out.println("-------------------------------------------------------------");
            System.out.print("Loaded " + getSize() + " instances that belong into ");
            System.out.println(this.getNumberOfConcepts() + " concepts");
            System.out.println("-------------------------------------------------------------");
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HashMap<Integer, Instance> getInstances() {
        return instanceSet;
    }

    public Instance getInstance(int index) {
        return instanceSet.get(index);
    }

    public int getSize() {
        return instanceSet.size();
    }

    public int getNumberOfConcepts() {
        return conceptSet.size();
    }

    public void print() {

        for (Instance i : instanceSet.values()) {
            i.print();
        }
    }

    public HashSet<Concept> getConceptSet() {
        return conceptSet;
    }

    public HashSet<String> getAttributeNameSet() {
        return attributeNameSet;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
