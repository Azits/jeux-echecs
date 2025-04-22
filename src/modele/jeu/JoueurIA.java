package modele.jeu;

import java.util.ArrayList;
import java.util.Random;

import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class JoueurIA extends Joueur{
	public JoueurIA(Jeu _jeu,String nom,String Couleur) {
        super(_jeu,nom,Couleur);

	}
	public abstract Coup getCoup();
    protected abstract int evaluerCoupSimple(Coup coup);

}
