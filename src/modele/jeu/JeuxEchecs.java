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
    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        Case caseRoi = plateau.getCaseRoi(couleurJoueur);

        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && !p.getCouleur().equals(couleurJoueur)) {
                    ArrayList<Case> attaques = p.getCasesAccessibles();
                    if (attaques.contains(caseRoi)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public boolean estPat(String couleur) {
        Plateau clone=getPlateau().clone();
        if (enEchec(couleur,clone)) return false;

        ArrayList<Case> cases = clone.getCaseAvecPieces(couleur);
        for (Case c : cases) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles();

            for (Case d : destinations) {
                clone.deplacerPiece(c,d);

                boolean echecApresCoup = enEchec(couleur,clone);

                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean echecEtMat(String couleurAdverse) {
        if (!enEchec(couleurAdverse, getPlateau())) {
            return false;
        }
        Plateau p=getPlateau().clone();
        ArrayList<Case> caseAvecPiceMemeCouleur = p.getCaseAvecPieces(couleurAdverse);

        for (Case c : caseAvecPiceMemeCouleur) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles();

            for (Case d : destinations) {
                p.deplacerPiece(c,d);
                boolean echecApresCoup = enEchec(couleurAdverse,p);
                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        System.out.println("Echec Et math");
        return true;
    }

    public boolean partieGagner() {
        if(echecEtMat(getCouleurJoueurSuivant()) || estPat(getCouleurJoueurSuivant()) ) {
                return true;
        }
        return false;
    }
}
