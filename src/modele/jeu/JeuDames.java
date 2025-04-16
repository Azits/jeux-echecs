package modele.jeu;

import modele.plateau.Case;
import modele.plateau.DIirectionEnDiagonale;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class JeuDames extends  Jeu{
    public JeuDames(String jeu,String typeAdverssaire) {
        super(jeu,typeAdverssaire);
    }

    @Override
    public boolean partieGagner() {
        boolean finDeParti = false;
        ArrayList listeJoeur=getPlateau().getCaseAvecPieces(getCouleurJoueurSuivant());
        if(listeJoeur.size()==0){
            finDeParti=true;
        }
        return finDeParti;
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
    public Piece choisirPromotion(String couleur) {
        return new PionDame_dame(getPlateau(), couleur);
    }

    @Override
    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        return false;
    }
}
