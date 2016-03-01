package controler;

import java.util.EventListener;

public interface ChessListener extends EventListener {
	public void caseClicked(int line, int column);
}
