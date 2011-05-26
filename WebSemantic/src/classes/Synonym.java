package classes;

import java.util.Vector;

public class Synonym {
	private String _classe;
	private Vector<String> _synonymes;
	
	public Synonym(String classe){
		_classe = classe;
		_synonymes = new Vector<String>();
	}
	
	public void addSynonym(String syn){
		_synonymes.add(syn);
	}

	public String get_classe() {
		return _classe;
	}

	public Vector<String> get_synonymes() {
		return _synonymes;
	}
}
