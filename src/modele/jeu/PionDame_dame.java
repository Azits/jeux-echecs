package modele.jeu;

import modele.plateau.Case;
import modele.plateau.DecorateurPionDame;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class PionDame_dame extends Piece{
    public PionDame_dame(Plateau _plateau, String _couleur) {
        super(_plateau,_couleur);
        casesAccessibles = new DecorateurPionDame(null);
    }

    @Override
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
