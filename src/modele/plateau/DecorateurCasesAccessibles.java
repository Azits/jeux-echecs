package modele.plateau;

import modele.jeu.Jeu;
import modele.jeu.Piece;

import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

	protected Plateau plateau; // TODO
    protected Piece piece; // TODO
    protected Jeu jeu;

    protected DecorateurCasesAccessibles base;

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

    public void setPiece(Piece p) {
        this.piece = p;
        if (base != null)
            base.setPiece(p);
    }
    public void setPlateau(Plateau p) {
        this.plateau = p;
        if (base != null)
            base.setPlateau(p);
    }


    public Case getPosition() {
        return piece.getCase();
    }

    public boolean estPositionValide(int x, int y) {
        Point p=new Point(x,y);
        return plateau.contenuDansGrille(p);
    }


}
