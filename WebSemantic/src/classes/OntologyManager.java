package classes;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OntologyManager {
	private OWLOntologyManager _manager;
	private OWLOntology _ontology;
	private OWLDataFactory _factory;
	private HashMap<String, Synonym> _concepts = new HashMap<String, Synonym>();
	private HashMap<String, Instance> _instances = new HashMap<String, Instance>();
	
	public OntologyManager(){
		try {
			_manager = OWLManager.createOWLOntologyManager();
			
			// We load the ontology 
			File onto = new File("resources/OntoBaladev2.owl");
			
			// Now ask the manager to load the ontology
			_ontology = _manager.loadOntologyFromOntologyDocument(onto);
			
			// We can get a reference to a data factory from an OWLOntologyManager
			_factory = _manager.getOWLDataFactory();			
			
			OWLAnnotationProperty label = _factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
			
			// récupération des synonymes et des instances
			for(OWLClass cls : _ontology.getClassesInSignature()){
				// déclaration d'un nouvel objet synonyme pour stocker les synonymes de classe de l'ontologie
				Synonym s = new Synonym(cls.toString());
				
				// Get the annotations on the class that use the label property
				for (OWLAnnotation annotation : cls.getAnnotations(_ontology, label)) {
					if (annotation.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) annotation.getValue();
						// on regarde si la classe est déjà présente dans la hashMap
						s.addValue(val.getLiteral());
					}
				}
				
				// ajout du concept avec ses synonymes dans le vecteur de concepts
				_concepts.put(cls.toString(), s);
				
				
				Instance inst = new Instance(cls.toString());
				for(OWLIndividual ind : cls.getIndividuals(_ontology)){					
					for(OWLNamedIndividual namedInd : ind.getIndividualsInSignature()){
						for(OWLAnnotation ann : namedInd.getAnnotations(_ontology)){
							OWLLiteral val = (OWLLiteral) ann.getValue();
							inst.addValue(val.getLiteral());
						}
					}					
				}
				
				// ajout de l'instance avec ses annotations
				_instances.put(cls.toString(), inst);
			}
		}
		catch (OWLOntologyCreationException e) {
			System.out.println("The ontology could not be created: " + e.getMessage());
		}
	}
	
	public HashMap<String, Synonym> getConcepts(){					
		return _concepts;
	}
	
	public HashMap<String, Instance> getInstances(){		
		return _instances;
	}
	
	public Instance getInstanceByConcept(String concept){
		for(Entry<String, Instance> entry : _instances.entrySet()){
			Instance val = entry.getValue();
			if(val.get_classe().equalsIgnoreCase(concept)){
				return val;
			}
		}
		return new Instance(concept);
	}
	
	public Synonym getSynonymByMot(String mot){
		Vector<String> vec = new Vector<String>();
		for(Entry<String, Synonym> entry : _concepts.entrySet()){
			Synonym val = entry.getValue();
			vec = val.get_listValues();
			if(vec.contains(mot)){
				return val;
			}
		}
		return new Synonym(mot);
	}
	
	public static void main(String[] args) {
		OntologyManager o = new OntologyManager();
		
		Synonym test3 = o.getSynonymByMot("département");
		for(String s : test3.get_listValues()){
			System.out.println(s);
		}
		
		Instance test4 = o.getInstanceByConcept(test3.get_classe());
		for(String s : test4.get_listValues()){
			System.out.println(s);
		}
	}
}

