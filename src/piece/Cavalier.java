package piece;

import java.util.ArrayList;

import coup.Coup;
import data.Case;
import data.ChessModel;
import data.Color;

public class Cavalier extends Piece {
	
	/** Constructeur */
	public Cavalier(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.CAVALIER_NOIR;
		}
		return Piece.CAVALIER_BLANC;
	}
	
	@Override
	public int getValue() {
		return 300;
	}
	
	@Override
	public String getString() {
		if (color.isWhite()) {
			return "N";
		} else {
			return "n";
		}
	}
	
	@Override
	public ArrayList<Case> getCaseJouable() {
		ArrayList<Case> caseAttack = new ArrayList<Case>();
		Case caseArr;
		Coup coup;
		Piece piece;
		
		for (int i : new int[] {-2, 2}) {
			for (int j : new int[] {-1, 1}) {
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseArr = p.getCase(getLine()+i, getColumn()+j);
					piece = caseArr.getPiece();
					if (piece == null || !sameColor(piece)) {
						coup = new Coup(casePiece, caseArr);
						if (p.isCoupValid(coup)) {
							caseAttack.add(caseArr);
						}
					}
				}
			}
		}
		
		for (int j : new int[] {-2, 2}) {
			for (int i : new int[] {-1, 1}) {
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseArr = p.getCase(getLine()+i, getColumn()+j);
					piece = caseArr.getPiece();
					if (caseArr != null && (piece == null || !sameColor(piece))) {
						coup = new Coup(casePiece, caseArr);
						if (p.isCoupValid(coup)) {
							caseAttack.add(caseArr);
						}
					}
				}
			}
		}
		
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		Case caseAttack;
		
		for (int i : new int[] {-2, 2}) {
			for (int j : new int[] {-1, 1}) {
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseAttack = p.getCase(getLine()+i, getColumn()+j);
					if (caseAttack.equals(c)) {
						return true;
					}
				}
			}
		}
		
		for (int j : new int[] {-2, 2}) {
			for (int i : new int[] {-1, 1}) {
				if (p.valid(getLine()+i, getColumn()+j)) {
					caseAttack = p.getCase(getLine()+i, getColumn()+j);
					if (caseAttack.equals(c)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
