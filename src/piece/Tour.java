package piece;

import java.util.ArrayList;

import coup.Coup;
import data.Case;
import data.ChessModel;
import data.Color;

public class Tour extends Piece {
	
	/** Constructeur */
	public Tour(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.TOUR_NOIRE;
		}
		return Piece.TOUR_BLANCHE;
	}
	
	@Override
	public int getValue() {
		return 500;
	}
	
	@Override
	public String getString() {
		if (getColor().isWhite()) {
			return "R";
		} else {
			return "r";
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
			i = 0;
			j = 0;
			do {
				loop = false;
				j += sign_x;
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseArr = p.getCase(getLine()+i, getColumn()+j);
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
		
		for (int sign_y : new int[] {-1, 1}) {
			i = 0;
			j = 0;
			do {
				loop = false;
				i += sign_y;
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseArr = p.getCase(getLine()+i, getColumn()+j);
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
		
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		int dx = c.line - getLine();
		int dy = c.column - getColumn();
		
		// Si la tour est sur la meme ligne ou colonne que la case
		if (dx == 0 || dy == 0) {
			int sign_x = (dx > 0 ? 1 : -1);
			int sign_y = 0;
			if (dx == 0) {
				sign_x = 0;
				sign_y = (dy > 0 ? 1 : -1);
			}
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
