package piece;

import java.util.ArrayList;

import coup.Coup;
import data.Case;
import data.ChessModel;
import data.Color;

public class Fou extends Piece {
	
	/** Constructeur */
	public Fou(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.FOU_NOIR;
		}
		return Piece.FOU_BLANC;
	}
	
	@Override
	public int getValue() {
		return 300;
	}
	
	@Override
	public String getString() {
		if (getColor().isWhite()) {
			return "B";
		} else {
			return "b";
		}
	}
	
	@Override
	public ArrayList<Case> getCaseJouable() {
		ArrayList<Case> caseAttack = new ArrayList<Case>();
		Case caseArr;
		Coup coup;
		Piece piece;
		boolean loop;
		int i, j;
		
		for (int sign_x : new int[] {-1, 1}) {
			for (int sign_y : new int[] {-1, 1}) {
				i = getLine();
				j = getColumn();
				do {
					loop = false;
					i += sign_x;
					j += sign_y;
					if (p.valid(i, j)) {
						caseArr = p.getCase(i, j);
						piece = caseArr.getPiece();
						if (piece == null || !sameColor(piece)) {
							loop = (piece == null);
							coup = new Coup(casePiece, caseArr);
							if (p.isCoupValid(coup)) {
								caseAttack.add(caseArr);
							}
						}
					}
				} while (loop);
			}
		}
		
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		int dx = c.line - getLine();
		int dy = c.column - getColumn();
		
		// Si le fou est dans une meme diagonale
		if (Math.abs(dx) == Math.abs(dy)) {
			int sign_x = (dx > 0 ? 1 : -1);
			int sign_y = (dy > 0 ? 1 : -1);
			int i = 0;
			int j = 0;
			boolean loop;
			Case caseAttack;
			do {
				loop = false;
				i += sign_x;
				j += sign_y;
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseAttack = p.getCase(getLine()+i, getColumn()+j);
					if (caseAttack.equals(c)) {
						return true;
					} else if (caseAttack.caseVide()) {
						loop = true;
					}
				}
			} while (loop);
		}
		
		return false;
	}
}
