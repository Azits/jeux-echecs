package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class JeuDames extends  Jeu{
    public JeuDames(String jeu,String typeAdverssaire) {
        super(jeu,typeAdverssaire);
    }

    @Override
    public boolean partieGagner() {
        return false;
    }

    @Override
    public boolean coupValide(Case caseClic1, Case caseClic2) {
        boolean valide = false;
        Plateau clone = getPlateau().clone();

        int x1 = caseClic1.getX();
        int y1 = caseClic1.getY();
        int x2 = caseClic2.getX();
        int y2 = caseClic2.getY();

        Case caseClone1 = clone.getCase(x1, y1);
        Case caseClone2 = clone.getCase(x2, y2);

        if (caseClone1.getPiece() != null) {

            ArrayList<Case> casesAccessiblesC = caseClone1.getPiece().getCasesAccessibles();

            boolean prisePossible = false;

            // Parcourir toutes les pièces du joueur
            ArrayList<Case> casesJoueur = clone.getCaseAvecPieces(caseClic1.getPiece().getCouleur());

            for (Case c : casesJoueur) {
                ArrayList<Case> deplacements = c.getPiece().getCasesAccessibles();

                for (Case dest : deplacements) {
                    int dx = Math.abs(dest.getX() - c.getX());
                    int dy = Math.abs(dest.getY() - c.getY());

                    // Si déplacement de 2 ou plus = prise possible
                    if (dx >= 2 || dy >= 2) {
                        prisePossible = true;
                    }
                }
            }

            // S'il y a une prise possible → on doit vérifier que le joueur fait bien une prise
            if (prisePossible) {
                int dx = Math.abs(x2 - x1);
                int dy = Math.abs(y2 - y1);

                if ((dx >= 2 || dy >= 2) && casesAccessiblesC.contains(caseClone2)) {
                    valide = true;
                }
            } else {
                // Sinon mouvement libre classique
                if (casesAccessiblesC.contains(caseClone2)) {
                    valide = true;
                }
            }
        }
        return valide;
    }


    @Override
    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        return false;
    }
}
