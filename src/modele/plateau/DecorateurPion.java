package modele.plateau;

import java.awt.*;
import java.util.ArrayList;

public class DecorateurPion extends DecorateurCasesAccessibles {
    public DecorateurPion(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }
    @Override
    public ArrayList<Case> getMesCasesAccessibles(ArrayList<Case> casesEnemieCapture) {
        ArrayList<Case> res = new ArrayList<>();
        Case depart=piece.getCase();

        if (depart == null) return res;  // sécurité absolue

        int x = depart.getX();
        int y = depart.getY();

        int direction = (piece.getCouleur().equals("B")) ? -1 : 1;

        // Vérifier que la case devant est dans la grille
        if (plateau.contenuDansGrille(new Point(x, y + direction))) {
            Case avant = plateau.getCase(x, y + direction);
            if (avant.vide()) {
                res.add(avant);
            }
            // Avancer de 2 cases uniquement si départ (1 pour noir, 6 pour blanc)
            boolean surLigneDepart = (piece.getCouleur().equals("B") && y == 6) || (piece.getCouleur().equals("N") && y == 1);

            if (surLigneDepart && plateau.contenuDansGrille(new Point(x, y + 2 * direction)) && avant.vide()) {
                Case deuxAvant = plateau.getCase(x, y + 2 * direction);
                if (deuxAvant.vide()) {
                    res.add(deuxAvant);
                }
            }
        }

        // Vérifier capture diagonale gauche
        if (plateau.contenuDansGrille(new Point(x - 1, y + direction))) {
            Case diagGauche = plateau.getCase(x - 1, y + direction);
            if (!diagGauche.vide() && !diagGauche.getPiece().getCouleur().equals(piece.getCouleur())) {
                res.add(diagGauche);
            }
        }

        // Vérifier capture diagonale droite
        if (plateau.contenuDansGrille(new Point(x + 1, y + direction))) {
            Case diagDroite = plateau.getCase(x + 1, y + direction);
            if (!diagDroite.vide() && !diagDroite.getPiece().getCouleur().equals(piece.getCouleur())) {
                res.add(diagDroite);
            }
        }

        return res;
    }

}
