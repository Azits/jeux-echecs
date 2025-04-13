/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;


public class Roi extends Piece
{
    public Roi(Plateau _plateau,String _couleur) {
        super(_plateau,_couleur);
        casesAccessibles = new DecorateurCasesRoi(null);

        // le décorateur récupère les cases en diagonale et en ligne
        // ArrayList<Case> lst = casesAccessibles.getCasesAccessibles();

    }
    public ArrayList<Case> getCasesAccessibles() {
		casesAccessibles.setPlateau(this.plateau);
		casesAccessibles.setPiece(this);
	    
		return this.casesAccessibles.getCasesAccessibles();
	}
    public Piece clone(Plateau clone) {
        return new Roi(clone,this.couleur);
    }

    private boolean aDejaBouge = false;

    public boolean aDejaBouge() {
        return aDejaBouge;
    }

    public void setDejaBouge(boolean dejaBouge) {
        this.aDejaBouge = dejaBouge;
    }



}
