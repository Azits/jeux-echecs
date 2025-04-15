package modele.jeu;

import modele.plateau.Case;
import modele.plateau.DIirectionEnDiagonale;
import modele.plateau.Plateau;

import java.awt.*;
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
        if (caseClic1.getPiece() != null) {
            ArrayList<Case> casesAccessiblesC = caseClic1.getPiece().getCasesAccessibles(new ArrayList<>());

            if (casesAccessiblesC.contains(caseClic2)) {
                valide = true;
            }
        }
        return valide;
    }
    @Override
    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        return false;
    }
}
