package data;

public enum GameType {
	
	joueur_contre_joueur(0),
	joueur_contre_ordinateur(1),
	ordinateur_contre_joueur(2),
	ordinateur_contre_ordinateur(3);
	
	private int type;
	
	GameType(int type) {
		this.type = type;
	}
	
	public boolean isCpuTurn(ChessModel model) {
		if (type == 0) {
			return false;
		} else if (type == 1) {
			return (model.getJoueur().isBlack());
		} else if (type == 2) {
			return (model.getJoueur().isWhite());
		} else {
			return true;
		}
	}
}
