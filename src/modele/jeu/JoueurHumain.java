package modele.jeu;

public class JoueurHumain extends Joueur {
	public JoueurHumain(Jeu _jeu,String nom,String Couleur) {
        super(_jeu,nom,Couleur);
    }
	public Coup getCoup() {

        synchronized (getJeu()) {
            try {
                getJeu().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return getJeu().coupRecu;
    }
}
