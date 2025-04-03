package modele.plateau;

import java.util.ArrayList;

import modele.jeu.Piece;

public class DecorateurCasesEnDiagonale extends DecorateurCasesAccessibles {

    public DecorateurCasesEnDiagonale(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesAccessibles = new ArrayList<>();

        //
        Case caseDepart = this.getPosition();
        int xDepart = caseDepart.getX();
        int yDepart = caseDepart.getY();

        Piece piece = this.getPiece();

        // Récupérer le plateau
        Plateau plateau = this.getPlateau();

        
        int[][] directions = {
            {-1, -1}, // Haut-gauche
            {-1, 1},  // Haut-droite
            {1, -1},  // Bas-gauche
            {1, 1}    // Bas-droite
        };

        // Parcours de chaque direction
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
