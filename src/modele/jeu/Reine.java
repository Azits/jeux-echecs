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
    public ArrayList<Case> getCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
		casesAccessibles.setPlateau(this.plateau);
		casesAccessibles.setPiece(this);
		return this.casesAccessibles.getCasesAccessibles(casesEnemieCapture);
	}
    public Piece clone(Plateau clone) {
        Reine cloneReine = new Reine(clone,this.couleur);
        return cloneReine;
    }
}