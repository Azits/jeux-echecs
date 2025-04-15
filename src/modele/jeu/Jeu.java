package modele.jeu;
import java.util.ArrayList;
import modele.plateau.Case;
import modele.plateau.Plateau;
import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthTextAreaUI;


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


    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }

    }

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

    public void appliquerCoup(Coup coup) {
        ArrayList<Case> caseContenantEnemisPris=new ArrayList<>();
        ArrayList<Case> caseAc=coup.dep.getPiece().getCasesAccessibles(caseContenantEnemisPris);
        plateau.deplacerPiece(coup.dep,coup.arr);
        JouerSon.lectureSon("Son/DeplacementAvecCapture.wav");
        for (Case c : caseContenantEnemisPris) {
            ajouterPiecePrise(c.getPiece());
            c.quitterLaCase();
        }
        System.out.println(piecesPrisesJ1);
        System.out.println(piecesPrisesJ2);
        // Promotion

        Piece piece = coup.arr.getPiece();
        if (piece != null && piece instanceof Pion) {
            int y = coup.arr.getY();
            if ((piece.getCouleur().equals("B") && y == 0) ||
                    (piece.getCouleur().equals("N") && y == 7)) {

                Piece promotion = choisirPromotion(piece.getCouleur());
                promotion.allerSurCase(coup.arr);     // 💡 clé
                coup.arr.setPiece(promotion);
            }
        }
            // Le roque
        if (piece instanceof Roi) {
            int dx = coup.arr.getX() - coup.dep.getX();

            if (Math.abs(dx) == 2) {
                int y = coup.dep.getY();
                if (dx > 0) {
                    // Roque Roi
                    plateau.deplacerPiece(plateau.getCase(7, y), plateau.getCase(5, y));
                } else {
                    // Roque Reine
                    plateau.deplacerPiece(plateau.getCase(0, y), plateau.getCase(3, y));
                }
            }
        }

        if (piece instanceof Roi) {
            ((Roi) piece).setDejaBouge(true);
        } else if (piece instanceof Tour) {
            ((Tour) piece).setDejaBouge(true);
        }
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
            System.out.println("jai ajouter blanc capturé ");
            piecesPrisesJ2.add(piece);
        } else {
            System.out.println("jai ajouter Noir capturé ");
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



