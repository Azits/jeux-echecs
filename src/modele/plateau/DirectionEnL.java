package modele.plateau;

public enum DirectionEnL {

    l1(2, 1),
    l2(1, 2),
    l3(-1, 2),
    l4(-2, 1),
    l5(-2, -1),
    l6(-1, -2),
    l7(1, -2),
    l8(2, -1);

    public final int dx;
    public final int dy;

    private DirectionEnL(int dx, int dy) {
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
