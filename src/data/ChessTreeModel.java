package data;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ChessTreeModel implements TreeModel {
	
	protected Node racine;
	protected EventListenerList listenerList;
	
	/** Constructeur */
	public ChessTreeModel(ChessModel model) {
		this.racine = new Node(null, model);
		this.listenerList = new EventListenerList();
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}
	
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}
	
	protected void fireNodeInserted(Node node, int childIndice) {
		Object source = this;
		Object[] path = getPathToRoot(node.getParent()).toArray();
		int[] childIndices = new int[] {childIndice};
		Object[] children = new Object[] {node};
		TreeModelEvent ev = new TreeModelEvent(source, path, childIndices, children);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof TreeModelListener) {
				((TreeModelListener) listeners[i]).treeNodesInserted(ev);
			}
		}
	}
	
	protected void fireNodeRemoved(Node node, int childIndice) {
		Object source = this;
		Object[] path = getPathToRoot(node.getParent()).toArray();
		int[] childIndices = new int[] {childIndice};
		Object[] children = new Object[] {node};
		TreeModelEvent ev = new TreeModelEvent(source, path, childIndices, children);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof TreeModelListener) {
				((TreeModelListener) listeners[i]).treeNodesRemoved(ev);
			}
		}
	}
	
	protected void fireNodeChanged(Node node) {
		Object source = this;
		Object[] path = new Object[] {node};
		TreeModelEvent ev = new TreeModelEvent(source, path);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] instanceof TreeModelListener) {
				((TreeModelListener) listeners[i]).treeNodesChanged(ev);
			}
		}
	}
	
	
	
	@Override
	public Object getChild(Object obj, int index) {
		Node node = (Node) obj;
		return node.getChild(index);
	}
	
	@Override
	public int getChildCount(Object obj) {
		Node node = (Node) obj;
		return node.getChildCount();
	}
	
	@Override
	public int getIndexOfChild(Object obj, Object child) {
		Node node = (Node) obj;
		return node.getIndexOfChild(child);
	}
	
	@Override
	public Object getRoot() {
		return racine;
	}
	
	@Override
	public boolean isLeaf(Object obj) {
		Node node = (Node) obj;
		return node.isLeaf();
	}
	
	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// pas implemente
	}
	
	
	
	/** Ajoute un fils au noeud obj de l'arbre */
	public void add(Object obj, ChessModel model) {
		Node node = (Node) obj;
		Node child = new Node(node, model);
		node.add(child);
		fireNodeInserted(child, node.getChildCount()-1);
	}
	
	/** Retire le fils n°index du noeud obj */
	public void remove(Object obj, int index) {
		Node node = (Node) obj;
		Node child = node.getChild(index);
		node.remove(index);
		fireNodeRemoved(child, index);
	}
	
	/** Retire tous les fils du noeud obj */
	public void clear(Object obj) {
		int n = getChildCount(obj)-1;
		while (n >= 0) {
			remove(obj, n);
			n--;
		}
	}
	
	/** Retourne le chemin pour aller de la racine a node */
	private ArrayList<Node> getPathToRoot(Node node) {
		ArrayList<Node> array;
		if (node.getParent() == null) {
			array = new ArrayList<Node>(0);
			array.add(node);
			return array;
		}
		array = getPathToRoot(node.getParent());
		array.add(node);
		return array;
	}
	
	
	
	/** Classe representant un noeud du TreeModel */
	public class Node {
		
		private ChessModel model;
		private Node parent;
		private ArrayList<Node> fils;
		private String name;
		
		/** Constructeur */
		public Node(Node parent, ChessModel model) {
			this.model = model;
			this.parent = parent;
			this.fils = new ArrayList<Node>();
			if (parent == null) {
				this.name = "1";
			} else {
				this.name = parent.toString() + "." + (parent.getChildCount()+1);
			}
		}
		
		/** Retourne le modele du noeud */
		public ChessModel getModel() {
			return model;
		}
		
		/** Met a jour le modele du noeud */
		protected void setModel(ChessModel model) {
			this.model = model;
		}
		
		/** Retourne le noeud parent */
		protected Node getParent() {
			return parent;
		}
		
		/** Retourne le noeud enfant n°index */
		protected Node getChild(int index) {
			return fils.get(index);
		}
		
		/** Retourne le nombre de fils */
		protected int getChildCount() {
			return fils.size();
		}
		
		/** Retourne l'index du noeud enfant passe en parametre */
		protected int getIndexOfChild(Object child) {
			return fils.indexOf(child);
		}
		
		/** Retourne vrai si l'arbre est une feuille */
		protected boolean isLeaf() {
			return (getChildCount() == 0);
		}
		
		/** Ajoute un fils a l'arbre */
		protected void add(Node node) {
			fils.add(node);
		}
		
		/** Retire le fils n°index */
		protected void remove(int index) {
			Node child = fils.get(index);
			int n = child.getChildCount()-1;
			while (n >= 0) {
				child.remove(n);
				n--;
			}
			child.setModel(null);
			child.fils.clear();
			fils.remove(index);
		}
		
		/** Enleve tous les fils du noeud */
		protected void clear() {
			int n = getChildCount()-1;
			while (n >= 0) {
				remove(n);
				n--;
			}
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
