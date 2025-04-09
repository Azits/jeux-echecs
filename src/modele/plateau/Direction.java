/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

/** Type énuméré des directions : les directions correspondent à un ensemble borné de valeurs, connu à l'avance
 *
 *
 */
public enum Direction {
    // Déplacements simples (1 case)
    haut(0, -1),
    bas(0, 1),
    gauche(-1, 0),
    droite(1, 0);


    public final int dx;
    public final int dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
}