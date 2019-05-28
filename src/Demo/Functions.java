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

package mol2inchi;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openbabel.OBConversion;
import org.openbabel.OBMol;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class Functions {
	/**By taking the list of molecules stored in a file; 
	 * 
	 * 1. Atomcontainers are created from the mol list .
	 * 2. SMILES (generic) are generated for atomcontainers. 
	 * 3. By using openbabel, smiles are converted to InChI strings.
	 * 4. InChI based classification was performed by using previous functions.
	 * 5. The equal atom containers are returned from InChI classification.
	 * 
	 * Note: java.library.path sould be set to the directory including openbabel.jar and openbabel_java.dll files.
	 * 
	 * @param listt
	 * @throws CDKException
	 * @throws IOException
	 */
	
	//Reading the sdf file and creating acontainers
	public static Map<Integer, IAtomContainer> sdf2ac( String listt) throws CDKException, IOException {
		Map<Integer, IAtomContainer> acs= new HashMap<Integer, IAtomContainer>();  
		IteratingSDFReader iterator = new IteratingSDFReader(new FileReader(listt),SilentChemObjectBuilder.getInstance());
		int i=0;
		while (iterator.hasNext()) 
		{
			i++;
			IAtomContainer ac=iterator.next();
			acs.put(i, ac);
		}
		iterator.close();
	return acs;
	}
	
	public static String ac2smi(IAtomContainer acon) throws CDKException{
		SmilesGenerator gen = new SmilesGenerator(SmiFlavor.Generic);
		String          smi     = gen.create(acon); 
		return smi;
	}
	
	//From Smiles to Inchi
	// TODO: How to stop error: *** Open Babel Warning  in InChI code #0 :Omitted undefined stereo
	public static String inchi(String smiles) throws CloneNotSupportedException, CDKException, IOException{
		System.loadLibrary("openbabel_java");
		// Read molecule from SMILES string
	    OBConversion conv = new OBConversion();
	    OBMol mol = new OBMol();
	    conv.SetInFormat("smi");
	    conv.ReadString(mol, smiles);
	    
	    conv.SetOutFormat("inchi");
	    String inch=conv.WriteString(mol).replace("InChI=", "");
	    return inch;
	}
	
	//Takes the path of mol file to create inchi map; first creates acontainers then call inchi
	public static Map<Integer, String>  mol2inchilist (String path) throws CloneNotSupportedException, CDKException, IOException{
		Map<Integer, IAtomContainer> acs=(Functions.sdf2ac(path));
		Map<Integer, String> ac2inch= new HashMap<Integer, String>();  
		int i=0;
		for(Object key: acs.keySet()){
			i++;
			IAtomContainer ac=(IAtomContainer) acs.get(key);
			String smi =(Functions.ac2smi(ac));
			ac2inch.put((int)key, Functions.inchi(smi));
		}
		return ac2inch;
	}
	
	//TODO: Some other functions can be created from this function. Needs to be re-written.
	//Classify the equal acontainers based on their Inchi keys.
	//First I tried with atomcontainers for mol2inchilist but due to the atomcontainer equality check,
	//I changed to the index of atomcontainers.
	public static ListMultimap<String, Integer> inchiclass(String path) throws CloneNotSupportedException, CDKException, IOException{
		ListMultimap<String, Integer> rep = ArrayListMultimap.create();
		Map<Integer, String> acninc=Functions.mol2inchilist(path);
		for(Integer key1: acninc.keySet()){
			for(Integer key2: acninc.keySet()){//In 2 loops, ignore the identical acontainer and inchi pairs.
					String res1=acninc.get(key1);
					String res2=acninc.get(key2);
					if(res1.equals(res2)){
						if(!rep.get(res1).contains(key1)){
							rep.put(res1, key1);
						}
						if(!rep.get(res1).contains(key2)){
							rep.put(res1, key2);
						}
					}
			}
		}
		return rep;
	}
	
	//Equal atomcontainers based on their InChis are returned.
	public static ListMultimap<String, IAtomContainer> equalones(ListMultimap<String, Integer>  rep, String list) throws CloneNotSupportedException, CDKException, IOException{
		ListMultimap<String, IAtomContainer> equalones = ArrayListMultimap.create(); //Equal atomcontainers are ranked based on InChI.
		Map<Integer, IAtomContainer> acsmap=Functions.sdf2ac(list); //The index list of atomcontainers.
		for(String key: rep.keySet()){ 
			if(rep.get(key).size()!=1){ //InChI repetitions
				for(Integer ind: rep.get(key)){ //For these indices store as InChI,atomcontainer pairs
					equalones.put(key, acsmap.get(ind));
				}
			}
		}
		return equalones;
	}	
}
