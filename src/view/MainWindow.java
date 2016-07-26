package view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import controler.Controler;
import controler.MenuListener;
import data.ChessModel;
import data.GameType;
import data.Plateau;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JChess chess;
	private JChessHistoric chessHistorique;
	private JChessTree chessTree;
	private Controler controler;
	private JMenuBar menu;
	
	/** Constructeur */
	public MainWindow() {
		super("Master Chess");
		
		ChessModel model = new Plateau();
		this.chess = new JChess(model);
		this.chessHistorique = new JChessHistoric(model);
		this.chessTree = new JChessTree(model);
		this.controler = new Controler(chess, chessHistorique, chessTree);
		//this.controler.newGame(GameType.joueur_contre_ordinateur);
		
		this.menu = new JChessMenu(new MyMenuListener());
		
		// Game Board
		JPanel panelChess = new JPanel();
		panelChess.setLayout(new GridBagLayout());
		panelChess.add(chess);
		JScrollPane panelBoard = new JScrollPane(panelChess);
		
		// GameBoardInfo
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new GridLayout(1, 2));
		panelInfo.add(chessHistorique);
		panelInfo.add(chessTree);
		
		JPanel panneau = new JPanel();
		panneau.setLayout(new BorderLayout());
		panneau.add(panelBoard, BorderLayout.CENTER);
		panneau.add(panelInfo, BorderLayout.EAST);
		
		setContentPane(panneau);
		setJMenuBar(menu);
		
		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	/** Listener prive sur le Menu */
	private class MyMenuListener implements MenuListener {
		
		public void newGame(GameType gameType) {
			controler.newGame(gameType);
		}
		
		public void exit() {
			dispose();
		}
	}
	
	public static void main(String[] args) {
		// TODO : changer tous les 'historique' par 'historic'
		
		Runnable run = new Runnable() {
			public void run() {
				MainWindow fen = new MainWindow();
				fen.setVisible(true);
			}
		};
		
		SwingUtilities.invokeLater(run);
		
	}
}
