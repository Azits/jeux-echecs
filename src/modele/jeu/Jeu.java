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
    private ArrayList<Piece> piecesPrisesJ1;
    private ArrayList<Piece> piecesPrisesJ2 ;

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();
        joueurs=new Joueur[N_JOUEUR];
        joueurs[0]=new JoueurHumain(this,"Azits","N");
        joueurs[1]=new JoueurHumain(this,"Mori","B");
        idxJoueurActuel=1;


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
            ajouterPiecePrise(coup.arr.getPiece());
            plateau.deplacerPiece(coup.dep,coup.arr);
        }else {
            plateau.deplacerPiece(coup.dep,coup.arr);
        }

    }
    public abstract boolean partieGagner();


    public void run() {
        jouerPartie();
    }
    public void jouerPartie() {

        while (!partieGagner()) {
            synchronized (this) {
                try {
                    while (coupRecu == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (coupValide(coupRecu.dep, coupRecu.arr)) {
                    appliquerCoup(coupRecu);

                    System.out.println("le Joueur " + ((idxJoueurActuel + 1) % N_JOUEUR) + " " + getJoueurSuivant().getCouleur() + " est en echec et math ? " + partieGagner());

                    if (!partieGagner()) {
                        idxJoueurActuel = (idxJoueurActuel + 1) % N_JOUEUR;
                    }

                } else {
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

    public abstract boolean enEchec(String couleurJoueur, Plateau plateau);

}



