package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controler.ChessHistoricListener;
import controler.ChessModelListener;
import coup.Coup;
import data.ChessModel;
import data.HistoricModel;

public final class JChessHistoric extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public static final String MODEL_CHANGED_PROPERTY = "model";
	
	private ChessModel model;
	private Handler handler;
	
	private JToolBar toolBar;
	private JButton backward, split, forward;
	private JList<Coup> tableCoup;
	private DefaultListModel<Coup> listModel;
	
	/** Constructeur */
	public JChessHistoric(ChessModel model) {
		super();
		this.model = null;
		this.handler = new Handler();
		
		setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
        toolBar.setLayout(new GridLayout(1, 3));
        toolBar.setFloatable(false);
        addButtons();
        
		////////// Historique //////////
        listModel = new DefaultListModel<Coup>();
		tableCoup = new JList<Coup>(listModel);
		tableCoup.getSelectionModel().addListSelectionListener(getHandler());
		tableCoup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCoup.setCellRenderer(new CellRenderer());
		tableCoup.setOpaque(false);
		
		setModel(model);
		
        JPanel panelHistorique = new JPanel(new BorderLayout());
        panelHistorique.add(tableCoup, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(panelHistorique);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
	}
	
	private void addButtons() {
		backward = new JButton("<<");
		backward.setPreferredSize(new Dimension(48, 24));
		backward.addActionListener(getHandler());
		toolBar.add(backward);
		
		split = new JButton("fork");
		split.setPreferredSize(new Dimension(48, 24));
		split.addActionListener(getHandler());
		toolBar.add(split);
		
		forward = new JButton(">>");
		forward.setPreferredSize(new Dimension(48, 24));
		forward.addActionListener(getHandler());
		toolBar.add(forward);
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
			listModel.removeAllElements();
		}
		
		model = newModel;
		
		if (newModel != null) {
			newModel.addChessListener(getHandler());
			HistoricModel hisoricModel = newModel.getHistoricModel();
			for (int index = 0; index < hisoricModel.getSize(); index++) {
				listModel.addElement(hisoricModel.getElementAt(index));
			}
			tableCoup.setSelectedIndex(hisoricModel.getIndex());
		}
		
		firePropertyChange(MODEL_CHANGED_PROPERTY, oldModel, model);
	}
	
	
	
	/** Ajoute un listener selection sur le modele */
	public void addChessHistoriqueListener(ChessHistoricListener l) {
		listenerList.add(ChessHistoricListener.class, l);
	}
	
	/** Retire un listener selection sur le modele */
	public void removeChessHistoriqueSelectionListener(ChessHistoricListener l) {
		listenerList.remove(ChessHistoricListener.class, l);
	}
	
	/** Notifie les listeners qui ecoute la vue */
	private void fireRemovedClicked() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessHistoricListener) {
				((ChessHistoricListener) listeners[i]).removedClicked();
			}
		}
	}
	
	/** Notifie les listeners qui ecoute la vue */
	private void fireForkClicked() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessHistoricListener) {
				((ChessHistoricListener) listeners[i]).forkClicked();
			}
		}
	}
	
	/** Notifie les listeners qui ecoute la vue */
	private void fireHistoriqueClicked(int index) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof ChessHistoricListener) {
				((ChessHistoricListener) listeners[i]).historicClicked(index);
			}
		}
	}
	
	
	
	/** Classe qui ecoute le modele, les click sur les boutons et sur l'historique */
	private class Handler implements ActionListener, ListSelectionListener, ChessModelListener {
		
		///
		/// ActionListener
		///
		@Override
		public void actionPerformed(ActionEvent ev) {
			if (ev.getSource() == backward) {
				fireRemovedClicked();
			} else if (ev.getSource() == split) {
				fireForkClicked();
			} else if (ev.getSource() == forward) { }
		}
		
		///
		/// ListSelectionListener
		///
		@Override
		public void valueChanged(ListSelectionEvent ev) {
			ListSelectionModel selection = (ListSelectionModel) ev.getSource();
			// La valeur est en cours d'ajustement quand la selection est entrain de changer 
			if (!selection.getValueIsAdjusting() && !selection.isSelectionEmpty()) {
				int index = selection.getLeadSelectionIndex();
				fireHistoriqueClicked(index);
			}
		}
		
		///
		/// ChessListener
		///
		@Override
		public void caseChanged(int line, int column) { }
		
		@Override
		public void coupAdded(Coup coup) {
			listModel.addElement(coup);
			tableCoup.clearSelection();
		}
		
		@Override
		public void coupRemoved(Coup coup) {
			listModel.removeElement(coup);
			tableCoup.clearSelection();
		}
	}
	
	private class CellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = 1L;
		//protected Border border = new LineBorder(Color.BLACK);
		protected Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
		
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        Coup coup = (Coup) value;
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
			if (coup.getColor().isWhite()) {
				setHorizontalAlignment(LEFT);
			} else {
				setHorizontalAlignment(RIGHT);
			}
	        setText(coup.getNotation());
	        setBorder(border);
	        return this;
		}
	}
}
