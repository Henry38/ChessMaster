package controler;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import artificial_intelligence.IA_AlphaBeta;
import sound.SoundBoard;
import view.JChess;
import view.JChessHistoric;
import view.JChessTree;
import coup.Coup;
import data.Case;
import data.ChessModel;
import data.ChessTreeModel;
import data.ChessTreeModel.Node;
import data.GameType;
import data.HistoricModel;
import data.Plateau;

public class Controler extends Thread implements ChessListener, ChessHistoricListener, TreeSelectionListener {
	
	private JChess chess;
	private JChessHistoric chessHistoric;
	private SoundBoard soundboard;
	
	private ChessModel model;
	private HistoricModel historicModel;
	private ChessTreeModel treeModel;
	private Node currentNode;
	
	private Case casePlayed;
	private boolean cpuPhase, endGame;
	private GameType gameType;
	private Coup coup;
	
	/** Constructeur */
	public Controler(JChess chess, JChessHistoric chessHistoric, JChessTree chessTree) {
		super();
		
		this.chess = chess;
		this.chessHistoric = chessHistoric;
		this.soundboard = new SoundBoard();
		
		this.model = null;
		this.historicModel = null;
		this.treeModel = chessTree.getTreeModel();
		this.currentNode = (Node) treeModel.getRoot();
		
		this.casePlayed = null;
		this.cpuPhase = false;
		this.endGame = true;
		this.gameType = null;
		this.coup = null;
		
		chess.addChessListener(this);
		chessHistoric.addChessHistoriqueListener(this);
		chessTree.addTreeSelectionListener(this);
		
		setDaemon(true);
		start();
	}
	
	/** Demarre une nouvelle partie */
	public synchronized void newGame(GameType gameType) {
		// Nouveau jeu
		newGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", gameType);
		//newGame("rnbq1bnr/pppkp1p1/6Q1/3p3p/3P1B2/5N2/PPP2PPP/RN2KB1R w KQkq - 0 1", gameType);
	}
	
	/** Demarre une nouvelle partie enregistre au format fen */
	public synchronized void newGame(String fen, GameType gameType) {
		// isEventdispatch = true
		if (casePlayed != null) {
			setSelectionCaseJouable(casePlayed, false);
			casePlayed = null;
		}
		this.gameType = gameType;
		soundboard.playNewgame();
		
		Node root = (Node) treeModel.getRoot();
		treeModel.clear(root);
		if (root.getModel() == null) {
			ChessModel model = new Plateau();
			treeModel.setModel(root, model);
		}
		root.getModel().newGame(fen);
		placeAtNode(root);
		
		notify();
	}
	
	/** Change le model du controler par celui du noeud node */
	private void placeAtNode(Node node) {
		currentNode = node;
		this.model = currentNode.getModel();
		this.historicModel = model.getHistoricModel();
		chess.setModel(model);
		chessHistoric.setModel(model);
		// Remise a jour des informations
		endGame = model.checkMateKing(model.getJoueur());
		cpuPhase = (gameType.isCpuTurn(model));
	}
	
	
	
	/** Retourne vrai si c'est au tour d'un joueur humain de jouer */
	private boolean isHumanPhase() {
		return (!endGame && !cpuPhase);
	}
	
	/** Met a jour les cases selectionnees */
	private void setSelectionCaseJouable(Case c, boolean select) {
		casePlayed = c;
		model.setSelection(c, select);
		// La condition devrait etre toujours valide
		if (c.getPiece() != null) {
			for (Case cc : c.getPiece().getCaseJouable()) {
				model.setSelection(cc, select);
			}
		}
	}
	
	/** Cherche le meilleur coup pour l'ordinateur */
	private synchronized void searchCoup() {
		// Calcul du meilleur coup
		IA_AlphaBeta t = new IA_AlphaBeta(model, 2);
		t.run();	// Ne lance pas de nouveau thread !
		coup = t.getCoup();
	}
	
	/** Joue le coup passe en parametre */
	private synchronized void jouerCoup(Coup coup) {
		if (coup.getPrise() == null) {
			soundboard.playBmove();
		} else {
			soundboard.playCapture();
		}
		// Mise a jour du plateau
		model.addCoup(coup);
	}
	
	/** Joue le coup passe en parametre avec animation de deplacement */
	private synchronized void jouerCoupCpu(Coup coup) {
		// Animation du coup
		model.setSelection(coup.caseDep, true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		jouerCoup(coup);
		model.setSelection(coup.caseDep, false);
		model.setSelection(coup.caseArr, true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		model.setSelection(coup.caseArr, false);
	}
	
	/** Verifie l'etat du plateau une fois le coup joue */
	private synchronized void resultCoup(Coup coup) {
		// Si le roi est echec et mate
		if (model.checkMateKing(model.getJoueur())) {
			soundboard.playSelect();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			soundboard.playMate();
			//MessageBox.ShowMessageError("Echec et Mat !", "Echec");
			endGame = true;
		}
		
		// Si la partie est declaree nulle
		if (!endGame && model.isGameTie()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			soundboard.playDraw();
			endGame = true;
		}
		
		// Si le roi est en echec
		if (!endGame && model.checkKing(model.getJoueur())) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			soundboard.playEchec();
		}
		
		// Mise a jour de la variable cpuPhase
		cpuPhase = (gameType.isCpuTurn(model));
	}
	
	
	
	///
	///	Listener sur le JChess
	///
	@Override
	public void caseClicked(int line, int column) {
		// isEventDispatchThread = true
		if (isHumanPhase() && historicModel.isIndexOnLast()) {
			Case c = model.getCase(line, column);
			// Si la case cliquee est la meme que precedemment
			if (c == casePlayed) {
				setSelectionCaseJouable(casePlayed, false);
				casePlayed = null;
			// Sinon si la case cliquee appartient au joueur
			} else if (model.caseJoueur(c)) {
				// Si des cases sont selectionnees
				if (casePlayed != null) {
					setSelectionCaseJouable(casePlayed, false);
					casePlayed = null;
				}
				setSelectionCaseJouable(c, true);
				casePlayed = c;
			// Sinon si une case a ete selectionnee precedemment
			} else if (casePlayed != null) {
				for (Case select : casePlayed.getPiece().getCaseJouable()) {
					if (select.equals(c)) {
						coup = model.getCoupFromCasePlayed(casePlayed, select, true);
						break;
					}
				}
				setSelectionCaseJouable(casePlayed, false);
				casePlayed = null;
				// On joue le coup sur le plateau si besoin
				if (coup != null) {
					synchronized (this) {
						notify();
					}
				}
			}
		}
	}
	
	///
	///	Listener sur JChessHistoric
	///
	@Override
	public void removedClicked() {
		// isEventDispatch = true;
		if ((isHumanPhase() || endGame) && historicModel.isIndexOnLast() && historicModel.getSize() > 0) {
			// Si des cases sont selectionnees
			if (casePlayed != null) {
				setSelectionCaseJouable(casePlayed, false);
				casePlayed = null;
			}
			// On retire le dernier coup archive sur le plateau
			Coup coup = model.removeCoup();
			for (Case c : coup.getCases()) {
				model.setSelection(c, false);
			}
			// On retire le dernier coup de l'IA si necessaire
			if (gameType.isCpuTurn(model) && historicModel.getSize() > 0) {
				coup = model.removeCoup();
				for (Case c : coup.getCases()) {
					model.setSelection(c, false);
				}
			}
			endGame = false;
			cpuPhase = (gameType.isCpuTurn(model));
			// On notifie le thread si jamais la partie etait finie
			synchronized (this) {
				notify();
			}
		}
	}
	
	@Override
	public void forkClicked() {
		// isEventDispatch = true;
		if (isHumanPhase() || endGame) {
			ChessModel clone = model.clone();
			treeModel.add(currentNode, clone);
		}
	}
	
	@Override
	public void historicClicked(int index) {
		// isEventdispatch = true
		if (isHumanPhase() || endGame) {
			if (casePlayed != null) {
				setSelectionCaseJouable(casePlayed, false);
				casePlayed = null;
			}
			model.goTo(index);
		}
	}
	
	///
	///	Listener sur JChessTree
	///
	@Override
	public void valueChanged(TreeSelectionEvent ev) {
		if ((isHumanPhase() || endGame) && ev.isAddedPath()) {
			TreePath treePath = ev.getPath();
			Node node = (Node) treePath.getPathComponent(treePath.getPathCount()-1);
			if (node != currentNode) {
				// Si des cases sont selectionnees
				if (casePlayed != null) {
					setSelectionCaseJouable(casePlayed, false);
					casePlayed = null;
				}
				// Change le model
				placeAtNode(node);
				synchronized (this) {
					notify();
				}
			}
		}
	}
	
	
	
	@Override
	public synchronized void run() {
		while (true) {
			
			while (endGame) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			while (coup == null) {
				try {
					wait(100);
					// Fait jouer l'ordinateur
					if (cpuPhase) {
						searchCoup();
					// Sinon attend qu'un humain joue un coup
					} else {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					coup = null;
				}
			}
			
			if (cpuPhase) {
				jouerCoupCpu(coup);
			} else {
				jouerCoup(coup);
			}
			
			resultCoup(coup);
			coup = null;
		}
	}
}
