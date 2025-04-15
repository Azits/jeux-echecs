package modele.jeu;

import modele.plateau.*;
import java.util.ArrayList;

public class Pion extends Piece {
	 public Pion(Plateau _plateau,String _couleur) {
	        super(_plateau,_couleur);
	        casesAccessibles = new DecorateurPion(null);

	        // le décorateur récupère les cases en diagonale et en ligne
	        // ArrayList<Case> lst = casesAccessibles.getCasesAccessibles();

	    }
	 public ArrayList<Case> getCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
			casesAccessibles.setPlateau(this.plateau);
			casesAccessibles.setPiece(this);
		    
			return this.casesAccessibles.getCasesAccessibles(casesEnemieCapture);
	}

	@Override
	public Piece clone(Plateau clone) {
		Pion clonePion = new Pion(clone, this.couleur);
		return clonePion;
	}
}
