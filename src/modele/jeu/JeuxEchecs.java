package modele.jeu;
import modele.plateau.Case;

import java.util.ArrayList;

public class JeuxEchecs extends Jeu{
	public JeuxEchecs() {
        super();
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
        else if (piece instanceof Reine) {
             ArrayList<Case> casesAccessiblesActuelles=piece.getCasesAccessibles();
             if(casesAccessiblesActuelles.contains(caseClic2)){
                 valide = true;
             }
        }

        System.out.println(valide);
        return valide;
    }
}
