package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;


public class Reine extends Piece
{
    public Reine(Plateau _plateau,String _couleur) {
        super(_plateau,_couleur);
        casesAccessibles = new DecorateurCasesEnLigne(new DecorateurCasesEnDiagonale(null));

        // le décorateur récupère les cases en diagonale et en ligne
        // ArrayList<Case> lst = casesAccessibles.getCasesAccessibles();

    }
    public ArrayList<Case> getCasesAccessibles() {
		casesAccessibles.setPlateau(this.plateau);
		casesAccessibles.setPiece(this);
	    
		return casesAccessibles.getCasesAccessibles();
	}
}