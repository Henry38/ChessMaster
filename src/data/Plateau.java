package data;

import java.util.ArrayList;
import java.util.LinkedList;

import piece.Cavalier;
import piece.Fou;
import piece.Piece;
import piece.Pion;
import piece.Reine;
import piece.Roi;
import piece.Tour;
import view.PromoteDialog;
import coup.Coup;
import coup.CoupEp;
import coup.CoupPromotion;
import coup.CoupRock;

public class Plateau extends ChessModel {
	
	private Case[][] table;
	private ArrayList<Piece> pieceWhite;
	private ArrayList<Piece> pieceBlack;
	private LinkedList<Cell> historic;
	
	private Color joueur;
	
	private int nbCoup;
	private int nbDemiCoup;
	private int maskRoque;
	private int ep;
	
	private String initialState;
	
	/** Constructeur */
	public Plateau() {
		super();
		
		this.table = new Case[8][];
		for (int line = 0; line < 8; line++) {
			this.table[line] = new Case[8];
			for (int column = 0; column < 8; column++) {
				this.table[line][column] = new Case(line, column, null);
			}
		}
		
		this.pieceWhite = new ArrayList<Piece>(0);
		this.pieceBlack = new ArrayList<Piece>(0);
		this.historic = new LinkedList<Cell>();
		
		this.joueur = null;
	}
	
	@Override
	public ChessModel clone() {
		ChessModel model = new Plateau();
		model.newGame(initialState);
		
		Case caseDep, caseArr;
		int index = 0;
		while (index <= historicModel.getIndex()) {
			Coup coup = historicModel.getElementAt(index);
			caseDep = model.getCase(coup.caseDep.line, coup.caseDep.column);
			caseArr = model.getCase(coup.caseArr.line, coup.caseArr.column);
			coup = model.getCoupFromCasePlayed(caseDep, caseArr, false);
			model.addCoup(coup);
			index++;
		}
		
		return model;
	}
	
	public void newGame(String fen) {
		super.newGame(fen);
		// TODO : verifier que le format fen est valide
		
		String[] section = fen.split(" ");
		pieceWhite.clear();
		pieceBlack.clear();
		historic.clear();
		
		initialState = fen;
		
		int line = 0;
		int column = 0;
		Case c = null;
		Piece piece = null;
		
		// Zone 1
		for (String sl : section[0].split("/")) {
			column = 0;
			for (String sc : sl.split("")) {
				char ch = sc.charAt(0);
				if (ch < '0' || ch > '9') {
					piece = null;
					if (ch == 'K') {
						piece = new Roi(this, Color.White);
					} else if (ch == 'Q') {
						piece = new Reine(this, Color.White);
					} else if (ch == 'B') {
						piece = new Fou(this, Color.White);
					} else if (ch == 'N') {
						piece = new Cavalier(this, Color.White);
					} else if (ch == 'R') {
						piece = new Tour(this, Color.White);
					} else if (ch == 'P') {
						piece = new Pion(this, Color.White);
					} else if (ch == 'k') {
						piece = new Roi(this, Color.Black);
					} else if (ch == 'q') {
						piece = new Reine(this, Color.Black);
					} else if (ch == 'b') {
						piece = new Fou(this, Color.Black);
					} else if (ch == 'n') {
						piece = new Cavalier(this, Color.Black);
					} else if (ch == 'r') {
						piece = new Tour(this, Color.Black);
					} else if (ch == 'p') {
						piece = new Pion(this, Color.Black);
					}
					
					c = getCase(line, column);
					c.setPiece(piece);
					piece.setCase(c);
					if (piece.getColor().isWhite()) {
						pieceWhite.add(piece);
					} else if (piece.getColor().isBlack()) {
						pieceBlack.add(piece);
					}
					
					column++;
				} else {
					for (int j = 0; j < Character.getNumericValue(ch); j++) {
						getCase(line, column).setPiece(null);
						column++;
					}
				}
			}
			line++;
		}
		
		// Zone 2
		if (section[1].charAt(0) == 'w') {
			joueur = Color.White;
		} else {
			joueur = Color.Black;
		}
		
		// Zone 3
		int[] roque = new int[] {0, 0, 0, 0};
		if (!section[2].equals("-")) {
			for (String s : section[2].split("")) {
				char ch = s.charAt(0);
				if (ch == 'K') {
					roque[0] = 1;
				} else if (ch == 'Q') {
					roque[1] = 1;
				} else if (ch == 'k') {
					roque[2] = 1;
				} else if (ch == 'q') {
					roque[3] = 1;
				}
			}
		}
		maskRoque = ((roque[0] << 3) | (roque[1] << 2) | (roque[2] << 1) | roque[3]);
		
		// Zone 4
		ep = -1;
		if (section[3].charAt(0) != '-') {
			int epLine = Character.getNumericValue(section[3].charAt(1));
			int epColumn = Character.toLowerCase(section[3].charAt(0)) - 97;
			ep = (8 * epLine) + epColumn;
		}
		
		// Zone 5
		nbDemiCoup = Integer.valueOf(section[4]);
		
		// Zone 6
		nbCoup = Integer.valueOf(section[5]);
		
		allUpdate();
	}
	
	
	
	public Color getJoueur() {
		return joueur;
	}
	
	public Color getJoueurAdverse() {
		return joueur.getInverse();
	}
	
	public boolean valid(int line, int column) {
		return (line >= 0 && line <= 7 && column >= 0 && column <= 7);
	}
	
	public Case getCase(int line, int column) {
		if (!valid(line, column)) {
			return null;
		}
		return table[line][column];
	}
	
	public Case getCaseEp() {
		if (ep >= 0) {
			return getCase(ep / 8, ep % 8);
		}
		return null;
	}
	
	public int getNbCoup() {
		return nbCoup;
	}
	
	public int getNbDemiCoup() {
		return nbDemiCoup;
	}
	
	public boolean Kroque() {
		return ((maskRoque >> 3) & 1) == 1;
	}
	
	public boolean Qroque() {
		return ((maskRoque >> 2) & 1) == 1;
	}
	
	public boolean kroque() {
		return ((maskRoque >> 1) & 1) == 1;
	}
	
	public boolean qroque() {
		return (maskRoque & 1) == 1;
	}
	
	public boolean isGameTie() {
		// Aucune piece prise ni de pion bouge pendant 50 demi coups
		if (nbDemiCoup >= 50) {
			return true;
		}
		// Pat
		if (getCaseJouableFromPlayer(getJoueur()).size() == 0 && !checkKing(getJoueur())) {
			return true;
		}
		// Finale roi contre roi
		if (getPieceFromPlayer(getJoueur()).size() == 1 && getPieceFromPlayer(getJoueurAdverse()).size() == 1) {
			return true;
		}
		return false;
	}
	
	public final ArrayList<Piece> getPieceFromPlayer(Color joueur) {
		if (joueur.isWhite()) {
			return pieceWhite;
		} else {
			return pieceBlack;
		}
	}
	
	@Override
	public Coup getCoupFromCasePlayed(Case caseDep, Case caseArr, boolean isHuman) {
		Coup coup;
		if (caseDep.getPiece() instanceof Pion) {
			if (caseArr.line == 0 || caseArr.line == 7) {
				int promotePiece = 2;
				if (isHuman) {
					promotePiece = PromoteDialog.showMessageDialog(getJoueur());
				}
				coup = new CoupPromotion(this, caseDep, caseArr, promotePiece);
			} else if (getCaseEp() != null && getCaseEp().equals(caseArr)) {
				coup = new CoupEp(caseDep, caseArr, getCase(caseDep.line, caseArr.column));
			} else {
				coup = new Coup(caseDep, caseArr);
			}
		} else if (caseDep.getPiece() instanceof Roi) {
			if (caseArr.column - caseDep.column == 2) {
				int line = (caseDep.getPiece().getColor().isWhite() ? 7 : 0);
				coup = new CoupRock(caseDep, caseArr, getCase(line, 7), getCase(line, 5));
			} else if (caseArr.column - caseDep.column == -2) {
				int line = (caseDep.getPiece().getColor().isWhite() ? 7 : 0);
				coup = new CoupRock(caseDep, caseArr, getCase(line, 0), getCase(line, 3));
			} else {
				coup = new Coup(caseDep, caseArr);
			}
		} else {
			coup = new Coup(caseDep, caseArr);
		}
		return coup;
	}
	
	
	
	public void jouerCoup(Coup coup) {
		// Empile du coup dans l'historique local
		Color colorCoup = getJoueur();
		coup.setColor(colorCoup);
		coup.setNotation(getNotationCoup(coup));
		Cell cell = new Cell(coup, nbCoup, nbDemiCoup, maskRoque, ep);
		historic.add(cell);
		// Deplace les pieces sur le plateau
		coup.jouerCoup();
		// Mise a jour si promotion
		if (coup instanceof CoupPromotion) {
			CoupPromotion coupPro = (CoupPromotion) coup;
			if (colorCoup.isWhite()) {
				int index = pieceWhite.indexOf(coupPro.getReplacePiece());
				pieceWhite.set(index, coupPro.getPromotePiece());
			} else {
				int index = pieceBlack.indexOf(coupPro.getReplacePiece());
				pieceBlack.set(index, coupPro.getPromotePiece());
			}
		}
		// Mise a jour des pieces des joueur
		Piece p = coup.getPrise();
		if (p != null) {
			if (p.getColor().isWhite()) {
				pieceWhite.remove(p);
			} else {
				pieceBlack.remove(p);
			}
		}
		// Mise a jour du nombre de coup
		if (colorCoup.isBlack()) {
			nbCoup++;
		}
		// Mise a jour du nombre de demi coup
		if ((coup.caseArr.getPiece() instanceof Pion) || coup.getPrise() != null) {
			nbDemiCoup = 0;
		} else {
			nbDemiCoup = cell.nbDemiCoup+1;
		}
		// Mise a jour des roques
		if (coup.caseArr.getPiece() instanceof Roi) {
			if (colorCoup.isWhite()) {
				int mask = 3;	// mask = 0011
				maskRoque = maskRoque & mask;
			} else {
				int mask = 12;	// mask = 1100
				maskRoque = maskRoque & mask;
			}
		}
		if (Kroque() && !getCase(7, 7).hasPiece(Piece.TOUR_BLANCHE)) {
			int mask = 7;	// mask = 0111
			maskRoque = maskRoque & mask;
		}
		if (Qroque() && !getCase(7, 0).hasPiece(Piece.TOUR_BLANCHE)) {
			int mask = 11;	// mask = 1011
			maskRoque = maskRoque & mask;
		}
		if (kroque() && !getCase(0, 7).hasPiece(Piece.TOUR_NOIRE)) {
			int mask = 13;	// mask = 1101
			maskRoque = maskRoque & mask;
		}
		if (qroque() && !getCase(0, 0).hasPiece(Piece.TOUR_NOIRE)) {
			int mask = 14;	// mask = 1110
			maskRoque = maskRoque & mask;
		}
		// Mise a jour de la prise en passant
		if ((coup.caseArr.getPiece() instanceof Pion) && Math.abs(coup.caseArr.line - coup.caseDep.line) == 2) {
			Case c = getCase((coup.caseDep.line + coup.caseArr.line)/2, coup.caseDep.column);
			ep = (8 * c.line) + c.column;
		} else {
			ep = -1;
		}
		// Change de joueur
		switchJoueur();
	}
	
	public void getBack() {
		// Depile le coup dans l'historique local
		Cell cell = historic.pollLast();
		Coup coup = cell.coup;
		nbCoup = cell.nbCoup;
		nbDemiCoup = cell.nbDemiCoup;
		maskRoque = cell.maskRoque;
		ep = cell.ep;
		// Replace les pieces sur le plateau
		coup.getBack();
		// Mise a jour si promotion
		if (coup instanceof CoupPromotion) {
			CoupPromotion coupPro = (CoupPromotion) coup;
			Color colorCoup = coup.getColor();
			if (colorCoup.isWhite()) {
				int index = pieceWhite.indexOf(coupPro.getPromotePiece());
				pieceWhite.set(index, coupPro.getReplacePiece());
			} else {
				int index = pieceBlack.indexOf(coupPro.getPromotePiece());
				pieceBlack.set(index, coupPro.getReplacePiece());
			}
		}
		// Mise a jour des pieces du joueur
		Piece p = coup.getPrise();
		if (p != null) {
			if (p.getColor().isWhite()) {
				pieceWhite.add(p);
			} else {
				pieceBlack.add(p);
			}
		}
		// Chande de joueur
		switchJoueur();
	}
	
	protected void switchJoueur() {
		this.joueur = joueur.getInverse();
	}
	
	
	
	public int evaluation() {
		int pointsWhite = 0;
		int pointsBlack = 0;
		// Materiel blanc
		for (Piece p : getPieceFromPlayer(Color.White)) {
			pointsWhite += p.getValue();
		}
		// Materiel noir
		for (Piece p : getPieceFromPlayer(Color.Black)) {
			pointsBlack += p.getValue();
		}
		return (pointsWhite - pointsBlack);
	}
	
	/** Retourne la notation du coup sur l'echiquier */
	private String getNotationCoup(Coup coup) {
		String res = "";
		if (coup.getColor().isWhite()) {
			res += nbCoup + ". ";
		}
		switch (Math.abs(coup.caseDep.getPiece().getId())) {
		case 1:
			res += "R";
			break;
		case 2:
			res += "D";
			break;
		case 3:
			res += "F";
			break;
		case 4:
			res += "C";
			break;
		case 5:
			res += "T";
			break;
		default:
			break;
		}
		// Prise d'une piece adverse
		if (coup.getPrise() != null) {
			if (Math.abs(coup.caseDep.getPiece().getId()) == 6 || coup instanceof CoupPromotion) {
				res = String.valueOf(Character.toChars(97+coup.caseDep.column));
			}
			res += "x";
		}
		// Notation de la case d'arrivee
		res += coup.caseArr.toString();
		// Promotion
		if (coup instanceof CoupPromotion) {
			switch (Math.abs(coup.caseDep.getPiece().getId())) {
			case 2:
				res += "D";
				break;
			case 3:
				res += "F";
				break;
			case 4:
				res += "C";
				break;
			case 5:
				res += "T";
				break;
			default:
				break;
			}
		}
		// Prise en passant
		if (coup instanceof CoupEp) {
			res += " e.p.";
		}
		return res;
	}
	
	
	/** Private Class
	 * Retiens l'etat du plateau juste avant d'avoir joue un coup
	 * */
	private class Cell {
		
		private Coup coup;
		private int nbCoup, nbDemiCoup, maskRoque, ep;
		
		/** Constructeur */
		public Cell(Coup coup, int nbCoup, int nbDemiCoup, int maskRoque, int ep) {
			this.coup = coup;
			this.nbCoup = nbCoup;
			this.nbDemiCoup = nbDemiCoup;
			this.maskRoque = maskRoque;
			this.ep = ep;
		}
	}


}
