package classes;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OntologyManager {
	OWLOntologyManager _manager;
	OWLOntology _ontology;
	OWLDataFactory _factory;
	
	public OntologyManager(){
		try {
			_manager = OWLManager.createOWLOntologyManager();
			
			// We load the ontology 
			File onto = new File("resources/OntoBaladev2.owl");
			
			// Now ask the manager to load the ontology
			_ontology = _manager.loadOntologyFromOntologyDocument(onto);
			
			// We can get a reference to a data factory from an OWLOntologyManager
			_factory = _manager.getOWLDataFactory();
		}
		catch (OWLOntologyCreationException e) {
			System.out.println("The ontology could not be created: " + e.getMessage());
		}
	}
	
	public HashMap<String, String> getConcepts(){
		HashMap<String, String> concepts = new HashMap<String, String>();
					
		OWLAnnotationProperty label = _factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		
		for(OWLClass cls : _ontology.getClassesInSignature()){
			// Get the annotations on the class that use the label property
			for (OWLAnnotation annotation : cls.getAnnotations(_ontology, label)) {
				if (annotation.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) annotation.getValue();
						if (val.hasLang("fr")) {
							concepts.put(cls.toString(), val.getLiteral());
					}
				}
			}
		}
		
		return concepts;
	}
	
	public static void main(String[] args) {
		OntologyManager o = new OntologyManager();
		HashMap<String, String> test;
		test = o.getConcepts();
		
		for(Entry<String, String> entry : test.entrySet()) {
		    String cle = entry.getKey();
		    String valeur = entry.getValue();
		    System.out.println(cle+" => "+valeur);
		}
	}
}

