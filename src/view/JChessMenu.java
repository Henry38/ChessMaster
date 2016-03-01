package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controler.MenuListener;
import data.GameType;

public class JChessMenu extends JMenuBar {
	
	private static final long serialVersionUID = 1L;
	private JMenu fichier, difficulte;
	private JMenuItem start, end, niveau1, niveau2, niveau3, hvh, hvo, ovh, ovo;
	private MenuListener listener;
	
	/** Constructeur */
	public JChessMenu(MenuListener menuListener) {
		super();
		this.listener = menuListener;
		
		// Creation du sous-menu nouvelle partie
		start = new JMenu("Nouvelle partie");
		end = new JMenuItem("Quitter");
		hvh = new JMenuItem("Joueur contre Joueur");
		hvo = new JMenuItem("Joueur contre ordinateur");
		ovh = new JMenuItem("Ordinateur contre Joueur");
		ovo = new JMenuItem("Ordinateur contre Ordinateur");
		start.add(hvh);
		start.add(hvo);
		start.add(ovh);
		start.add(ovo);
		
		// Creation du menu fichier avec ses sous-menus
		fichier = new JMenu("Fichier");
		fichier.add(start);
		fichier.add(end);
		
		// Creation du menu difficulte IA avec ses-sous menus
		difficulte = new JMenu("Difficulte IA");
		niveau1 = new JMenuItem("facile");
		niveau2 = new JMenuItem("intermediaire");
		niveau3 = new JMenuItem("difficile");
		difficulte.add(niveau1);
		difficulte.add(niveau2);
		difficulte.add(niveau3);
		
		// Ajout des JMenuItem au menu
		this.add(fichier);
		this.add(difficulte);
		
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.exit();
			}
		});
		
		// Creation des listener pour chaque sous menu
		hvh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.newGame(GameType.joueur_contre_joueur);
			}
		});
		
		hvo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.newGame(GameType.joueur_contre_ordinateur);
			}
		});
		
		ovh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.newGame(GameType.ordinateur_contre_joueur);
			}
		});
		
		ovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.newGame(GameType.ordinateur_contre_ordinateur);
			}
		});
	}
}