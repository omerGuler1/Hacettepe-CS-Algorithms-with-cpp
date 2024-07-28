import java.util.*;
import java.util.stream.Collectors;

// Class representing a molecular structure
public class MolecularStructure {

    // Private field
    private final List<Molecule> molecules = new ArrayList<>(); // List of molecules in the structure

    // Method to check if a molecule exists in the structure
    public boolean hasMolecule(String moleculeId) {
        return molecules.stream().map(Molecule::getId).collect(Collectors.toList()).contains(moleculeId);
    }

    // Method to add a molecule to the structure
    public void addMolecule(Molecule molecule) {
        molecules.add(molecule);
    }

    public void addMolecules(Set<Molecule> moleculesToAdd) {
        molecules.addAll(moleculesToAdd);
    }

    // This method checks if any molecule in the structure is connected to the given molecule
    public boolean isConnected(Molecule molecule) {
        Set<String> ids = molecules.stream().map(Molecule::getId).collect(Collectors.toSet());
        for (String id : molecule.getBonds()) {
            if (ids.contains(id)) {
                return true;
            }
        }
        return false;
    }

    // Getter for molecules in the structure
    public List<Molecule> getMolecules() {
        return molecules;
    }
    public boolean containsMolecule(Molecule molecule) {
        return this.getMolecules().contains(molecule);
    }

    // Method to get the easiest-to-bond molecule in the structure
    public Molecule getMoleculeWithWeakestBondStrength() {
        return molecules.stream().min(Comparator.comparing(Molecule::getBondStrength)).orElse(null);
    }

    // Override equals method to compare structures
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MolecularStructure other = (MolecularStructure) obj;

        Set<String> thisMoleculeIds = this.molecules.stream()
                .map(Molecule::getId)
                .collect(Collectors.toSet());

        Set<String> otherMoleculeIds = other.molecules.stream()
                .map(Molecule::getId)
                .collect(Collectors.toSet());

        return thisMoleculeIds.equals(otherMoleculeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(molecules.stream().map(Molecule::getId).sorted().collect(Collectors.toList()));
    }


    // Override toString method to provide a string representation of the structure
    @Override
    public String toString() {
        ArrayList<Molecule> sortedMolecules = new ArrayList<>(molecules);
        Collections.sort(sortedMolecules);
        return sortedMolecules.toString();
    }


}
