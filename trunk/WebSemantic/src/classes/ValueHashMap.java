package classes;

import java.util.Vector;

public abstract class ValueHashMap {
	private String _classe;
	private Vector<String> _listValues;
	
	public ValueHashMap(String classe){
		_classe = classe;
		_listValues = new Vector<String>();
	}
	
	public void addValue(String val){
		_listValues.add(val);
	}

	public String get_classe() {
		return _classe;
	}

	public Vector<String> get_listValues() {
		return _listValues;
	}
}
