package modele.plateau;

import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

	Plateau plateau; // TODO
    Piece piece; // TODO

    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur) {
        base = _baseDecorateur;
    }

    public ArrayList<Case> getCasesAccessibles() {
        ArrayList<Case> retour = getMesCasesAccessibles();

        if (base != null) {
            retour.addAll(base.getCasesAccessibles());
        }

        return retour;
    }

    public abstract ArrayList<Case> getMesCasesAccessibles();
    
    public Plateau getPlateau() {
        return plateau;
    }
    public void setPlateau(Plateau plateau) {
        this.plateau= plateau;
    }
   

    public Piece getPiece() {
        return piece;
    }
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    

    public Case getPosition() {
        return piece.getCase();
    }

    public boolean estPositionValide(int x, int y) {
        return plateau.positionValide(x, y);
    }


}
