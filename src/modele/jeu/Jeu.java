package modele.jeu;
import java.util.ArrayList;
import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.print.event.PrintJobEvent;

public class Jeu extends Thread{
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    protected Coup coupRecu;
    private boolean lancer;
    private Joueur joueurActuel;
    private ArrayList<Piece> piecesPrisesJ1;
    private ArrayList<Piece> piecesPrisesJ2 ;

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this,"Azits","B");
        j2 = new Joueur(this,"AZ","N");
        
        
        joueurActuel=j1;
        
        lancer=true;
        
        piecesPrisesJ1=new ArrayList<>();
        piecesPrisesJ2=new ArrayList<>();
        
        piecesPrisesJ1.add(new Tour(plateau,"N"));
        piecesPrisesJ2.add(new Tour(plateau,"B"));

        start();

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
        plateau.deplacerPiece(coup.dep, coup.arr);
        
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
                
                if (joueurActuel == j1) {
                    joueurActuel = j2;
                } else {
                    joueurActuel = j1;
                }
                
                synchronized (joueurActuel) {
                    joueurActuel.setMonTour(true);
                    joueurActuel.notify();
                }
            }
            synchronized (joueurActuel) {
                while (!joueurActuel.getMonTour()) {
                    try {
                        joueurActuel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public String getCouleurJoueurActuel() {
    	return joueurActuel.getCouleur();
    }
    public void ajouterPiecePrise(Piece piece) {
        if (piece.getCouleur().equals("B")) {
            piecesPrisesJ1.add(piece);
        } else {
            piecesPrisesJ2.add(piece);
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

    }



