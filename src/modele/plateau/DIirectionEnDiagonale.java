package modele.plateau;

public enum DIirectionEnDiagonale {
    hautGauche(-1, -1),
    hautDroite(1, -1),
    basGauche(-1, 1),
    basDroite(1, 1);
    public final int dx;
    public final int dy;

    private DIirectionEnDiagonale(int dx, int dy) {
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
