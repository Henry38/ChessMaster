package data;

import java.util.LinkedList;

import coup.Coup;

public class HistoricModel {
	
	private LinkedList<Coup> listCoup;
	private int index;
	
	/** Constructeur */
	public HistoricModel() {
		super();
		this.listCoup = new LinkedList<Coup>();
		this.index = -1;
	}
	
	/** Retourne le nombre de coups dans l'historique*/
	public int getSize() {
		return listCoup.size();
	}
	
	/** Retourne l'index courant dans l'historique */
	public int getIndex() {
		return index;
	}
	
	/** Retourne l'element situe a l'index */
	public Coup getElementAt(int index) {
		return listCoup.get(index);
	}
	
	/** Retourne vrai si l'index de l'historique est sur le dernier element */
	public boolean isIndexOnLast() {
		return (getIndex() == getSize()-1);
	}
	
	/** Recule l'index de 1 */
	public void moveBackward() {
		if (getIndex() >= 0) {
			index--;
		}
	}
	
	/** Avance l'index de 1 */
	public void moveForeward() {
		if (getIndex() < getSize()-1) {
			index++;
		}
	}
	
	/** Ajoute un coup dans l'historique */
	public void add(Coup coup) {
		listCoup.add(coup);
		moveForeward();
	}
	
	/** Enleve le dernier coup de l'historique qui doit etre retire du plateau */
	public Coup remove() {
		Coup coup = listCoup.pollLast();
		moveBackward();
		return coup;
	}
	
	/** Nettoie l'historique */
	public void clear() {
		listCoup.clear();
		index = -1;
	}
}
