import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data

    public List<MolecularStructure> identifyMolecularStructures() {
        List<MolecularStructure> structures = new ArrayList<>();
        Set<Molecule> unvisited = new HashSet<>(molecules);

        while (!unvisited.isEmpty()) {
            Set<Molecule> visited = new HashSet<>();
            Set<Molecule> nextMolecules = new HashSet<>();
            nextMolecules.add(unvisited.iterator().next());

            while (!nextMolecules.isEmpty()) {
                Molecule current = nextMolecules.iterator().next();
                nextMolecules.remove(current);
                visited.add(current);

                for (Molecule m : unvisited) {
                    if (current.getBonds().contains(m.getId()) || m.getBonds().contains(current.getId())) {
                        nextMolecules.add(m);
                    }
                }
                unvisited.removeAll(nextMolecules);
            }

            MolecularStructure newStructure = new MolecularStructure();
            newStructure.addMolecules(visited);
            structures.add(newStructure);
        }
        return structures;
    }


    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        int count = 1;
        for (MolecularStructure structure : molecularStructures) {
            List<Molecule> sortedMolecules = new ArrayList<>(structure.getMolecules());
            Collections.sort(sortedMolecules);
            System.out.println("Molecules in Molecular Structure " + count + ": " + sortedMolecules);
            count++;
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        Set<MolecularStructure> sourceSet = new HashSet<>(sourceStructures);

        for (MolecularStructure target : targetStructures) {
            if (!sourceSet.contains(target)) {
                anomalyList.add(target);
            }
        }
        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure);
        }
    }
}