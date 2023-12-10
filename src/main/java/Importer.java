/*
 * Class to import an .owl file and create a parent child edge set list needed to calculate the KGS. Based heavily
 * upon the work of Dr. Daniel Schlegel (GitHub: digitalneoplasm) from the parent project of this that I also
 * worked on.
 */

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Importer {

    // LABEL_NAMING_MODE decides not to use IRIs in the LaptopCorrect, instead naming terms after their labels.
    private static final boolean LABEL_NAMING_MODE = true;

    private static final Map<String, String> iriLabelMap = new HashMap<>();

    public static void main(String[] args) throws OWLOntologyCreationException, IOException {
        OWLOntology ontology = loadOntology("src/main/resources/bfo.owl");

        FileWriter writer = new FileWriter("src/main/resources/output");

        // Write OWL Subclass / Superclass Data //
        List<OWLClass> classes = ontology.classesInSignature().filter(c -> !isDeprecated(c, ontology)).toList();
        List<OWLObjectProperty> properties = ontology.getObjectPropertiesInSignature().stream()
                .filter(c -> !isDeprecated(c, ontology)).toList();

        // get a reasoner for the ontology
        OWLReasoner reasoner = (new StructuralReasonerFactory()).createReasoner(ontology);

        /*
        go through each class and create the edge list file that we are looking for
         */

        if(LABEL_NAMING_MODE) resolveLabels(classes, properties, ontology);

        // for every class in the ontology
        for(OWLClass c : classes){
            // for every subclass write the relation of the two into the file
            for(OWLClass sub : getDirectSubclasses(c, reasoner)){
                String cl = c.getIRI() + "";
                String su = sub.getIRI() + "";
                writer.write(cl.substring(cl.indexOf("#") + 1) + " " +
                        su.substring(su.indexOf("#") + 1) + "\n");
            }
        }

        writer.close();
    }

    public static boolean isDeprecated(OWLEntity oc, OWLOntology ont){
        if (oc.getIRI().getShortForm().contains("ObsoleteClass"))
            return true;
        for(OWLAnnotationAssertionAxiom oaaa : ont.getAnnotationAssertionAxioms(oc.getIRI())){
            if (oaaa.getProperty().getIRI().getShortForm().equals("IAO_0100001")) // term replaced by
                return true;
        }
        return ont.getAnnotationAssertionAxioms(oc.getIRI()).stream().anyMatch(a
                -> a.getProperty().isDeprecated() && a.getValue() instanceof OWLLiteral &&
                ((OWLLiteral) a.getValue()).getLiteral().equals("true"));
    }

    // takes in a filename and creates an ontology from the given owl document
    public static OWLOntology loadOntology(String inputFilename) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager.loadOntologyFromOntologyDocument(new File(inputFilename));
    }

    // get the subclasses of a class in the onotlogy
    public static List<OWLClass> getDirectSubclasses(OWLClassExpression oce, OWLReasoner reasoner){
        return reasoner.getSubClasses(oce, true).entities().filter(oc -> !isNothingClass(oc)).toList();
    }

    // checks if the class is the BFO nothing?
    public static boolean isNothingClass(OWLClass oc){
        return oc.getIRI().getShortForm().equals("Nothing");
    }

    public static void resolveLabels(List<OWLClass> classes, List<OWLObjectProperty> properties, OWLOntology ont){
        for (OWLClass c : classes){
            for(OWLAnnotationAssertionAxiom oaaa : ont.getAnnotationAssertionAxioms(c.getIRI()))
                if (oaaa.getProperty().getIRI().getShortForm().equals("label")) {
                    String oaaastr = oaaa.getValue().toString();
                    int langat = oaaastr.lastIndexOf("@");
                    int typestr = oaaastr.lastIndexOf("^^xsd");
                    oaaastr = (langat > -1) ? oaaastr.substring(0, langat) : oaaastr;
                    oaaastr = (typestr > -1 && typestr > langat && typestr <= oaaastr.length()) ?
                            oaaastr.substring(0, typestr) : oaaastr;
                    iriLabelMap.put(c.getIRI().getShortForm(), oaaastr.toLowerCase());
                }
        }
        for (OWLObjectProperty p : properties){
            for(OWLAnnotationAssertionAxiom oaaa : ont.getAnnotationAssertionAxioms(p.getIRI()))
                if (oaaa.getProperty().getIRI().getShortForm().equals("label")) {
                    String oaaastr = oaaa.getValue().toString();
                    int langat = oaaastr.lastIndexOf("@");
                    int typestr = oaaastr.lastIndexOf("^^xsd");
                    oaaastr = (langat > -1) ? oaaastr.substring(0, langat) : oaaastr;
                    oaaastr = (typestr > -1 && typestr > langat && typestr <= oaaastr.length()) ?
                            oaaastr.substring(0, typestr) : oaaastr;
                    iriLabelMap.put(p.getIRI().getShortForm(), oaaastr.toLowerCase());
                }
        }

    }
}
