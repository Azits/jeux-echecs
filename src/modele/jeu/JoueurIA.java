package modele.jeu;

import java.util.ArrayList;
import java.util.Random;

import modele.plateau.Case;
import modele.plateau.Plateau;

public class JoueurIA extends Joueur{
	public JoueurIA(Jeu _jeu,String nom,String Couleur) {
        super(_jeu,nom,Couleur);
    
	}

	
	public Coup getCoup() {
		 Plateau plateau = getJeu().getPlateau();
		 ArrayList<Case> casesAvecMesPieces = plateau.getCaseAvecPieces(this.getCouleur());
		 ArrayList<Coup> tousLesCoups = new ArrayList<>();	
		 for (Case depart : casesAvecMesPieces) {
	            Piece piece = depart.getPiece();
	            ArrayList<Case> casesAccessibles = piece.getCasesAccessibles(new ArrayList<>());
	            
	            for (Case destination : casesAccessibles) {
	                Coup coup = new Coup(depart, destination);
	                tousLesCoups.add(coup);
	            }
	        }
		 if (!tousLesCoups.isEmpty()) {
	            int index = Tool.randomInt(0, tousLesCoups.size());
	            return tousLesCoups.get(index);
	        }
	       
	        return null;
	    }
	}
