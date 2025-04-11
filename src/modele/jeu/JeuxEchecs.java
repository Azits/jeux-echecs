package modele.jeu;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class JeuxEchecs extends Jeu{
	public JeuxEchecs() {
        super();
    }
    public boolean coupValide(Case caseClic1, Case caseClic2) {
        boolean valide = false;
        Plateau clone = getPlateau().clone();

        int x1 = caseClic1.getX();
        int y1 = caseClic1.getY();
        int x2 = caseClic2.getX();
        int y2 = caseClic2.getY();

        Case caseClone1 = clone.getCase(x1,y1);
        Case caseClone2 = clone.getCase(x2,y2);

        if (caseClone1.getPiece() != null) {
            ArrayList<Case> casesAccessiblesC = caseClone1.getPiece().getCasesAccessibles();
            if (casesAccessiblesC.contains(caseClone2)) {
                clone.deplacerPiece(caseClone1, caseClone2);

                if (!enEchec(getCouleurJoueurActuel(), clone)) {
                    valide = true;
                }
            }
        }
        return valide;
    }
}
