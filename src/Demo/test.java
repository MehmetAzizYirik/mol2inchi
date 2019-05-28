/**
 * MIT License
 *
 * Copyright (c) 2018 Mehmet Aziz Yirik
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


/**
 * This class is for the generation and classification of InChI values for a list
 * of mol files.
 * 
 * @author Mehmet Aziz Yirik
 */

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
