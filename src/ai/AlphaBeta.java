package ai;

import java.util.ArrayList;

import coup.Coup;
import piece.Piece;
import data.Case;
import data.ChessModel;

public class AlphaBeta extends Thread {
	
	private ChessModel model;
	private int level;
	public Coup coup;
	
	/** Constructeur */
	public AlphaBeta(ChessModel model, int level) {
		this.model = model;
		this.level = level;
	}
	
	public Coup getCoup() {
		return coup;
	}
	
	private ArrayList<Coup> getCoupFromPlayer(ChessModel model) {
		ArrayList<Coup> listCoup = new ArrayList<Coup>();
		Case caseDep;
		for (Piece piece : model.getPieceFromPlayer(model.getJoueur())) {
			caseDep = model.getCase(piece.getLine(), piece.getColumn());
			for (Case caseArr : piece.getCaseJouable()) {
				listCoup.add(model.getCoupFromCasePlayed(caseDep, caseArr, false));
			}
		}
		return listCoup;
	}
	
	/** Meilleur coup des noirs */
	private void jouerAlphaBeta() {
		int value, value_min = Integer.MAX_VALUE;
		ArrayList<Coup> listCoup = getCoupFromPlayer(model);
		for (Coup coup : listCoup) {
			model.jouerCoup(coup);
			
			value = ab_max_min(model, Integer.MIN_VALUE, Integer.MAX_VALUE, level-1);
			
			model.getBack();
			
			if (value <= value_min) {
				value_min = value;
				this.coup = coup;
			}
		}
	}
	
	/** Meilleur coup des blancs */
	private void jouerBetaAlpha() {
		int value, value_max = Integer.MIN_VALUE;
		ArrayList<Coup> listCoup = getCoupFromPlayer(model);
		for (Coup coup : listCoup) {
			model.jouerCoup(coup);
			
			value = ab_min_max(model, Integer.MIN_VALUE, Integer.MAX_VALUE, level-1);
			
			model.getBack();
			
			if (value >= value_max) {
				value_max = value;
				this.coup = coup;
			}
		}
	}
	
	
	/** Algorithme Alpha Beta MinMax */
	private int ab_min_max(ChessModel model, int alpha, int beta, int depth) {
		
		// p_courant.joueur == -1
		if (model.checkMateKing(model.getJoueur())) {
			return Integer.MAX_VALUE;
		}
		if (depth == 0) {
			return model.evaluation();
		}
		
		// child servira comme support pour tester tout les coups possibles
		ChessModel child = model;
		// Creation de la liste des coups possibles
		ArrayList<Coup> listCoup = getCoupFromPlayer(child);
		int value;
		
		// Boucle de parcourt de tout les enfants
		for (Coup coup : listCoup) {
			child.jouerCoup(coup);
			
			value = ab_max_min(child, alpha, beta, depth-1);
			
			child.getBack();
			
			if (value < beta) {
				beta = value;
				if (alpha > beta) {
					return beta;
				}
			}
		}
		
		return beta;
	}
	
 	/** Algorithme Alpha Beta MaxMin */
	private int ab_max_min(ChessModel model, int alpha, int beta, int depth) {
		
		// p_courant.joueur == 1
		if (model.checkMateKing(model.getJoueur())) {
			return Integer.MIN_VALUE;
		}
		if (depth == 0) {
			return model.evaluation();
		}
		
		// child servira comme support pour tester tout les coups possibles
		ChessModel child = model;
		// Creation de la liste des coups possibles
		ArrayList<Coup> listCoup = getCoupFromPlayer(child);
		int value;
		
		// Boucle de parcourt de tout les enfants
		for (Coup coup : listCoup) {
			child.jouerCoup(coup);
			
			value = ab_min_max(child, alpha, beta, depth-1);
			
			child.getBack();
			
			if (value > alpha) {
				alpha = value;
				if (alpha > beta) {
					return alpha;
				}
			}
		}
		
		return alpha;
	}
	
	/** Lance le calcul du meilleur coup sur ChessModel */
	public void run() {
		coup = null;
		if (model.getJoueur().isWhite()) {
			jouerBetaAlpha();
		} else {
			jouerAlphaBeta();
		}
	}
}
