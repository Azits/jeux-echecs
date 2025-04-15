/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;


public class Cavalier extends Piece
{
    public Cavalier(Plateau _plateau,String _couleur) {
        super(_plateau,_couleur);
        casesAccessibles = new DecorateurCasesEnL(null);

        // le décorateur récupère les cases en diagonale et en ligne
        // ArrayList<Case> lst = casesAccessibles.getCasesAccessibles();

    }

	@Override
	public ArrayList<Case> getCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
		casesAccessibles.setPlateau(this.plateau);
		casesAccessibles.setPiece(this);
	    
		return this.casesAccessibles.getCasesAccessibles(casesEnemieCapture);
	}
	public Piece clone(Plateau clone) {
		return new Cavalier(clone,this.couleur);
	}
}
