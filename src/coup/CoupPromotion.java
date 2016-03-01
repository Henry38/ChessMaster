package coup;

import data.Case;
import data.Color;
import data.ChessModel;
import piece.Cavalier;
import piece.Fou;
import piece.Piece;
import piece.Reine;
import piece.Tour;

public class CoupPromotion extends Coup {
	
	private Piece replacePiece, promotePiece;
	
	/** Constructeur */
	public CoupPromotion(ChessModel p, Case caseDep, Case caseArr, int promoteId) {
		super(caseDep, caseArr);
		this.replacePiece = caseDep.getPiece();
		Color color;
		if (caseArr.line == 0) {
			color = Color.White;
		} else {
			color = Color.Black;
		}
		switch (promoteId) {
		case 2:
			promotePiece = new Reine(p, color);
			break;
		case 5:
			promotePiece = new Tour(p, color);
			break;
		case 3:
			promotePiece = new Fou(p, color);
			break;
		case 4:
			promotePiece = new Cavalier(p, color);
			break;
			
		default:
			promotePiece = null;
			break;
		}
	}
	
	/** Retourne la piece remplace */
	public Piece getReplacePiece() {
		return replacePiece;
	}
	
	/** Retourne la piece promue */
	public Piece getPromotePiece() {
		return promotePiece;
	}
	
	public void jouerCoup() {
		super.jouerCoup();
		getReplacePiece().setCase(null);
		caseArr.setPiece(getPromotePiece());
		getPromotePiece().setCase(caseArr);
	}
	
	public void getBack() {
		super.getBack();
		caseDep.setPiece(getReplacePiece());
		getReplacePiece().setCase(caseDep);
		getPromotePiece().setCase(null);
	}
}
