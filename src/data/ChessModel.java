package data;

import java.util.ArrayList;
import javax.swing.event.EventListenerList;

import piece.Piece;
import piece.Roi;
import controler.ChessModelListener;
import coup.Coup;

public abstract class ChessModel {
	
	protected HistoricModel historicModel;
	protected EventListenerList listenerList;
	
	/** Constructeur */
	public ChessModel() {
		this.historicModel = new HistoricModel();
		this.listenerList = new EventListenerList();
	}
	
	public abstract ChessModel clone();
	
	/** Retourne le modele d'historique */
	public HistoricModel getHistoricModel() {
		return historicModel;
	}
	
	/** Ajoute un listener selection sur le modele */
	public void addChessListener(ChessModelListener l) {
		listenerList.add(ChessModelListener.class, l);
	}
	
	/** Retire un listener selection sur le modele */
	public void removeChessListener(ChessModelListener l) {
		listenerList.remove(ChessModelListener.class, l);
	}
	
	protected void fireCaseChanged(int line, int column) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessModelListener) {
				((ChessModelListener) listeners[i]).caseChanged(line, column);
			}
		}
	}
	
	protected void fireCoupAdded(Coup coup) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessModelListener) {
				((ChessModelListener) listeners[i]).coupAdded(coup);
			}
		}
	}
	
	protected void fireCoupRemoved(Coup coup) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessModelListener) {
				((ChessModelListener) listeners[i]).coupRemoved(coup);
			}
		}
	}
	
	protected void allUpdate() {
		for (int line = 0; line < 8; line++) {
			for (int column = 0; column < 8; column++) {
				fireCaseChanged(line, column);
			}
		}
	}
	
	
	/** Initialise une nouvelle table de jeu a partir d'un format fen */
	public void newGame(String fen) {
		historicModel.clear();
	}
	
	/** Retourne le numero du joueur courant */
	public abstract Color getJoueur();
	
	/** Retourne le numero du joueur adverse */
	public abstract Color getJoueurAdverse();
	
	/** Retourne vrai si la case pointee n'est pas en dehors du plateau */
	public abstract boolean valid(int line, int column);
	
	/** Retourne l'objet de type Case pointee */
	public abstract Case getCase(int line, int column);
	
	/** Retourne la case sur laquelle est le roi du joueur passe en parametre*/
	public Case getCaseKing(Color joueur) {
		for (Piece p : getPieceFromPlayer(joueur)) {
			if (p instanceof Roi) {
				return getCase(p.getLine(), p.getColumn());
			}
		}
		return null;
	}
	
	/** Retourne l'objet Case qui peut etre prise en passant, null si rien */
	public abstract Case getCaseEp();
	
	/** Retourne vrai si la case pointee appartient au joueur courant */
	public boolean caseJoueur(Case c) {
		if (c.caseVide()) {
			return false;
		}
		Color color1 = getJoueur();
		Color color2 = c.getPiece().getColor();
		return color1.equals(color2);
	}
	
	/** Retourne vrai si la case pointee appartient au joueur adverse */
	public boolean caseAdverse(Case c) {
		if (c.caseVide()) {
			return false;
		}
		return !caseJoueur(c);
	}
	
	/** Retourne vrai si la case pointee est attaquee par le joueur passe en parametre */
	public boolean caseUnderAttackFromPlayer(Color joueur, Case c) {
		for (Piece p : getPieceFromPlayer(joueur)) {
			if (p.attackCase(c)) {
				return true;
			}
		}
		return false;
	}
	
	/** Retourne vrai si le roi du joueur passe en parametre est mis en echec */
	public boolean checkKing(Color joueur) {
		return caseUnderAttackFromPlayer(joueur.getInverse(), getCaseKing(joueur));
	}
	
	/** Retourne vrai si le roi du joueur courant est mis echec et mat */
	public boolean checkMateKing(Color joueur) {
		if (!checkKing(joueur)) {
			return false;
		}
		// Retourne vrai si aucun deplacement ne peut proteger le roi 
		return (getCaseJouableFromPlayer(joueur).size() == 0);
	}
	
	/** Retourne le nombre de coup joue */
	public abstract int getNbCoup();
	
	/** Retourne le nombre de demi coup joue */
	public abstract int getNbDemiCoup();
	
	/** Retourne vrai si le roque blanc cote roi est possible */
	public abstract boolean Kroque();
	
	/** Retourne vrai si le roque blanc cote reine est possible */
	public abstract boolean Qroque();
	
	/** Retourne vrai si le roque noir cote roi est possible */
	public abstract boolean kroque();
	
	/** Retourne vrai si le roque noir cote reine est possible */
	public abstract boolean qroque();
	
	/** Retourne vrai si la partie est finie */
	//public abstract boolean isEndGame();
	
	/** Retourne vrai si la position du plateau est valide */
	public boolean isPositionValid() {
		// Controle des rois blancs et noirs
		int nbCaseKingWhite = 0;
		int nbCaseKingBlack = 0;
		for (int line = 0; line < 8; line++) {
			for (int column = 0; column < 8; column++) {
				Case c = getCase(line, column);
				if (c.getPiece() != null) {
					if (c.getPiece().getId() == Piece.ROI_BLANC) {
						nbCaseKingWhite++;
					} else if (c.getPiece().getId() == Piece.ROI_NOIR) {
						nbCaseKingBlack++;
					}
				}
			}
		}
		// Retourne faux s'il n'y a pas un roi de chaque couleur
		if (nbCaseKingWhite != 1 || nbCaseKingBlack != 1) {
			return false;
		}
		// Retourne faux si le roi qui ne joue pas est en echec
		if (checkKing(getJoueurAdverse())) {
			return false;
		}
		// Aucune erreur presente
		return true;
	}
	
	/** Retourne vrai s'il y a match nul */
	public abstract boolean isGameTie();
	
	/** Retourne l'ensemble des pieces du joueur passe en parametre */
	public abstract ArrayList<Piece> getPieceFromPlayer(Color joueur);
	
	/** Retourne l'ensemble des cases jouables par le joueur passe en parametre */
	protected ArrayList<Case> getCaseJouableFromPlayer(Color joueur) {
		ArrayList<Case> alc = new ArrayList<Case>();
		for (Piece p : getPieceFromPlayer(joueur)) {
			alc.addAll(p.getCaseJouable());
		}
		return alc;
	}
	
	/** Retourne le coup associe au deplacement d'une case a une autre */
	public abstract Coup getCoupFromCasePlayed(Case caseDep, Case caseArr, boolean isHuman);
	
	/** Retourne vrai si le coup passe en parametre est jouable */
	public boolean isCoupValid(Coup coup) {
		boolean check;
		coup.jouerCoup();
		check = !checkKing(getJoueur());
		coup.getBack();
		return check;
	}
	
	
	
	/** Joue sur le plateau le coup passe en parametre et change de joueur */
	public abstract void jouerCoup(Coup coup);
	
	/** Annule le dernier coup joue et change de joueur */
	public abstract void getBack();
	
	/** Place le plateau sur le coup numero indexCoup */
	public void goTo(int indexCoup) {
		Coup coup = null;
		// Retour arriere jusqu'au indexCoup
		while (historicModel.getIndex() > indexCoup) {
			getBack();
			historicModel.moveBackward();
		}
		// Avance jusqu'au indexCoup
		while (historicModel.getIndex() < indexCoup) {
			historicModel.moveForeward();
			coup = historicModel.getElementAt(historicModel.getIndex());
			jouerCoup(coup);
		}
		// Actualise tout le plateau
		allUpdate();
	}
	
	/** Change le joueur du plateau */
	protected abstract void switchJoueur();
	
	
	
	/** Met a jour la selection d'une case */
	public void setSelection(Case c, boolean select) {
		c.setSelection(select);
		fireCaseChanged(c.line, c.column);
	}
	
	/** Joue et archive le coup dans l'historique */
	public void addCoup(Coup coup) {
		jouerCoup(coup);
		historicModel.add(coup);
		fireCoupAdded(coup);
	}
	
	/** Retire le dernier coup archive */
	public Coup removeCoup() {
		Coup coup = historicModel.remove();
		getBack();
		fireCoupRemoved(coup);
		return coup;
	}
	
	
	/** Evalue la valeur de la position du plateau */
	public abstract int evaluation();
	
	/** Retourne le format FEN du plateau de jeu */
	public static String toFenFormat(ChessModel model) {
		String s = "";
		int column, count = 0;
		
		// Zone 1
		for (int line = 0; line < 8; line++) {
			column = 0;
			while (column < 8) {
				count = 0;
				while (column < 8 && model.getCase(line, column).getPiece() == null) {
					count++;
					column++;
				}
				
				if (count > 0) {
					s += String.valueOf(count);
				} else {
					s += model.getCase(line, column).getPiece().getString();
					column++;
				}
			}
			if (line < 7) {
				s += "/";
			}
		}
		
		// Zone 2
		if (model.getJoueur().isWhite()) {
			s += " w ";
		} else {
			s += " b ";
		}
		
		// Zone 3
		if (!model.Kroque() && !model.Qroque() && !model.kroque() && !model.qroque()) {
			s += "-";
		} else {
			if (model.Kroque()) {
				s += "K"; 
			}
			if (model.Qroque()) {
				s += "Q";
			}
			if (model.kroque()) {
				s += "k";
			}
			if (model.qroque()) {
				s += "q";
			}
		}
		
		// Zone 4
		if (model.getCaseEp() == null) {
			s += " -";
		} else {
			s += " " + model.getCaseEp().toString();
		}
		
		// Zone 5
		s += " " + String.valueOf(model.getNbDemiCoup());
		
		// Zone 6
		s += " " + String.valueOf(model.getNbCoup());
		
		return s;
	}
}
