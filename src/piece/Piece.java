package piece;

import java.util.ArrayList;

import data.Case;
import data.ChessModel;
import data.Color;

public abstract class Piece {
	
	public static int ROI_BLANC = 1;
	public static int REINE_BLANCHE = 2;
	public static int FOU_BLANC = 3;
	public static int CAVALIER_BLANC = 4;
	public static int TOUR_BLANCHE = 5;
	public static int PION_BLANC = 6;
	public static int ROI_NOIR = -1;
	public static int REINE_NOIRE = -2;
	public static int FOU_NOIR = -3;
	public static int CAVALIER_NOIR = -4;
	public static int TOUR_NOIRE = -5;
	public static int PION_NOIR = -6;
	
//	public static int ROI = 1;
//	public static int REINE = 2;
//	public static int FOU = 3;
//	public static int CAVALIER = 4;
//	public static int TOUR = 5;
//	public static int PION = 6;
	
	
	protected ChessModel p;
	protected Case casePiece;
	protected Color color;
	
	/** Constructeur */
	public Piece(ChessModel p, Color color) {
		this.p = p;
		this.casePiece = null;
		this.color = color;
	}
	
	/** Retourne la ligne sur laquelle est situee la piece */
	public int getLine() {
		if (casePiece == null) {
			return -1;
		}
		return casePiece.line;
	}
	
	/** Retourne la colonne sur laquelle est situee la piece */
	public int getColumn() {
		if (casePiece == null) {
			return -1;
		}
		return casePiece.column;
	}
	
	/** Retourne la couleur de la piece */
	public final Color getColor() {
		return color;
	}
	
	/** Retourne vrai si les deux pieces sont de la meme couleur */
	public boolean sameColor(Piece piece) {
		return (getColor().equals(piece.getColor()));
	}
	
	/** Met a jour la case sur laquelle se trouve la piece */
	public void setCase(Case c) {
		this.casePiece = c;
	}
	
	/** Retourne l'identifiant de la piece */
	public abstract int getId();
	
	/** Retourne la valeur de la piece */
	public abstract int getValue();
	
	/** Retourne la chaine de caractere associe a la piece */
	public abstract String getString();
	
	/** Retourne l'ensemble de cases jouables par la piece */
	public abstract ArrayList<Case> getCaseJouable();
	
	/** Retourne vrai si la piece attaque la case c */
	public abstract boolean attackCase(Case c);
	
}
