package classes;


/**
 * Classe representant les diff�rents attrinuts d'un mot
 * @author m2ice-2
 *
 */
public class Mot 
{
	String _chaine;
	String _path;
	String _nomDoc;
	Integer _position;


	public Mot(String _chaine, String _path, String _nomDoc, int _position) 
	{
		super();
		this._chaine = _chaine;
		this._path = _path;
		this._nomDoc = _nomDoc;
		this._position = _position;
	}


	public String get_chaine() {
		return _chaine;
	}


	public void set_chaine(String _chaine) {
		this._chaine = _chaine;
	}


	public String get_path() {
		return _path;
	}


	public void set_path(String _path) {
		this._path = _path;
	}


	public String get_nomDoc() {
		return _nomDoc;
	}


	public void set_nomDoc(String _nomDoc) {
		this._nomDoc = _nomDoc;
	}


	public Integer get_position() {
		return _position;
	}


	public void set_position(Integer _position) {
		this._position = _position;
	}

}