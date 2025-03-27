/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;


import modele.jeu.Cavalier;
import modele.jeu.Fou;
import modele.jeu.Piece;
import modele.jeu.Pion;
import modele.jeu.Roi;
import modele.jeu.Tour;
import modele.jeu.Reine;

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
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }

    public void placerPieces() {

    	Roi roiB = new Roi(this);
        Roi roiN = new Roi(this);
        Reine reineB = new Reine(this);
        Reine reineN = new Reine(this);
        
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
            Pion pionB = new Pion(this);
            Pion pionN = new Pion(this);

            pionB.allerSurCase(grilleCases[i][6]); 
            pionN.allerSurCase(grilleCases[i][1]); 

            pionsB.add(pionB);
            pionsN.add(pionN);
        }

        // Placement des cavaliers
        cavaliersB.add(new Cavalier(this));
        cavaliersB.add(new Cavalier(this));
        cavaliersN.add(new Cavalier(this));
        cavaliersN.add(new Cavalier(this));

        cavaliersB.get(0).allerSurCase(grilleCases[1][7]);
        cavaliersB.get(1).allerSurCase(grilleCases[6][7]); 
        cavaliersN.get(0).allerSurCase(grilleCases[1][0]); 
        cavaliersN.get(1).allerSurCase(grilleCases[6][0]); 

        // Placement des fous
        fousB.add(new Fou(this));
        fousB.add(new Fou(this));
        fousN.add(new Fou(this));
        fousN.add(new Fou(this));

        fousB.get(0).allerSurCase(grilleCases[2][7]); 
        fousB.get(1).allerSurCase(grilleCases[5][7]); 
        fousN.get(0).allerSurCase(grilleCases[2][0]);
        fousN.get(1).allerSurCase(grilleCases[5][0]); 

        // Placement des tours
        toursB.add(new Tour(this));
        toursB.add(new Tour(this));
        toursN.add(new Tour(this));
        toursN.add(new Tour(this));

        toursB.get(0).allerSurCase(grilleCases[0][7]);
        toursB.get(1).allerSurCase(grilleCases[7][7]);
        toursN.get(0).allerSurCase(grilleCases[0][0]); 
        toursN.get(1).allerSurCase(grilleCases[7][0]);
        

        setChanged();
        notifyObservers();

    }

    public void arriverCase(Case c, Piece p) {

        c.p = p;

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
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Case caseALaPosition(Point p) {
        Case retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }


}
