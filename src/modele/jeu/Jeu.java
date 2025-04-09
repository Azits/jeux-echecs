package modele.jeu;
import java.util.ArrayList;
import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.print.event.PrintJobEvent;

public abstract class Jeu extends Thread{

	public static final int N_JOUEUR=2;
    private Plateau plateau;
    protected Coup coupRecu;
    private Joueur[]joueurs;
    private int idxJoueurActuel;
    private boolean lancer;
    private ArrayList<Piece> piecesPrisesJ1;
    private ArrayList<Piece> piecesPrisesJ2 ;

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();
        joueurs=new Joueur[N_JOUEUR];
        joueurs[0]=new JoueurHumain(this,"Azits","N");
        joueurs[1]=new JoueurHumain(this,"Mori","B");
        idxJoueurActuel=1;
        lancer=true;

        piecesPrisesJ1=new ArrayList<>();
        piecesPrisesJ2=new ArrayList<>();

        start();

    }
    private Joueur getJoueurSuivant() {
        int x = (idxJoueurActuel+1) % N_JOUEUR;
        return joueurs[x];
    }

    public String getCouleurJoueurSuivant() {
        Joueur Joueur = joueurs[ (idxJoueurActuel+1) % N_JOUEUR];
        return Joueur.getCouleur();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void placerPieces() {

        plateau.placerPieces();
    }


    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }
        System.out.println("hello");
    }



    public void appliquerCoup(Coup coup) {
        Case dep = coup.dep;
        Case arr = coup.arr;

        Piece piece = dep.getPiece();
        Piece pieceCapturee = arr.getPiece();

        dep.quitterLaCase();
        arr.setPiece(piece);
        piece.allerSurCase(arr);

        if (enEchec(piece.getCouleur())) {
            System.out.println("Le joueur " + piece.getCouleur() + " s’est mis en échec. Coup refusé.");
            // Revenir à l’état précédent :
            arr.setPiece(pieceCapturee);
            dep.setPiece(piece);
            piece.allerSurCase(dep);
            return;
        }

        // Enregistrer la pièce capturée si coup valide
        if (pieceCapturee != null) {
            ajouterPiecePrise(pieceCapturee);
        }

        // Vérifie maintenant si l'adversaire est mat
        String couleurAdverse = piece.getCouleur().equals("B") ? "N" : "B";
        if (echecEtMat(couleurAdverse)) {
            System.out.println("Échec et mat ! Le joueur " + piece.getCouleur() + " a gagné !");
            lancer = false; // Fin de la partie
        }


    }
    public boolean echecEtMat(String couleurAdverse) {
        if (!enEchec(couleurAdverse)) {
            return false;
        }
        ArrayList<Case> casesAccessibles = plateau.getCaseAvecPieces(couleurAdverse);

        for (Case c : casesAccessibles) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles();

            for (Case d : destinations) {
                Case origine = piece.getCase();
                Piece cible = d.getPiece();

                // Simuler le coup
                origine.quitterLaCase();
                d.setPiece(piece);
                piece.allerSurCase(d);

                boolean echecApresCoup = enEchec(couleurAdverse);

                // Annuler le coup simulé
                d.setPiece(cible);
                origine.setPiece(piece);
                piece.allerSurCase(origine);
                if (cible != null) {
                    cible.allerSurCase(d);
                }

                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean enEchec(String couleur) {
        Case caseRoi = plateau.getCaseRoi(couleur);

        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && !p.getCouleur().equals(couleur)) {
                    ArrayList<Case> attaques = p.getCasesAccessibles();
                    if (attaques.contains(caseRoi)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

    	while(lancer) {

            synchronized (this) {
                try {
                    while (coupRecu == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                appliquerCoup(coupRecu);

                coupRecu = null;

               Joueur joueurActuel=getJoueurSuivant();
                idxJoueurActuel= (idxJoueurActuel+1) % N_JOUEUR;


                synchronized (joueurActuel) {
                    joueurActuel.setMonTour(true);
                    joueurActuel.notify();
                }
            }

        }
    }
    public String getCouleurJoueurActuel() {
    	return joueurs[idxJoueurActuel].getCouleur();
    }
    public void ajouterPiecePrise(Piece piece) {
        if (piece.getCouleur().equals("B")) {
            piecesPrisesJ2.add(piece);
        } else {
            piecesPrisesJ1.add(piece);
        }
    }
    public ArrayList<Piece> getPiecesPrise(int i) {
        if (i==1) {
            return piecesPrisesJ1;
        } else if (i==2) {
        	return piecesPrisesJ2;
        }
        return null;
    }

    public abstract boolean coupValide(Case caseClic1, Case caseClic2);

    public boolean estLancer() {
        return lancer;
    }
}



