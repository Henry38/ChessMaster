package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import data.ChessModel;
import data.ChessTreeModel;
import data.ChessTreeModel.Node;

public class JChessTree extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private ChessTreeModel treeModel;
	private Handler handler;
	
	private JToolBar toolBar;
	private JButton clear;
	
	/** Constructeur */
	public JChessTree(ChessModel model) {
		super();
		this.handler = new Handler();
		
		setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
        toolBar.setLayout(new GridLayout(1, 1));
        toolBar.setFloatable(false);
        
        clear = new JButton("Clear");
        clear.setPreferredSize(new Dimension(48, 24));
        clear.addActionListener(getHandler());
		toolBar.add(clear);
		
		////////// Tree //////////
		treeModel = new ChessTreeModel(model);
		tree = new JTree(treeModel);
		tree.addTreeSelectionListener(getHandler());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
        JPanel panelTree = new JPanel(new BorderLayout());
        panelTree.add(tree, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(panelTree);
		
		add(toolBar, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/** Retourne le modele du JTree */
	public ChessTreeModel getTreeModel() {
		return treeModel;
	}
	
	private Handler getHandler() {
		return handler;
	}
	
	
	
	/** Ajoute un listener selection sur le modele */
	public void addTreeSelectionListener(TreeSelectionListener l) {
		listenerList.add(TreeSelectionListener.class, l);
	}
	
	/** Retire un listener selection sur le modele */
	public void removeTreeSelectionListener(TreeSelectionListener l) {
		listenerList.remove(TreeSelectionListener.class, l);
	}
	
	/** Notifie les listeners qui ecoute la vue */
	private void fireValueChanged(TreeSelectionEvent ev) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof TreeSelectionListener) {
				((TreeSelectionListener) listeners[i]).valueChanged(ev);
			}
		}
	}
	
	
	
	/** Classe qui ecoute le modele */
	private class Handler implements ActionListener, TreeSelectionListener {
		
		public void actionPerformed(ActionEvent ev) {
			if (ev.getSource() == clear) {
				if (tree.getSelectionModel().getSelectionCount() > 0) {
					TreePath treePath = tree.getSelectionModel().getSelectionPath();
					Node node = (Node) treePath.getPathComponent(treePath.getPathCount()-1);
					treeModel.clear(node);
				}
			}
		}
		
		public void valueChanged(TreeSelectionEvent ev) {
			fireValueChanged(ev);
		}
	}
}
