package piece;

import java.util.ArrayList;

import coup.Coup;
import coup.CoupEp;
import coup.CoupPromotion;
import data.Case;
import data.ChessModel;
import data.Color;

public class Pion extends Piece {
	
	/** Constructeur */
	public Pion(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.PION_NOIR;
		}
		return Piece.PION_BLANC;
	}
	
	@Override
	public int getValue() {
		return 100;
	}
	
	@Override
	public String getString() {
		if (getColor().isWhite()) {
			return "P";
		} else {
			return "p";
		}
	}
	
	@Override
	public ArrayList<Case> getCaseJouable() {
		ArrayList<Case> caseJouable = new ArrayList<Case>();
		ArrayList<Case> caseAttack = new ArrayList<Case>();
		int joueur = (getColor().isWhite() ? 1 : -1);
		Case tmp;
		Coup coup;
		Piece piece;
		
		// Avance normale
		if (p.valid(getLine()-joueur, getColumn())) {
			tmp = p.getCase(getLine()-joueur, getColumn());
			if (tmp.caseVide()) {
				caseJouable.add(tmp);
				tmp = p.getCase(getLine()-(2*joueur), getColumn());
				if (getColor().isWhite() && getLine() == 6 && tmp.caseVide()) {
					caseJouable.add(tmp);
				}
				if (color.isBlack() && getLine() == 1 && tmp.caseVide()) {
					caseJouable.add(tmp);
				}
			}
		}
		
		// Prise normale et prise en passant
		for (int i : new int[] {-1, 1}) {
			if (p.valid(getLine()-joueur, getColumn()-i)) {
				tmp = p.getCase(getLine()-joueur, getColumn()-i);
				piece = tmp.getPiece();
				if (piece != null && !sameColor(piece)) {
					caseJouable.add(tmp);
				}
				if (tmp.equals(p.getCaseEp())) {
					caseJouable.add(tmp);
				}
			}
		}
		
		for (Case caseArr : caseJouable) {
			if (caseArr.line == 0 || caseArr.line == 7) {
				coup = new CoupPromotion(p, casePiece, caseArr, 2);
			} else if (caseArr.equals(p.getCaseEp())) {
				coup = new CoupEp(casePiece, caseArr, p.getCase(getLine(), caseArr.column));
			} else {
				coup = new Coup(casePiece, caseArr);
			}
			if (p.isCoupValid(coup)) {
				caseAttack.add(caseArr);
			}
		}
		
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		Case caseAttack;
		int joueur = (getColor().isWhite() ? 1 : -1);
		for (int i : new int[] {-1, 1}) {
			if (p.valid(getLine()-joueur, getColumn()-i)) {
				caseAttack = p.getCase(getLine()-joueur, getColumn()-i);
				if (caseAttack.equals(c)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
