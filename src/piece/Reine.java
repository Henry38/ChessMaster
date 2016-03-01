package piece;

import java.util.ArrayList;

import data.Case;
import data.ChessModel;
import data.Color;

public class Reine extends Piece {
	
	/** Constructeur */
	public Reine(ChessModel p, Color color) {
		super(p, color);
	}
	
	@Override
	public int getId() {
		if (getColor().isBlack()) {
			return Piece.REINE_NOIRE;
		}
		return Piece.REINE_BLANCHE;
	}
	
	@Override
	public int getValue() {
		return 1000;
	}
	
	@Override
	public String getString() {
		if (color.isWhite()) {
			return "Q";
		} else {
			return "q";
		}
	}
	
	@Override
	public ArrayList<Case> getCaseJouable() {
		ArrayList<Case> caseAttack = new ArrayList<Case>();
		Fou fou = new Fou(p, getColor());
		fou.setCase(casePiece);
		Tour tour = new Tour(p, getColor());
		tour.setCase(casePiece);
		caseAttack.addAll(fou.getCaseJouable());
		caseAttack.addAll(tour.getCaseJouable());
		return caseAttack;
	}
	
	@Override
	public boolean attackCase(Case c) {
		Fou fou = new Fou(p, getColor());
		fou.setCase(casePiece);
		Tour tour = new Tour(p, getColor());
		tour.setCase(casePiece);
		return (tour.attackCase(c) || fou.attackCase(c));
	}
}
