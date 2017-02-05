package data;

import java.util.LinkedList;

import coup.Coup;

public class HistoricModel {
	
	private LinkedList<Coup> listCoup;
	private boolean cpuTurn, endGame;
	private int index;
	
	/** Constructeur */
	public HistoricModel() {
		super();
		this.listCoup = new LinkedList<Coup>();
		this.cpuTurn = false;
		this.endGame = true;
		this.index = -1;
	}
	
	/** Retourne c'est un joueur humain qui a le trait d'apres le dernier coup joue */
	public boolean isCpuTurn() {
		return cpuTurn;
	}
	
	/** Retourne vrai si la partie est finie d'apres le dernier coup joue sur l'historique */
	public boolean isEndGame() {
		return endGame;
	}
	
	/** Retourne le nombre de coups dans l'historique */
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
		return (getSize() >= 0 && getIndex() == getSize()-1);
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
	
	/** Met a jour si l'ordinateur a le trait */
	public void setCpuTurn(boolean cpuTurn) {
		this.cpuTurn = cpuTurn;
	}
	
	/** Met a jour la fin de la partie */
	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}
	
	/** Nettoie l'historique */
	public void clear() {
		listCoup.clear();
		cpuTurn = false;
		endGame = true;
		index = -1;
	}
}
