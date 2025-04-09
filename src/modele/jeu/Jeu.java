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
        idxJoueurActuel = (idxJoueurActuel+1) % N_JOUEUR;
        return joueurs[idxJoueurActuel];
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
        Piece pieceCapturee = arr.getPiece(); // sauvegarde de la pièce capturée

        // Déplacement temporaire
        dep.quitterLaCase();
        arr.setPiece(piece);
        piece.allerSurCase(arr); // met à jour la case de la pièce

        // Vérifie si le roi du joueur est en échec après ce coup
        if (enEchec(piece.getCouleur())) {
            System.out.println("Le joueur " + piece.getCouleur() + " a mis son propre roi en échec. Il perd !");
            lancer = false;  // fin de partie
            return;
        }

        // Coup valide : on garde l'état tel quel
        if (pieceCapturee != null) {
            ajouterPiecePrise(pieceCapturee);
        }


    }

    private boolean enEchec(String couleur) {
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



