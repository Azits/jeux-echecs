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

    public Jeu(String jeu,String typeAdverssaire) {
        plateau = new Plateau();
        joueurs=new Joueur[N_JOUEUR];
        idxJoueurActuel=1;
        piecesPrisesJ1=new ArrayList<>();
        piecesPrisesJ2=new ArrayList<>();

        if(jeu.equals("Echecs")) {
            plateau.placerPieces();
            if(typeAdverssaire.equals("Humain")) {
                joueurs[0]=new JoueurHumain(this,"Azits","N");
                joueurs[1]=new JoueurHumain(this,"Mori","B");
            }
            else {
                joueurs[0]=new JoueurIA(this,"Azits","N");
                joueurs[1]=new JoueurHumain(this,"Mori","B");
            }
        }
        else if(jeu.equals("Dames")) {
            plateau.placerPieceDame();
            if(typeAdverssaire.equals("Humain")) {
                joueurs[0]=new JoueurHumain(this,"Azits","N");
                joueurs[1]=new JoueurHumain(this,"Mori","B");
            }
            else {
                joueurs[0]=new JoueurIA(this,"Azits","N");
                joueurs[1]=new JoueurHumain(this,"Mori","B");
            }

        }

        start();

    }
    public String getCouleurJoueurSuivant() {
        Joueur Joueur = joueurs[ (idxJoueurActuel+1) % N_JOUEUR];
        return Joueur.getCouleur();
    }

    public Plateau getPlateau() {
        return plateau;
    }


    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }

    }

    public abstract Piece choisirPromotion(String couleur);

    public void appliquerLaPromotion(Coup coup) {
        if (coup.arr.getPiece() != null && coup.arr.getPiece() instanceof Pion || coup.arr.getPiece() instanceof PionDame_simple) {
            int y = coup.arr.getY();
            if ((coup.arr.getPiece().getCouleur().equals("B") && y == 0) ||
                    (coup.arr.getPiece().getCouleur().equals("N") && y == 7)) {

                Piece promotion = choisirPromotion(coup.arr.getPiece().getCouleur());
                promotion.allerSurCase(coup.arr);
                coup.arr.setPiece(promotion);
            }
        }
    }

    public void appliquerCoup(Coup coup) {
        ArrayList<Case> caseContenantEnemisPris=new ArrayList<>();
        ArrayList<Case> caseAc=coup.dep.getPiece().getCasesAccessibles(caseContenantEnemisPris);
        for (Case c : caseContenantEnemisPris) {
            ajouterPiecePrise(c.getPiece());
            c.quitterLaCase();
        }
        JouerSon.lectureSon("Son/DeplacementAvecCapture.wav");
        plateau.deplacerPiece(coup.dep,coup.arr);
        appliquerLaPromotion(coup);


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

                if (this.coupValide(coupRecu.dep, coupRecu.arr)) {
                    appliquerCoup(coupRecu);
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



