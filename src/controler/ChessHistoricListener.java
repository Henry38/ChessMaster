package controler;

import java.util.EventListener;

public interface ChessHistoricListener extends EventListener {
	public void removedClicked();
	public void forkClicked();
	public void historicClicked(int index);
}
