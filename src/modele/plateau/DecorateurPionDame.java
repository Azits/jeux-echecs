package modele.plateau;

import modele.jeu.PionDame_dame;
import modele.jeu.PionDame_simple;

import java.util.ArrayList;

public class DecorateurPionDame extends DecorateurCasesAccessibles {
    public DecorateurPionDame(DecorateurCasesAccessibles baseDecorateur) {
        super(baseDecorateur);
    }
    public ArrayList<Case> getMesCasesAccessibles(ArrayList<Case> caseEnemieParcourue) {
        ArrayList<Case> res = new ArrayList<>();
        Case depart = piece.getCase();

        boolean estPionSimple = !(piece instanceof PionDame_dame); // Vérifie si la pièce est une Dame ou un pion normal

        for (DIirectionEnDiagonale dir : DIirectionEnDiagonale.values()) {
            Case caseA= plateau.getCaseDansDirection(depart, dir.dx, dir.dy);

            if (estPionSimple) {
                // Pion simple : 1 seule case
                if (caseA != null && caseA.vide()) {
                    res.add(caseA);
                }

                // Vérifier une prise possible
                if (caseA != null && !caseA.vide() && !caseA.getPiece().getCouleur().equals(piece.getCouleur())) {
                    Case apres = plateau.getCaseDansDirection(caseA, dir.dx, dir.dy);
                    if (apres != null && apres.vide()) {
                        res.add(apres);
                        trouverPrisesMultiples(apres, dir, res, caseEnemieParcourue, caseA);
                    }
                }

            } else {
                // Dame : déplacement infini en diagonale
                while (caseA != null && caseA.vide()) {
                    res.add(caseA);
                    caseA = plateau.getCaseDansDirection(caseA, dir.dx, dir.dy);
                }
                if (caseA != null && !caseA.vide() && !caseA.getPiece().getCouleur().equals(piece.getCouleur())) {
                    Case apres = plateau.getCaseDansDirection(caseA, dir.dx, dir.dy);
                    if (apres != null && apres.vide()) {
                        res.add(apres);
                        trouverPrisesMultiples(apres, dir, res, caseEnemieParcourue, caseA);
                    }
                }
            }
        }

        return res;
    }

    private void trouverPrisesMultiples(Case depart, DIirectionEnDiagonale directionInitiale, ArrayList<Case> res, ArrayList<Case> dejaPrises, Case dernierEnnemi) {
        dejaPrises.add(dernierEnnemi);

        for (DIirectionEnDiagonale dir : DIirectionEnDiagonale.values()) {
            Case current = plateau.getCaseDansDirection(depart, dir.dx, dir.dy);

            while (current != null && current.vide()) {
                current = plateau.getCaseDansDirection(current, dir.dx, dir.dy);
            }

            if (current != null && !current.vide() && !current.getPiece().getCouleur().equals(piece.getCouleur()) && !dejaPrises.contains(current)) {
                Case apres = plateau.getCaseDansDirection(current, dir.dx, dir.dy);
                if (apres != null && apres.vide()) {
                    res.add(apres);
                    trouverPrisesMultiples(apres, dir, res, dejaPrises, current);
                }
            }
        }
    }

}
