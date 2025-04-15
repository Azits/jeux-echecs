package modele.plateau;

import java.awt.*;
import java.util.ArrayList;

import modele.jeu.Piece;

public class DecorateurCasesEnL extends DecorateurCasesAccessibles {

    public DecorateurCasesEnL(DecorateurCasesAccessibles _baseDecorateur) {
		super(_baseDecorateur);
		// TODO Auto-generated constructor stub
	}

    @Override
    public ArrayList<Case> getMesCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
        ArrayList<Case> cases = new ArrayList<>();
        Case depart = piece.getCase();

        for (DirectionEnL dir : DirectionEnL.values()) {
            int xx = depart.getX() + dir.dx;
            int yy = depart.getY() + dir.dy;
            Point p = new Point(xx,yy);

            if (plateau.contenuDansGrille(p)) {
                Case cible = plateau.getCaseDansDirection(depart, dir.dx, dir.dy);
                if (cible.vide() || !cible.getPiece().getCouleur().equals(piece.getCouleur())) {
                    cases.add(cible);
                }
            }
        }

        return cases;
    }
}