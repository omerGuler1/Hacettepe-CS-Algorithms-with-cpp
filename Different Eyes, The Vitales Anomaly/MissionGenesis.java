import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    // parse the XML into a Document object, which represents the entire XML document as a tree.
    public void readXML(String filename) {
        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); // configures and creates DocumentBuilder instances
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); // used to parse XML documents and create Document objects from them
            Document doc = dBuilder.parse(inputFile); // representing the entire XML document after parsing.

            // consolidate adjacent text nodes and ensure the structure is as compact as possible.
            doc.getDocumentElement().normalize();

            NodeList humanList = doc.getElementsByTagName("HumanMolecularData").item(0).getChildNodes();
            NodeList vitalesList = doc.getElementsByTagName("VitalesMolecularData").item(0).getChildNodes();

            this.molecularDataHuman = new MolecularData(parseMolecularData(humanList));
            this.molecularDataVitales = new MolecularData(parseMolecularData(vitalesList));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // takes a NodeList, which represents a collection of XML elements, and parses it into a List of Molecule objects
    private List<Molecule> parseMolecularData(NodeList nodeList) {
        List<Molecule> molecules = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            // extract the ID, BondStrength, and a list of bonded molecule IDs (Bonds)
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(element.getElementsByTagName("BondStrength").item(0).getTextContent());

                NodeList bondsList = element.getElementsByTagName("Bonds").item(0).getChildNodes();
                List<String> bonds = new ArrayList<>();
                for (int j = 0; j < bondsList.getLength(); j++) {
                    Node bondNode = bondsList.item(j);
                    if (bondNode.getNodeType() == Node.ELEMENT_NODE) {
                        bonds.add(bondNode.getTextContent());
                    }
                }

                Molecule molecule = new Molecule(id, bondStrength, bonds);
                molecules.add(molecule);
            }
        }
        return molecules;
    }

}
