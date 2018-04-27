package Demo;

import java.io.IOException;
import java.util.Map;

import org.openscience.cdk.Atom;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import com.google.common.collect.ListMultimap;

public class test {
 public static void main(String[] args) throws CDKException, IOException, CloneNotSupportedException {
	String path= "...\\mollist.sdf"; //The path of the sdf files including the molecule list
		
	//Simply creating an atom container.
	IAtomContainer acontainer = new org.openscience.cdk.AtomContainer();

        acontainer.addAtom(new Atom("C")); //1
        acontainer.addAtom(new Atom("C")); //2
        acontainer.addAtom(new Atom("C")); //3
        acontainer.addAtom(new Atom("H")); //4
        acontainer.addAtom(new Atom("H")); //5
        acontainer.addAtom(new Atom("H")); //6
        acontainer.addAtom(new Atom("H")); //7
        acontainer.addAtom(new Atom("H")); //8
        
        //Adding the hydrogen atom bonded bonds.
        acontainer.addBond(0, 3, IBond.Order.SINGLE);
        acontainer.addBond(1, 4, IBond.Order.SINGLE);
        acontainer.addBond(1, 5, IBond.Order.SINGLE);
        acontainer.addBond(2, 6, IBond.Order.SINGLE);
        acontainer.addBond(2, 7, IBond.Order.SINGLE);
        acontainer.addBond(0, 1, IBond.Order.SINGLE);
        acontainer.addBond(0, 2, IBond.Order.SINGLE);
        acontainer.addBond(1, 2, IBond.Order.SINGLE);
        
        
	//Reading the sdf file and creating and indexing atomcontainers
	Map<Integer, IAtomContainer> acs=Functions.sdf2ac(path);
			
	//Classifying  molecules based on their InChIs
	ListMultimap inch=Functions.inchiclass(path);
		
	//Generating generic SMILES for the atomcontainer; the from SMILES to InChI.
	String smiles= Functions.ac2smi(acontainer);
	String inchi= Functions.inchi(smiles);
		
	//From the source file, directly the list of InChIs can be also generated 
	//with atomcontainer indices.
	Map<Integer, String> map1= Functions.mol2inchilist(path);
		
	//Classify molecules based on their InChIs. Returning a map includes index
	//of atomcontainers and their InChIs as pairs. 
	ListMultimap<String, Integer> map2= Functions.inchiclass(path);
		
	//The identical molecules are detected from the InChI classification map.
	ListMultimap<String, IAtomContainer> equals= Functions.equalones(map2, path);
	}

}
