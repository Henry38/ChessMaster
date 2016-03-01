package coup;

import data.Case;
import data.Color;
import piece.Piece;

public class Coup {
	
	public Case caseDep, caseArr;
	protected Piece prise;
	protected Color color;
	protected String notation;
	
	/** Constructeur */
	public Coup(Case caseDep, Case caseArr) {
		this.caseDep = caseDep;
		this.caseArr = caseArr;
		this.prise = caseArr.getPiece();
		this.color = null;
		this.notation = "";
	}
	
	/** Retourne la case de depart */
	public final Case getCaseDepart() {
		return caseDep;
	}
	
	/** Retourne la case de d'arrivee */
	public final Case getCaseArrivee() {
		return caseArr;
	}
	
	/** Retourne le tableau des cases intervenant dans le coup */
	public Case[] getCases() {
		return new Case[] {getCaseDepart(), getCaseArrivee()};
	}
	
	/** Retourne la piece prise, null si la case d'arrivee est vide */
	public Piece getPrise() {
		return prise;
	}
	
	/** Retourne la couleur du joueur qui avait le trait lors du coup */
	public Color getColor() {
		return color;
	}
	
	/** Retourne la notation su coup sur l'echiquier */
	public String getNotation() {
		return notation;
	}
	
	/** Deplace la piece sur la case d'arrivee */
	public void jouerCoup() {
		if (getPrise() != null) {
			getPrise().setCase(null);
		}
		caseArr.setPiece(caseDep.getPiece());
		caseDep.setPiece(null);
		caseArr.getPiece().setCase(caseArr);
	}
	
	/** Replace la piece sur la case de depart */
	public void getBack() {
		if (getPrise() != null) {
			getPrise().setCase(caseArr);
		}
		caseDep.setPiece(caseArr.getPiece());
		caseArr.setPiece(getPrise());
		caseDep.getPiece().setCase(caseDep);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	/** Definit la notation du coup */
	public void setNotation(String notation) {
		this.notation = notation;
	}
	
	/** Representation textuelle d'un coup */
	public String toString() {
		return caseDep.toString() + " -> " + caseArr.toString();
	}
}
