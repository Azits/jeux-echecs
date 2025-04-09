package modele.plateau;

import java.util.ArrayList;

import modele.jeu.Piece;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {

    public DecorateurCasesEnLigne(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> cases = new ArrayList<>();
        Case depart = piece.getCase();
        for (Direction d : Direction.values()) {
            Case suivante = plateau.getCaseDansDirection(depart,d.dx,d.dy);
            while (suivante != null) {
                if (!suivante.vide()) {
                    if (!suivante.getPiece().getCouleur().equals(piece.getCouleur())){
                        cases.add(suivante);
                    }
                    break;
                }
                else{
                    cases.add(suivante);
                }
                suivante = plateau.getCaseDansDirection(suivante, d.dx,d.dy);
            }
            }
        return cases ;
        }
    }

