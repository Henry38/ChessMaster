package controler;

import java.util.EventListener;

import coup.Coup;

public interface ChessModelListener extends EventListener {
	public void caseChanged(int line, int column);
	public void coupAdded(Coup coup);
	public void coupRemoved(Coup coup);
}
