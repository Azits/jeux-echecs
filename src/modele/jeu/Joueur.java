package modele.jeu;

public class Joueur {
    private Jeu jeu;
    private boolean monTour;
    private String nomJoueur;
    private String couleur;

    public Joueur(Jeu _jeu,String nom,String Couleur) {
        jeu = _jeu;
        this.monTour=false;
        this.couleur=Couleur;
        this.nomJoueur=nom;
    }

    public Coup getCoup() {

        synchronized (jeu) {
            try {
                jeu.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return jeu.coupRecu;
    }
    public boolean getMonTour() {
        return this.monTour;
    }
    public void setMonTour(boolean Tour) {
        this.monTour =Tour;
    }
    public String getCouleur() {
        return this.couleur;
    }
}
