package controler;

import data.GameType;

public interface MenuListener {
	public void newGame(GameType gameType);
	public void exit();
}
