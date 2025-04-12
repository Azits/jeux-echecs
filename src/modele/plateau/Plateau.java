/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;


import modele.jeu.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;


    private HashMap<Case, Point> map = new  HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                getMap().put(grilleCases[x][y], new Point(x, y));
            }

        }

    }
    public Plateau clone() {
        Plateau plateauClone = new Plateau();

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case caseOriginale = this.grilleCases[x][y];
                Case caseClone = new Case(plateauClone);

                plateauClone.grilleCases[x][y] = caseClone;
                plateauClone.getMap().put(caseClone, new Point(x, y));  // <<< TRÈS IMPORTANT

                if (!caseOriginale.vide()) {
                    Piece pieceOriginale = caseOriginale.getPiece();
                    Piece pieceClone = pieceOriginale.clone(plateauClone);

                    pieceClone.allerSurCase(caseClone);
                }
            }
        }

        return plateauClone;
    }

    public void placerPieces() {

    	Roi roiB = new Roi(this,"B");
        Roi roiN = new Roi(this,"N");
        Reine reineB = new Reine(this,"B");
        Reine reineN = new Reine(this,"N");
        
        ArrayList<Pion> pionsB = new ArrayList<>();
        ArrayList<Pion> pionsN = new ArrayList<>();
        
        ArrayList<Cavalier> cavaliersB = new ArrayList<>();
        ArrayList<Cavalier> cavaliersN = new ArrayList<>();
        
        ArrayList<Fou> fousB = new ArrayList<>();
        ArrayList<Fou> fousN = new ArrayList<>();
        ArrayList<Tour> toursB = new ArrayList<>();
        ArrayList<Tour> toursN = new ArrayList<>();
        
        roiB.allerSurCase(grilleCases[4][7]); 
        reineB.allerSurCase(grilleCases[3][7]); 
        roiN.allerSurCase(grilleCases[4][0]); 
        reineN.allerSurCase(grilleCases[3][0]); 

        // Placement des pions
        for (int i = 0; i < 8; i++) {
            Pion pionB = new Pion(this,"B");
            Pion pionN = new Pion(this,"N");

            pionB.allerSurCase(grilleCases[i][6]); 
            pionN.allerSurCase(grilleCases[i][1]); 

            pionsB.add(pionB);
            pionsN.add(pionN);
        }

        // Placement des cavaliers
        cavaliersB.add(new Cavalier(this,"B"));
        cavaliersB.add(new Cavalier(this,"B"));
        cavaliersN.add(new Cavalier(this,"N"));
        cavaliersN.add(new Cavalier(this,"N"));

        cavaliersB.get(0).allerSurCase(grilleCases[1][7]);
        cavaliersB.get(1).allerSurCase(grilleCases[6][7]); 
        cavaliersN.get(0).allerSurCase(grilleCases[1][0]); 
        cavaliersN.get(1).allerSurCase(grilleCases[6][0]); 

        // Placement des fous
        fousB.add(new Fou(this,"B"));
        fousB.add(new Fou(this,"B"));
        fousN.add(new Fou(this,"N"));
        fousN.add(new Fou(this,"N"));

        fousB.get(0).allerSurCase(grilleCases[2][7]); 
        fousB.get(1).allerSurCase(grilleCases[5][7]); 
        fousN.get(0).allerSurCase(grilleCases[2][0]);
        fousN.get(1).allerSurCase(grilleCases[5][0]); 

        // Placement des tours
        toursB.add(new Tour(this,"B"));
        toursB.add(new Tour(this,"B"));
        toursN.add(new Tour(this,"N"));
        toursN.add(new Tour(this,"N"));

        toursB.get(0).allerSurCase(grilleCases[0][7]);
        toursB.get(1).allerSurCase(grilleCases[7][7]);
        toursN.get(0).allerSurCase(grilleCases[0][0]); 
        toursN.get(1).allerSurCase(grilleCases[7][0]);
        

        setChanged();
        notifyObservers();

    }
    public void  placerPieceDame(){
        ArrayList<PionDame_simple> pionsB = new ArrayList<>();
        ArrayList<PionDame_simple> pionsN = new ArrayList<>();

        // PIONS NOIRS (en haut)
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                if ((x + y) % 2 == 0) {
                    PionDame_simple pionN = new PionDame_simple(this, "N");
                    pionN.allerSurCase(grilleCases[x][y]);
                }
            }
        }

        // PIONS BLANCS (en bas)
        for (int y = 5; y <= 7; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                if ((x + y) % 2 != 0) {
                    PionDame_simple pionB = new PionDame_simple(this, "B");
                    pionB.allerSurCase(grilleCases[x][y]);
                }
            }
        }

    }

    public void arriverCase(Case c, Piece p) {

        c.p = p;

    }
    public boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    public void deplacerPiece(Case c1, Case c2) {
        if (c1.p != null) {
            c1.p.allerSurCase(c2);

        }
        setChanged();
        notifyObservers();

    }


    /** Indique si p est contenu dans la grille
     */

  

    public ArrayList<Case> getCaseAvecPieces(String couleur) {
        ArrayList<Case> casesAvecPieces = new ArrayList<>();

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case c = grilleCases[x][y];
                Piece piece = c.getPiece();

                if (piece != null && piece.getCouleur().equals(couleur)) {
                    casesAvecPieces.add(c);
                }
            }
        }

        return casesAvecPieces;
    }
    public  Case getCase(int x, int y){
        return grilleCases[x][y];
    }
    public Case getCaseRoi(String couleur) {
        for(int x=0;x<SIZE_X;x++){
            for(int y=0;y<SIZE_Y;y++){
                Case c = grilleCases[x][y];
                Piece piece = c.getPiece();
                if (piece instanceof Roi && piece.getCouleur().equals(couleur)) {
                    return c;
                }
            }
        }
        return null;
    }
    public Case getCaseDansDirection(Case c, int dx,int dy) {
        int x = c.getX() + dx;
        int y = c.getY() + dy;
        Point p=new Point(x, y);

        if (contenuDansGrille(p)) {
            return getCases()[x][y];
        } else {
            return null;
        }
    }

	public HashMap<Case, Point> getMap() {
		return map;
	}




}
