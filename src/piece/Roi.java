package piece;

import java.util.ArrayList;

import coup.Coup;
import data.Case;
import data.ChessModel;
import data.Color;

public class Roi extends Piece {
	
	/** Constructeur */
	public Roi(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.ROI_NOIR;
		}
		return Piece.ROI_BLANC;
	}
	
	@Override
	public int getValue() {
		return 0;
	}
	
	@Override
	public String getString() {
		if (color.isWhite()) {
			return "K";
		} else {
			return "k";
		}
	}
	
	@Override
	public ArrayList<Case> getCaseJouable() {
		ArrayList<Case> caseAttack = new ArrayList<Case>();
		Color joueur = getColor();
		Case caseArr;
		Coup coup;
		Piece piece;
		
		// Parcours des 8 cases adjacentes
		for (int i : new int[] {-1, 0, 1}) {
			for (int j : new int[] {-1, 0, 1}) {
				if ((i != 0 || j != 0) && p.valid(getLine()+i, getColumn()+j)) {
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
		
		// Test le roque des rois
		if (joueur.isWhite()) {
			Color joueurAdverse = joueur.getInverse();
			if (p.Kroque()) {
				if (!p.checkKing(joueur) && p.getCase(7, 5).caseVide() && p.getCase(7, 6).caseVide() &&
						!p.caseUnderAttackFromPlayer(joueurAdverse, p.getCase(7, 5)) &&
						!p.caseUnderAttackFromPlayer(joueurAdverse, p.getCase(7, 6))) {
					caseAttack.add(p.getCase(7, 6));
				}
			}
			if (p.Qroque()) {
				if (!p.checkKing(joueur) && p.getCase(7, 3).caseVide() && p.getCase(7, 2).caseVide() && p.getCase(7, 1).caseVide() &&
						!p.caseUnderAttackFromPlayer(joueurAdverse, p.getCase(7, 3)) &&
						!p.caseUnderAttackFromPlayer(joueurAdverse, p.getCase(7, 2))) {
					caseAttack.add(p.getCase(7, 2));
				}
			}
		} else {
			if (p.kroque()) {
				if (!p.checkKing(joueur) && p.getCase(0, 5).caseVide() && p.getCase(0, 6).caseVide() &&
						!p.caseUnderAttackFromPlayer(joueur, p.getCase(0, 5)) &&
						!p.caseUnderAttackFromPlayer(joueur, p.getCase(0, 6))) {
					caseAttack.add(p.getCase(0, 6));
				}
			}
			if (p.qroque()) {
				if (!p.checkKing(p.getJoueur()) && p.getCase(0, 3).caseVide() && p.getCase(0, 2).caseVide() && p.getCase(0, 1).caseVide() &&
						!p.caseUnderAttackFromPlayer(joueur, p.getCase(0, 3)) &&
						!p.caseUnderAttackFromPlayer(joueur, p.getCase(0, 2))) { 
					caseAttack.add(p.getCase(0, 2));
				}
			}
		}
		
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		Case caseAttack;
		
		for (int i : new int[] {-1, 0, 1}) {
			for (int j : new int[] {-1, 0, 1}) {
				if ((i != 0 || j != 0) && p.valid(getLine()+i, getColumn()+j)) {
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
