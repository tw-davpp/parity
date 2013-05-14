package classification;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayes implements Classifier {
    private String name;
    protected TrainingSet tSet;
    protected Map<Concept, Double> conceptPriors;
    protected Map<Concept, Map<Attribute, AttributeValue>> p;
    protected ArrayList<String> attributeList;
    protected boolean verbose = false;

    public NaiveBayes(String name, TrainingSet set) {
        this.name = name;
        tSet = set;

        conceptPriors = new HashMap<Concept, Double>(tSet.getNumberOfConcepts());
        verbose = false;
    }

    public Concept classify(Instance instance) {
        Concept bestConcept = null;
        double bestP = 0.0;

        if (tSet == null || tSet.getConceptSet().size() == 0) {
            throw new RuntimeException("You have to train classifier first.");
        }
        if (verbose) {
            System.out.println("\n*** Classifying instance: " + instance.toString() + "\n");
        }
        for (Concept c : tSet.getConceptSet()) {
            double p = getProbability(c, instance);
            if (verbose) {
                System.out.printf("P(%s|%s) = %.15f\n", c.getName(), instance.toString(), p);
            }
            if (p >= bestP) {
                bestConcept = c;
                bestP = p;
            }
        }
        return bestConcept;
    }

    public boolean train() {
        long t0 = System.currentTimeMillis();
        boolean hasTrained = false;
        if (attributeList == null || attributeList.size() == 0) {
            System.out.print("Can't train the classifier without specifying the attributes for training!");
            System.out.print("Use the method --> trainOnAttribute(Attribute a)");
        } else {
            calculateConceptPriors();
            calculateConditionalProbabilities();
            hasTrained = true;
        }

        if (verbose) {
            System.out.print("       Naive Bayes training completed in ");
            System.out.println((System.currentTimeMillis() - t0) + " (ms)");
        }

        return hasTrained;
    }

    public void trainOnAttribute(String aName) {
        if (attributeList == null) {
            attributeList = new ArrayList<String>();
        }
        attributeList.add(aName);
    }

    private void calculateConceptPriors() {
        for (Concept c : tSet.getConceptSet()) {
            //Calculate the priors for the concepts
            int totalConceptCount = 0;
            for (Instance i : tSet.getInstances().values()) {

                if (i.getConcept().equals(c)) {
                    totalConceptCount++;
                }
            }
            conceptPriors.put(c, new Double(totalConceptCount));
        }
    }

    protected void calculateConditionalProbabilities() {
        p = new HashMap<Concept, Map<Attribute, AttributeValue>>();
        for (Instance i : tSet.getInstances().values()) {
            for (Attribute a : i.getAtrributes()) {
                if (a != null && attributeList.contains(a.getName())) {
                    if (p.get(i.getConcept()) == null) {
                        p.put(i.getConcept(), new HashMap<Attribute, AttributeValue>());
                    }

                    Map<Attribute, AttributeValue> aMap = p.get(i.getConcept());
                    AttributeValue aV = aMap.get(a);
                    if (aV == null) {
                        aV = new AttributeValue(a.getValue());
                        aMap.put(a, aV);
                    } else {
                        aV.count();
                    }
                }
            }
        }
    }

    public double getProbability(Concept c, Instance i) {
        double cP = 0;
        if (tSet.getConceptSet().contains(c)) {
            cP = (getProbability(i, c) * getProbability(c)) / getProbability(i);
        } else {
            // We have never seen this concept before
            // assign to it a "reasonable" value
            cP = 1 / (tSet.getNumberOfConcepts() + 1.0);
        }
        return cP;
    }

    public double getProbability(Instance i) {
        double cP = 0;
        for (Concept c : getTset().getConceptSet()) {
            cP += getProbability(i, c) * getProbability(c);
        }
        return (cP == 0) ? (double) 1 / tSet.getSize() : cP;
    }

    public double getProbability(Concept c) {
        Double trInstanceCount = conceptPriors.get(c);
        if (trInstanceCount == null) {
            trInstanceCount = 0.0;
        }
        return trInstanceCount / tSet.getSize();
    }

    public double getProbability(Instance i, Concept c) {
        double cP = 1;
        for (Attribute a : i.getAtrributes()) {
            if (a != null && attributeList.contains(a.getName())) {
                Map<Attribute, AttributeValue> aMap = p.get(c);
                AttributeValue aV = aMap.get(a);
                if (aV == null) {
                    // the specific attribute value is not present for the current concept.
                    // Can you justify the following estimate?
                    // Can you think of a better choice?
                    cP *= ((double) 1 / (tSet.getSize() + 1));
                } else {
                    cP *= (aV.getCount() / conceptPriors.get(c));
                }
            }
        }

        return (cP == 1) ? (double) 1 / tSet.getNumberOfConcepts() : cP;
    }

    public String getName() {
        return name;
    }

    public TrainingSet getTset() {
        return tSet;
    }
}
