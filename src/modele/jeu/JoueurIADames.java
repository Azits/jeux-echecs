package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class JoueurIADames extends JoueurIA {
    public JoueurIADames(Jeu _jeu, String nom, String Couleur) {
        super(_jeu, nom, Couleur);
    }

    @Override
    public Coup getCoup() {
        Coup meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        Plateau plateau = getJeu().getPlateau();
        ArrayList<Case> mesCases = plateau.getCaseAvecPieces(getCouleur());

        for (Case depart : mesCases) {
            Piece piece = depart.getPiece();
            if (piece == null) continue;
            ArrayList<Case> destinations = piece.getCasesAccessibles(new ArrayList<>());

            for (Case destination : destinations) {
                Coup coup = new Coup(depart, destination);
                int score = evaluerCoupSimple(coup);

                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleurCoup = coup;
                }
            }
        }
        return meilleurCoup;
    }

    @Override
    protected int evaluerCoupSimple(Coup coup) {
        int score = 0;
        ArrayList<Case> casesPris = new ArrayList<>();
        coup.dep.getPiece().getCasesAccessibles(casesPris);
        score += casesPris.size() * 3;

        int yAvant = coup.dep.getY();
        int yApres = coup.arr.getY();
        if (getCouleur().equals("B") && yApres < yAvant) {
            score += 1;
        } else if (getCouleur().equals("N") && yApres > yAvant) {
            score += 1;
        }

        if ((getCouleur().equals("B") && yApres == 0) ||
                (getCouleur().equals("N") && yApres == 7)) {
            score += 5;
        }
        return score;
    }
}
