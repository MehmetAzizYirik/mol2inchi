# mol2inchi
InChI generation and Molecular Classification Based on InChI.

The java file, Functions.java, comprises functions coded for InChI
generation, and duplicate checkers based on InChIs. Thus, a list of
molecules stored in an sdf file can  be converted to IAtomContainers
(IAtomContainer from CDK); then the SMILES and InChIs can be generated
by using the atomcontainers. The molecules are classified and stored
in maps regarding to their InChIs. These maps ease the detection of
identical molecules.

For the usage of these classes, the main requirements are CDK
(Chemistry Development Kit) and OpenBabel libraries. For the latter,
the java.library.path should be set to the directory of OpenBabel where
the openbabel.jar and openbabel_java.dll files are stored on your machine.
