package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class JoueurIAEchecs extends JoueurIA{
    public JoueurIAEchecs(Jeu _jeu,String nom,String Couleur){
        super(_jeu,nom,Couleur);
    }
    private Case getMaReineEnDanger(Plateau plateau) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && p.getClass().getSimpleName().equals("Reine") && p.getCouleur().equals(getCouleur())) {
                    ArrayList<Case> adverses = plateau.getCaseAvecPieces(getJeu().getCouleurJoueurSuivant());
                    for (Case adv : adverses) {
                        Piece pa = adv.getPiece();
                        ArrayList<Case> attaques = pa.getCasesAccessibles(null);
                        if (attaques.contains(c)) {
                            return c;
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean coupMetMonRoiEnEchec(Coup coup) {
        Plateau clone = getJeu().getPlateau().clone();

        int xDep = coup.dep.getX();
        int yDep = coup.dep.getY();
        int xArr = coup.arr.getX();
        int yArr = coup.arr.getY();

        Case depClone = clone.getCases()[xDep][yDep];
        Case arrClone = clone.getCases()[xArr][yArr];
        Piece piece = depClone.getPiece();

        if (piece == null) return false;
        clone.deplacerPiece(depClone, arrClone);
        return getJeu().enEchec(getCouleur(), clone);
    }
    private boolean coupMetEnDanger(Coup coup) {
        Plateau clone = getJeu().getPlateau().clone();

        int xDep = coup.dep.getX();
        int yDep = coup.dep.getY();
        int xArr = coup.arr.getX();
        int yArr = coup.arr.getY();

        Case depClone = clone.getCases()[xDep][yDep];
        Case arrClone = clone.getCases()[xArr][yArr];

        Piece piece = depClone.getPiece();
        if (piece == null) return false;

        clone.deplacerPiece(depClone, arrClone);
        ArrayList<Case> casesAdverses = clone.getCaseAvecPieces(getJeu().getCouleurJoueurSuivant());
        for (Case c : casesAdverses) {
            Piece p = c.getPiece();
            if (p != null) {
                ArrayList<Case> attaques = p.getCasesAccessibles(new ArrayList<>());
                if (attaques.contains(arrClone)) {
                    return true;
                }
            }
        }

        return false;
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
                if (coupMetMonRoiEnEchec(coup)) continue;
                int score = evaluerCoupSimple(coup);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleurCoup = coup;
                }
            }
        }
        return meilleurCoup;
    }
    private Case getMenaceurDeCase(Plateau plateau, Case cible) {
        ArrayList<Case> adverses = plateau.getCaseAvecPieces(getJeu().getCouleurJoueurSuivant());
        for (Case c : adverses) {
            Piece p = c.getPiece();
            if (p != null && p.getCasesAccessibles(new ArrayList<>()).contains(cible)) {
                return c;
            }
        }
        return null;
    }
    @Override
    protected int evaluerCoupSimple(Coup coup) {
        int score = 0;
        Piece cible = coup.arr.getPiece();
        if (cible != null) {
            switch (cible.getClass().getSimpleName()) {
                case "Reine": score += 9; break;
                case "Tour": score += 5; break;
                case "Fou": score += 4; break;
                case "Cavalier": score += 3; break;
                case "Pion": score += 1; break;
            }
        }


        int x = coup.arr.getX();
        int y = coup.arr.getY();
        if (x >= 2 && x <= 5 && y >= 2 && y <= 5) score += 1;

        if (coupMetEnDanger(coup)) score -= 3;

        Case caseReineEnDanger = getMaReineEnDanger(getJeu().getPlateau());

        if (caseReineEnDanger != null) {
            if (coup.dep.equals(caseReineEnDanger)) {
                score += 5;
            }
            Case menace = getMenaceurDeCase(getJeu().getPlateau(), caseReineEnDanger);
            if (menace != null && coup.arr.equals(menace)) {
                score += 5;
            }
            if (coup.arr.equals(caseReineEnDanger)) {
                score += 5;
            }
        }
        return score;
    }
}
