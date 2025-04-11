package modele.jeu;
import java.util.ArrayList;
import modele.plateau.Case;
import modele.plateau.Plateau;

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

    }



    public void appliquerCoup(Coup coup) {
        if(!coup.arr.vide()){
            plateau.deplacerPiece(coup.dep,coup.arr);
            ajouterPiecePrise(coup.arr.getPiece());
        }else {
            plateau.deplacerPiece(coup.dep,coup.arr);
        }
        System.out.println("hello");

    }
    public boolean echecEtMat(String couleurAdverse) {
        if (!enEchec(couleurAdverse, plateau)) {
            return false;
        }
        Plateau p=plateau.clone();
        ArrayList<Case> caseAvecPiceMemeCouleur = p.getCaseAvecPieces(couleurAdverse);

        for (Case c : caseAvecPiceMemeCouleur) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles();

            for (Case d : destinations) {
                p.deplacerPiece(c,d);
                boolean echecApresCoup = enEchec(couleurAdverse,p);
                if (!echecApresCoup) {
                    return false;
                }
            }
        }
        System.out.println("Echec Et math");
        return true;
    }
    public boolean enEchec(String couleurJoueur, Plateau plateau) {
        Case caseRoi = plateau.getCaseRoi(couleurJoueur);

        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && !p.getCouleur().equals(couleurJoueur)) {
                    ArrayList<Case> attaques = p.getCasesAccessibles();
                    if (attaques.contains(caseRoi)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    /*public boolean estPat(String couleur) {
        if (enEchec(couleur)) return false;

        ArrayList<Case> cases = plateau.getCaseAvecPieces(couleur);
        for (Case c : cases) {
            Piece piece = c.getPiece();
            ArrayList<Case> destinations = piece.getCasesAccessibles();

            for (Case d : destinations) {
                Case origine = piece.getCase();
                Piece cible = d.getPiece();

                // Simuler le coup
                origine.quitterLaCase();
                d.setPiece(piece);
                piece.allerSurCase(d);

                boolean echecApresCoup = enEchec(couleur);

                // Annuler le coup simulé
                d.setPiece(cible);
                origine.setPiece(piece);
                piece.allerSurCase(origine);
                if (cible != null) {
                    cible.allerSurCase(d);
                }

                if (!echecApresCoup) {
                    return false; // Il existe au moins un coup légal
                }
            }
        }

        return true; // Aucun coup légal possible et pas en échec
    }*/


    public void run() {
        jouerPartie();
    }
    public void jouerPartie() {
        while (lancer) {
            synchronized (this) {
                try {
                    while (coupRecu == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (echecEtMat(getCouleurJoueurSuivant())) {

                }
                if (coupValide(coupRecu.dep, coupRecu.arr)) {
                    appliquerCoup(coupRecu);
                    System.out.println(idxJoueurActuel);
                    idxJoueurActuel = (idxJoueurActuel + 1) % N_JOUEUR;
                }else {
                    System.out.println("coup Non valide");
                }

                coupRecu = null;
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



