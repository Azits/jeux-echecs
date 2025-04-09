package modele.jeu;
import modele.plateau.Case;

import java.util.ArrayList;

public class JeuxEchecs extends Jeu{
	public JeuxEchecs() {
        super();
    }
    public boolean coupValide(Case caseClic1, Case caseClic2) {
        boolean valide = false;
        ArrayList<Case>caseAccecible=caseClic1.getPiece().getCasesAccessibles();
        if (caseAccecible.contains(caseClic2)) {
            valide=true;
        }
        return valide;
    }
}
