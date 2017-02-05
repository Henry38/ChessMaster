package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import controler.ChessListener;
import controler.ChessModelListener;
import coup.Coup;
import data.Case;
import data.ChessModel;

public final class JChess extends JComponent {
	
	private static final long serialVersionUID = 1L;
	public static final String MODEL_CHANGED_PROPERTY = "model";
	
	private ChessModel model;
	private Handler handler;
	private JLabel[][] labels;
	
	private String ressourceFolder;
	private Hashtable<String, ImageIcon> hashIcon;
	
	/** Constructeur */
	public JChess(ChessModel model) {
		super();
		this.model = null;
		this.handler = new Handler();
		this.ressourceFolder = "standard";
		
		this.hashIcon = new Hashtable<String, ImageIcon>();
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File file, String filename) {
				return filename.toLowerCase().endsWith(".jpg");
			}
		};
		
		File root = new File("ressource/ui/");
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				for (File img : file.listFiles(filter)) {
					String key = img.getPath();
					ImageIcon value = new ImageIcon(key);
					hashIcon.put(key, value);
				}
			}
		}
		
		labels = new JLabel[8][];
		for (int line = 0; line < 8; line++) {
			labels[line] = new JLabel[8];
			for (int column = 0; column < 8; column++) {
				JLabel label = new JLabel();
				label.setBounds(1 + column * 50, 1 + line * 50, 49, 49);
				labels[line][column] = label;
				add(label);
			}
		}
//		this.grid = new JLabel(new ImageIcon("ressource/ui/grid.png"));
//		grid.setBounds(0, 0, 433, 433);
//		add(grid);
		
		setModel(model);
		
		addMouseListener(handler);
		setPreferredSize(new Dimension(401, 401));
	}
	
	/** Retourne le modele */
	public ChessModel getModel() {
		return model;
	}
	
	private Handler getHandler() {
		return handler;
	}
	
	/** Met a jour le modele */
	public void setModel(ChessModel newModel) {
		ChessModel oldModel = getModel();
		
		if (oldModel != null) {
			oldModel.removeChessListener(getHandler());
			for (int line = 0; line < 8; line++) {
				for (int column = 0; column < 8; column++) {
					labels[line][column].setIcon(null);
				}
			}
		}
		
		model = newModel;
		
		if (newModel != null) {
			newModel.addChessListener(getHandler());
			for (int line = 0; line < 8; line++) {
				for (int column = 0; column < 8; column++) {
					refreshIconCase(line, column);
				}
			}
		}
		
		firePropertyChange(MODEL_CHANGED_PROPERTY, oldModel, model);
	}
	
	private void refreshIconCase(int line, int column) {
		String iconString = getModel().getCase(line, column).toIconString();
		String key = "ressource/ui/" + ressourceFolder + "/" + iconString + ".jpg";
		labels[line][column].setIcon(hashIcon.get(key));
	}
	
	// TODO : implementer une option reverse dans la GUI
	/** Renverse le plateau pour l'affichage */
	public void reverse(boolean b) {
		JLabel label = null;
		int r = (b ? 7 : 0);
		for (int line = 0; line < 8; line++) {
			for (int column = 0; column < 8; column++) {
				label = labels[line][column];
				label.setLocation(1 + (r-column) * 50, 1 + (r-line) * 50);
			}
		}
	}
	
	/** Dessine le plateau */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
	
	/** Ajoute un listener sur le modele */
	public void addChessListener(ChessListener l) {
		listenerList.add(ChessListener.class, l);
	}
	
	/** Retire un listener sur le modele */
	public void removeChessListener(ChessListener l) {
		listenerList.remove(ChessListener.class, l);
	}
	
	/** Notifie les listeners qui ecoute la vue */
	private void fireCaseClicked(int line, int column) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessListener) {
				((ChessListener) listeners[i]).caseClicked(line, column);
			}
		}
	}
	
	
	
	/** Classe qui ecoute le modele et les click utilisateur */
	private class Handler extends MouseAdapter implements ChessModelListener {
		
		///
		/// MouseListener
		///
		@Override
		public void mousePressed(MouseEvent ev) {
			int line = ev.getY() / (getHeight() / 8);
			int column = ev.getX() / (getWidth() / 8);
			if (getModel() != null) {
				fireCaseClicked(line, column);
			}
		}
		
		///
		/// ChessListener
		///
		public void caseChanged(int line, int column) {
			refreshIconCase(line, column);
		}
		
		public void coupAdded(Coup coup) {
			for (Case c : coup.getCases()) {
				int line = c.line;
				int column = c.column;
				refreshIconCase(line, column);
			}
		}
		
		public void coupRemoved(Coup coup) {
			for (Case c : coup.getCases()) {
				int line = c.line;
				int column = c.column;
				refreshIconCase(line, column);
			}
		}
	}
}
