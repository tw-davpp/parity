package classification;

public interface Instance {

    public Attribute[] getAtrributes();

    public Concept getConcept();

    public void print();

    public Attribute getAttributeByName(String attrName);
}