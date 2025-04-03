package modele.plateau;

import java.util.ArrayList;

import modele.jeu.Piece;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {

    public DecorateurCasesEnLigne(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesAccessibles = new ArrayList<>();

       
        Case caseDepart = this.getPosition();

        int xDepart = caseDepart.getX();
        int yDepart = caseDepart.getY();

        Piece piece = this.getPiece();

       
        Plateau plateau = this.getPlateau();

        int[][] directions = {
            {1, 0},    // Vers le bas
            {-1, 0},   // Vers le haut
            {0, 1},    // Vers la droite
            {0, -1}    // Vers la gauche
        };

        for (int d = 0; d < directions.length; d++) {
            int dx = directions[d][0];
            int dy = directions[d][1];

            int x = xDepart + dx;
            int y = yDepart + dy;

           
            while (estPositionValide(x, y)) {
                Case caseCible = plateau.getCases()[x][y];

              
                if (caseCible.vide()) {
                    casesAccessibles.add(caseCible);
                } else {
                  
                    if (!caseCible.getPiece().getCouleur().equals(piece.getCouleur())) {
                        casesAccessibles.add(caseCible);
                    }
                  
                    break;
                }

                x = x + dx;
                y = y + dy;
            }
        }

        return casesAccessibles;
    }
}
