package modele.plateau;

import java.awt.*;
import java.util.ArrayList;

public class DecorateurPion extends DecorateurCasesAccessibles {
    public DecorateurPion(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> cases = new ArrayList<>();
        Case depart = piece.getCase();
        int direction=piece.getCouleur().equals("B")? -1 : 1;

        Case suivante =plateau.getCase(depart.getX(),depart.getY()+direction);
        if(suivante!=null && suivante.vide()) {
            cases.add(suivante);
            boolean debut=(piece.getCouleur().equals("B") && depart.getY()==6)||(piece.getCouleur().equals("N") && depart.getY()==1);
            Case deuPas=plateau.getCase(depart.getX(),depart.getY()+2*direction);
            if (debut && deuPas!=null && deuPas.vide()) {
                cases.add(deuPas);
            }
        }
        // Capture diagonale gauche
        if (plateau.contenuDansGrille(new Point(depart.getX() - 1,depart.getY() + direction))) {
            Case diaGauche = plateau.getCase(depart.getX() - 1,depart.getY() + direction);
            if (!diaGauche.vide() && !diaGauche.getPiece().getCouleur().equals(piece.getCouleur())) {
                cases.add(diaGauche);
            }
        }

// Capture diagonale droite
        if (plateau.contenuDansGrille(new Point(depart.getX() + 1,depart.getY() + direction))) {
            Case diaDroit = plateau.getCase(depart.getX() + 1,depart.getY() + direction);
            if (!diaDroit.vide() && !diaDroit.getPiece().getCouleur().equals(piece.getCouleur())) {
                cases.add(diaDroit);
            }
        }


        return cases ;
    }
}
