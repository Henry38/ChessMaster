package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.Color;

public class PromoteDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel panneau;
	private JLabel reine, tour, fou, cavalier;
	private ImageIcon reineClaire, reineFoncee;
	private ImageIcon tourClaire, tourFoncee;
	private ImageIcon fouClair, fouFonce;
	private ImageIcon cavalierClair, cavalierFonce;
	private int id;
	
	/** Constructeur */
	private PromoteDialog(JFrame frame, String title, boolean modal, Color color) {
		super(frame, title, modal);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.setLayout(new GridLayout(2, 1));
	    
	    if (color.isWhite()) {
	    	reineClaire = new ImageIcon("Pieces1/b2b.jpg");
	    	reineFoncee = new ImageIcon("Pieces1/b2n.jpg");
	    	tourClaire = new ImageIcon("Pieces1/b5b.jpg");
	    	tourFoncee = new ImageIcon("Pieces1/b5n.jpg");
	    	fouClair = new ImageIcon("Pieces1/b3b.jpg");
	    	fouFonce = new ImageIcon("Pieces1/b3n.jpg");
	    	cavalierClair = new ImageIcon("Pieces1/b4b.jpg");
	    	cavalierFonce = new ImageIcon("Pieces1/b4n.jpg");
	    } else {
	    	reineClaire = new ImageIcon("Pieces1/n2b.jpg");
	    	reineFoncee = new ImageIcon("Pieces1/n2n.jpg");
	    	tourClaire = new ImageIcon("Pieces1/n5b.jpg");
	    	tourFoncee = new ImageIcon("Pieces1/n5n.jpg");
	    	fouClair = new ImageIcon("Pieces1/n3b.jpg");
	    	fouFonce = new ImageIcon("Pieces1/n3n.jpg");
	    	cavalierClair = new ImageIcon("Pieces1/n4b.jpg");
	    	cavalierFonce = new ImageIcon("Pieces1/n4n.jpg");
	    }
	    
	    reine = new JLabel();
	    reine.setPreferredSize(new Dimension(50, 50));
	    reine.setIcon(reineClaire);
	    reine.addMouseListener(new LabelListener());
	    
	    tour = new JLabel();
	    tour.setPreferredSize(new Dimension(50, 50));
	    tour.setIcon(tourClaire);
	    tour.addMouseListener(new LabelListener());
	    
	    fou = new JLabel();
	    fou.setPreferredSize(new Dimension(50, 50));
	    fou.setIcon(fouClair);
	    fou.addMouseListener(new LabelListener());
	    
	    cavalier = new JLabel();
	    cavalier.setPreferredSize(new Dimension(50, 50));
	    cavalier.setIcon(cavalierClair);
	    cavalier.addMouseListener(new LabelListener());
	    
	    panneau = new JPanel();
	    panneau.setLayout(new FlowLayout());
	    
	    panneau.add(reine);
	    panneau.add(tour);
	    panneau.add(fou);
	    panneau.add(cavalier);
	    
	    add(new JLabel("Choisir une promotion :", JLabel.CENTER));
	    add(panneau);
	    
	    pack();
	}
	
	/** Retourne l'id de la piece choisie */
	public int getId() {
		return id;
	}
	
	private class LabelListener implements MouseListener {
		
		private boolean press = false;
		private boolean enter = false;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (press) {
				enter = true;
				if (e.getSource() == reine) {
					reine.setIcon(reineFoncee);
				} else if (e.getSource() == tour) {
					tour.setIcon(tourFoncee);
				} else if (e.getSource() == fou) {
					fou.setIcon(fouFonce);
				} else if (e.getSource() == cavalier) {
					cavalier.setIcon(cavalierFonce);
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			if (press) {
				enter = false;
				if (e.getSource() == reine) {
					reine.setIcon(reineClaire);
				} else if (e.getSource() == tour) {
					tour.setIcon(tourClaire);
				} else if (e.getSource() == fou) {
					fou.setIcon(fouClair);
				} else if (e.getSource() == cavalier) {
					cavalier.setIcon(cavalierClair);
				}
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			press = true;
			enter = true;
			if (e.getSource() == reine) {
				reine.setIcon(reineFoncee);
			} else if (e.getSource() == tour) {
				tour.setIcon(tourFoncee);
			} else if (e.getSource() == fou) {
				fou.setIcon(fouFonce);
			} else if (e.getSource() == cavalier) {
				cavalier.setIcon(cavalierFonce);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			press = false;
			if (enter) {
				if (e.getSource() == reine) {
					id = 2;
				} else if (e.getSource() == tour) {
					id = 5;
				} else if (e.getSource() == fou) {
					id = 3;
				} else if (e.getSource() == cavalier) {
					id = 4;
				}
				// Termine la boite de dialogue
				setVisible(false);
			}
		}
	}
	
	/** Lance l'affichage de la boite de dialogue et retourne l'id de la piece choisie */
	public static int showMessageDialog(Color color) {
		PromoteDialog dialog = new PromoteDialog(null, "Promotion", true, color);
		// Lance la boite de dialogue
		dialog.setVisible(true);
		return dialog.getId();
	}
	
}
