import java.util.*;
import java.util.stream.Collectors;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans
    public  List<Molecule> human = new ArrayList<>();
    public  List<Molecule> vitale = new ArrayList<>();

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        // Gather all molecules with the lowest bond strength from each structure
        List<Molecule> selectedMolecules = new ArrayList<>();
        Map<Molecule, Integer> structureMap = new HashMap<>();  // Map each molecule to its structure index
        int index = 0;
        // For human structures
        for (MolecularStructure structure : humanStructures) {
            Molecule lowest = structure.getMolecules().stream()
                    .min(Comparator.comparingInt(Molecule::getBondStrength))
                    .orElseThrow(NoSuchElementException::new);
            selectedMolecules.add(lowest);
            human.add(lowest);
            structureMap.put(lowest, index++);
        }
        // For unique Vitales structures
        for (MolecularStructure structure : diffStructures) {
            Molecule lowest = structure.getMolecules().stream()
                    .min(Comparator.comparingInt(Molecule::getBondStrength))
                    .orElseThrow(NoSuchElementException::new);
            selectedMolecules.add(lowest);
            vitale.add(lowest);
            structureMap.put(lowest, index++);
        }

        // Calculate all potential bonds and select the minimum necessary to connect all structures
        List<Bond> allBonds = new ArrayList<>();
        for (int i = 0; i < selectedMolecules.size(); i++) {
            for (int j = i + 1; j < selectedMolecules.size(); j++) {
                Molecule m1 = selectedMolecules.get(i);
                Molecule m2 = selectedMolecules.get(j);
                double bondStrength = (m1.getBondStrength() + m2.getBondStrength()) / 2.0;
                allBonds.add(new Bond(m1, m2, bondStrength));
            }
        }

        // Sort bonds by strength
        allBonds.sort(Comparator.comparingDouble(Bond::getWeight));

        // union-find algorithm to ensure all structures are connected
        List<Bond> selectedBonds = new ArrayList<>();

        int[] parent = new int[humanStructures.size() + diffStructures.size()];
        for (int i = 0; i < parent.length; i++) parent[i] = i;

        for (Bond bond : allBonds) {
            int root1 = find(structureMap.get(bond.getFrom()), parent);
            int root2 = find(structureMap.get(bond.getTo()), parent);
            if (root1 != root2) {
                selectedBonds.add(bond);
                parent[root1] = root2;  // Union the sets
            }
            if (selectedBonds.size() >= parent.length - 1) break;  // Only need parent.length - 1 bonds
        }

        return selectedBonds;
    }

    private int find(int node, int[] parent) {
        if (parent[node] != node) {
            parent[node] = find(parent[node], parent);
        }
        return parent[node];
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {
        System.out.println("Typical human molecules selected for synthesis: " + (human));
        System.out.println("Vitales molecules selected for synthesis: " + (vitale));
        System.out.println("Synthesizing the serum...");

        double totalBondStrength = 0.0;
        for (Bond bond : serum) {
            Molecule m1 = bond.getFrom();
            Molecule m2 = bond.getTo();
            // Ensure molecules are printed in the correct order
            if (m1.getId().compareTo(m2.getId()) > 0) {
                Molecule temp = m1;
                m1 = m2;
                m2 = temp;
            }
            System.out.printf("Forming a bond between %s - %s with strength %.2f%n", m1.getId(), m2.getId(), bond.getWeight());
            totalBondStrength += bond.getWeight();
        }
        System.out.println("The total serum bond strength is " + String.format("%.2f", totalBondStrength));
    }
}