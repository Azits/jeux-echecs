package modele.jeu;

import java.util.ArrayList;

import modele.plateau.Case;
import modele.plateau.DecorateurCasesAccessibles;
import modele.plateau.Direction;
import modele.plateau.Plateau;

/**
 * Entités amenées à bouger
 */
public abstract class Piece {

    protected Case c;
    protected Plateau plateau;

    protected DecorateurCasesAccessibles casesAccessibles;
    protected String couleur;

    public Piece(Plateau _plateau,String _couleur) {
        this.plateau = _plateau;
        this.couleur=_couleur;
    }

    public void quitterCase() {
        c.quitterLaCase();
    }
    public void allerSurCase(Case _c) {
        if (c != null) {
            quitterCase();
        }
        c = _c;
        plateau.arriverCase(c, this);
    }

    public Case getCase() {
        return c;
    }
    public String getCouleur() {
    	return couleur;
    }

	public abstract ArrayList<Case> getCasesAccessibles(ArrayList<Case> casesEnemieCapture);


    public abstract Piece clone(Plateau clone);
}
