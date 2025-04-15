package modele.plateau;

import java.util.ArrayList;

public class DecorateurCasesRoi extends DecorateurCasesAccessibles {
    public DecorateurCasesRoi(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
        ArrayList<Case> cases = new ArrayList<>();
        Case depart = piece.getCase();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Case c = plateau.getCaseDansDirection(depart, dx, dy);
                if (c != null && (c.vide() || !c.getPiece().getCouleur().equals(piece.getCouleur()))) {
                    cases.add(c);
                }
            }
        }
        return cases;
    }
}
