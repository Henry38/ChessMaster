package data;

import piece.Piece;


public class Case {
	
	public int line, column;
	private Piece piece;
	private boolean selected;
	
	/** Constructeur */
	public Case(int line, int column, Piece piece) {
		this.line = line;
		this.column = column;
		this.piece = piece;
		this.selected = false;
	}
	
	/** Retourne la piece presente sur la case, null si la case est vide */
	public Piece getPiece() {
		return piece;
	}
	
	/** Retourne vrai s'il n'y a pas de piece sur la case */
	public boolean caseVide() {
		return (getPiece() == null);
	}
	
	/** Retourne vrai si la case est selectionnee */
	public boolean isSelected() {
		return selected;
	}
	
	/** Retourne vrai si la case contient une piece identifiee par id */
	public boolean hasPiece(int id) {
		return (!caseVide() && getPiece().getId() == id); 
	}
	
	/** Met a jour la piece presente sur la case */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	/** Met a jour la selection de la case */
	public void setSelection(boolean selected) {
		this.selected = selected;
	}
	
	/** Bascule la selection de la case */
	public void toggleSelect() {
		this.selected = !selected;
	}
	
	/** Retourne vrai si les deux cases sont les memes lignes et colonnes */
	public boolean equals(Case c) {
		if (c == null) {
			return false;
		}
		return (this.line == c.line && this.column == c.column);
	}
	
	/** Retourne le nom associe a une case */
	public String toIconString() {
		String name = "";
		
		int pieceId = (piece != null ? piece.getId() : 0);
		name += (pieceId > 0 ? "b" : (pieceId < 0 ? "n" : ""));
		name += (Math.abs(pieceId) > 0 ? Integer.toString(Math.abs(pieceId)) : "");
		
		if (selected) {
			name += "s";
		} else {
			if ((line + column) % 2 == 0) {
				name += "b";
			} else {
				name += "n";
			}
			
		}
		
		return name;
	}
	
	/** Representation textuelle d'une case */
	public String toString() {
		return String.valueOf(Character.toChars(97+column)) + String.valueOf(8-line);
	}
}
