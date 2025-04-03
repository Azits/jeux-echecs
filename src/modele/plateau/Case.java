/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.awt.Point;

import modele.jeu.Piece;

public class Case {

    protected Piece p;
    protected Plateau plateau;



    public void quitterLaCase() {
        p = null;
    }



    public Case(Plateau _plateau) {

        plateau = _plateau;
        p=null;
    }

    public Piece getPiece() {
        return p;
    }
    public boolean vide() {
    	if(p==null) {
    		return true;
    	}
    	return false;
    }
/*
    public void setEntite(Piece _e) {

        p = _e;

    }*/

    public int getX() {
        Point position = plateau.getMap().get(this); 
        if (position != null) {
            return position.x;
        } else {
            return -1;
        }
    }
    public int getY() {
        Point position = plateau.getMap().get(this); 
        if (position != null) {
            return position.y;
        } else {
            return -1;
        }
    }
   }
