package modele.jeu;
import modele.plateau.Case;
import modele.plateau.Plateau;


import javax.swing.*;
import java.util.ArrayList;
/**
 * Classe représentant le jeu d'échecs, hérite de Jeu.
 * Gère les règles spécifiques aux échecs : roque, échec, mat, pat, promotion.
 */
public class JeuxEchecs extends Jeu{
	public JeuxEchecs(String jeu,String typeAdverssaire) {
        super(jeu,typeAdverssaire);
    }

    private Coup dernierCoup;

    public Coup getDernierCoup() {
        return this.dernierCoup;
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
            ArrayList<Case> casesAccessiblesC = caseClone1.getPiece().getCasesAccessibles(new ArrayList<>());
            if (casesAccessiblesC.contains(caseClone2)) {
                clone.deplacerPiece(caseClone1, caseClone2);

                if (!enEchec(getCouleurJoueurActuel(), clone)) {
                    valide = true;
                }
            }
        }

        // Prise en passant
        if (!valide && caseClone1.getPiece() instanceof Pion && caseClone2.vide()) {
            int dx = x2 - x1;
            int dy = y2 - y1;
            if (Math.abs(dx) == 1 && Math.abs(dy) == 1 && dernierCoup != null) {
                Piece pieceDernierCoup = dernierCoup.arr.getPiece();
                if (pieceDernierCoup instanceof Pion &&
                        Math.abs(dernierCoup.dep.getY() - dernierCoup.arr.getY()) == 2 &&
                        dernierCoup.arr.getX() == x2 &&
                        dernierCoup.arr.getY() == y1) {

                    Plateau simulation = getPlateau().clone();
                    Case dep = simulation.getCase(x1, y1);
                    Case arr = simulation.getCase(x2, y2);
                    simulation.deplacerPiece(dep, arr);

                    if (!enEchec(getCouleurJoueurActuel(), simulation)) {
                        return true;
                    }
                }
            }
        }

        if (caseClone1.getPiece() instanceof Roi) {
            int dx = x2 - x1;
            if (Math.abs(dx) == 2 && peutRoquer(clone, caseClone1, caseClone2)) {
                valide = true;
            }
        }
        return valide;
    }

    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        Case caseRoi = plateau.getCaseRoi(couleurJoueur);
        if (caseRoi == null) {
            System.out.println("ATTENTION : Roi " + couleurJoueur + " absent !!");
            return false;
        }
        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && !p.getCouleur().equals(couleurJoueur)) {
                    ArrayList<Case> attaques = p.getCasesAccessibles(new ArrayList<>());
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
            ArrayList<Case> destinations = piece.getCasesAccessibles(null);

            for (Case d : destinations) {
                Piece pieceDepart = c.getPiece();
                Piece pieceArrivee = d.getPiece();

                clone.deplacerPiece(c, d);

                boolean echecApresCoup = enEchec(couleur, clone);

                clone.deplacerPiece(d, c);
                d.setPiece(pieceArrivee);
                c.setPiece(pieceDepart);

                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean echecEtMat(String couleur) {
        if (!enEchec(couleur, getPlateau())) {
            return false;
        }
        Plateau p=getPlateau().clone();
        ArrayList<Case> caseAvecPiceMemeCouleur = p.getCaseAvecPieces(couleur);

        for (Case c :caseAvecPiceMemeCouleur) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles(new ArrayList<>());

            for (Case d : destinations) {
                Piece pieceDepart = c.getPiece();
                Piece pieceArrivee = d.getPiece();

                p.deplacerPiece(c, d);

                boolean echecApresCoup = enEchec(couleur, p);

                p.deplacerPiece(d, c);
                d.setPiece(pieceArrivee);
                c.setPiece(pieceDepart);

                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        System.out.println("Echec Et math");
        return true;
    }

    public boolean peutRoquer(Plateau plateau, Case roiCase, Case destination) {
        Piece roi = roiCase.getPiece();
        if (!(roi instanceof Roi) || ((Roi) roi).aDejaBouge()) return false;

        int y = roiCase.getY();
        int xRoi = roiCase.getX();
        int xDest = destination.getX();
        int direction = (xDest - xRoi > 0) ? 1 : -1;

        // Position de la tour selon le côté
        int xTour = (direction == 1) ? 7 : 0;
        Case caseTour = plateau.getCase(xTour, y);
        Piece tour = caseTour.getPiece();

        // La tour est-elle valide ?
        if (!(tour instanceof Tour) || ((Tour) tour).aDejaBouge()) return false;

        // Cases entre roi et tour doivent être vides
        for (int x = xRoi + direction; x != xTour; x += direction) {
            if (!plateau.getCase(x, y).vide()) return false;
        }

        // Vérifier que le roi ne passe pas par une case attaquée (à faire plus tard)
        return true;
    }
    public void appliquerLeRoque(Coup coup){
        Piece piece=coup.arr.getPiece();
        if (piece instanceof Roi) {
            int dx = coup.arr.getX() - coup.dep.getX();

            if (Math.abs(dx) == 2) {
                int y = coup.dep.getY();
                if (dx > 0) {
                    // Roque Roi
                    getPlateau().deplacerPiece(getPlateau().getCase(7, y), getPlateau().getCase(5, y));
                } else {
                    // Roque Reine
                    getPlateau().deplacerPiece(getPlateau().getCase(0, y), getPlateau().getCase(3, y));
                }
            }
        }

        if (piece instanceof Roi) {
            ((Roi) piece).setDejaBouge(true);
        } else if (piece instanceof Tour) {
            ((Tour) piece).setDejaBouge(true);
        }

    }

    @Override
    public void appliquerCoup(Coup coup) {
        ArrayList l=coup.dep.getPiece().getCasesAccessibles(null);
        Piece piece = coup.dep.getPiece();

        // Prise en passant
        if (piece instanceof Pion && coup.arr.vide()) {
            int dx = coup.arr.getX() - coup.dep.getX();
            int dy = coup.arr.getY() - coup.dep.getY();
            if (Math.abs(dx) == 1 && Math.abs(dy) == 1 && dernierCoup != null) {
                if (dernierCoup.arr.getPiece() instanceof Pion &&
                        Math.abs(dernierCoup.dep.getY() - dernierCoup.arr.getY()) == 2 &&
                        dernierCoup.arr.getX() == coup.arr.getX() &&
                        dernierCoup.arr.getY() == coup.dep.getY()) {

                    Case casePionPris = getPlateau().getCase(dernierCoup.arr.getX(), dernierCoup.arr.getY());
                    ajouterPiecePrise(casePionPris.getPiece());
                    casePionPris.setPiece(null);
                }
            }
        }

        if (coup.arr.getPiece()!=null){
            if(l.contains(coup.arr)){
                ajouterPiecePrise(coup.arr.getPiece());
                getPlateau().deplacerPiece(coup.dep,coup.arr);
                JouerSon.lectureSon("Son/DeplacementAvecCapture.wav");
            }
        }
        else {
            getPlateau().deplacerPiece(coup.dep,coup.arr);
            JouerSon.lectureSon("Son/DeplacementAvecCapture.wav");
        }
        appliquerLaPromotion(coup);
        appliquerLeRoque(coup);
        this.dernierCoup = coup;

    }

    public boolean partieGagner() {
        return echecEtMat(getCouleurJoueurSuivant()) || estPat(getCouleurJoueurSuivant());
    }


    @Override
    public Piece choisirPromotion(String couleur) {
        String[] options = {"Reine", "Tour", "Fou", "Cavalier"};

        int choix = JOptionPane.showOptionDialog(
                null,
                "Promotion du pion " + couleur + " :\nChoisissez une pièce.",
                "Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choix) {
            case 1: return new Tour(getPlateau(), couleur);
            case 2: return new Fou(getPlateau(), couleur);
            case 3: return new Cavalier(getPlateau(), couleur);
            default: return new Reine(getPlateau(), couleur); // défaut : Reine
        }
    }
}
