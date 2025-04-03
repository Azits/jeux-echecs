package modele.plateau;

import java.util.ArrayList;

import modele.jeu.Piece;

public class DecorateurCasesEnL extends DecorateurCasesAccessibles {

    public DecorateurCasesEnL(DecorateurCasesAccessibles _baseDecorateur) {
		super(_baseDecorateur);
		// TODO Auto-generated constructor stub
	}

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> accessibles = new ArrayList<>();

        Case caseDepart = getPosition();
        int xDepart = caseDepart.getX();
        int yDepart = caseDepart.getY();

        Plateau plateau = getPlateau();
        Piece piece = getPiece();
        String couleur = piece.getCouleur();
        
        int[][] mouvements = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] m : mouvements) {
            int x = xDepart + m[0];
            int y = yDepart + m[1];

            if (estPositionValide(x, y)) {
                Case cible = plateau.getCases()[x][y];

                if (cible.vide() || !cible.getPiece().getCouleur().equals(couleur)) {
                    accessibles.add(cible);
                }
            }
        }

        return accessibles;
    }
}