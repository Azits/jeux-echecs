package modele.jeu;
import modele.plateau.Case;

import java.util.ArrayList;

public class JeuxEchecs extends Jeu{
	public JeuxEchecs() {
        super();
    }
    public boolean coupValide(Case caseClic1, Case caseClic2) {
        boolean valide = false;
        ArrayList<Case> caseAccessible = caseClic1.getPiece().getCasesAccessibles();
        if (caseAccessible.contains(caseClic2)) {
            Case dep = caseClic1;
            Case arr = caseClic2;

            Piece piece = dep.getPiece();
            Piece pieceCapturee = arr.getPiece();

            // Simuler le coup
            dep.quitterLaCase();
            arr.setPiece(piece);
            piece.allerSurCase(arr);

            // Valider si pas en échec
            if (!enEchec(piece.getCouleur())) {
                valide = true;
            }

            // Annuler la simulation
            arr.setPiece(pieceCapturee);
            dep.setPiece(piece);
            piece.allerSurCase(dep);
            if (pieceCapturee != null) {
                pieceCapturee.allerSurCase(arr);
            }
        }
        return valide;
    }
}
