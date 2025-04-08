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
    	Case c=coup.arr;
    	if(c.vide()) {
    		plateau.deplacerPiece(coup.dep, coup.arr);
    	}
    	else {
    		Piece prise=c.getPiece();
    		ajouterPiecePrise(prise);
    		plateau.deplacerPiece(coup.dep, coup.arr);
    		
    	}
        
        
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

    public boolean coupValide(Case caseClic1, Case caseClic2) {
        boolean valide = false;
        Piece piece = caseClic1.getPiece();
        int x = caseClic1.getX();
        int y = caseClic1.getY();

        int xArrivee = caseClic2.getX();
        int yArrivee = caseClic2.getY();

        int dx = xArrivee - x;
        int dy = yArrivee - y;

        String couleurPiece = piece.getCouleur();

        if (piece instanceof Pion) {
            int direction = couleurPiece.equals("B") ? -1 : 1;

            Piece pieceEnFace = caseClic2.getPiece();

            // Avancer tout droit
            if (dx == 0 && dy == direction) {
                valide = true;
            }
            // Double pas depuis la ligne de départ
            else if (dx == 0 && dy == 2 * direction) {
                if ((couleurPiece.equals("B") && y == 6) || (couleurPiece.equals("N") && y == 1)) {
                    valide = true;

                }
            }
            // Capture diagonale
            else if (Math.abs(dx) == 1 && dy == direction && pieceEnFace != null) {
                if (!pieceEnFace.getCouleur().equals(couleurPiece)) {
                    valide = true;
                }
            }
        }
        System.out.println(valide);
        return valide;
    }
}



