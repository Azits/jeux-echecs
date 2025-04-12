package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;

public class PionDame_simple extends Piece{
    public PionDame_simple(Plateau _plateau, String _couleur) {
        super(_plateau,_couleur);
        casesAccessibles = new DecorateurPionDame(null);
    }

    @Override
    public ArrayList<Case> getCasesAccessibles() {
        casesAccessibles.setPlateau(this.plateau);
        casesAccessibles.setPiece(this);
        return this.casesAccessibles.getCasesAccessibles();
    }

    @Override
    public Piece clone(Plateau clone) {
        Pion clonePion = new Pion(clone, this.couleur);
        return clonePion;
    }
}
