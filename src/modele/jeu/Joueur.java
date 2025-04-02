package modele.jeu;

public abstract class Joueur {
    private Jeu jeu;
    private boolean monTour;
    private String nomJoueur;
    private String couleur;

    public Joueur(Jeu _jeu,String nom,String Couleur) {
        setJeu(_jeu);
        this.monTour=false;
        this.couleur=Couleur;
        this.nomJoueur=nom;
    }
    

    protected  abstract Coup getCoup();
    public boolean getMonTour() {
        return this.monTour;
    }
    public void setMonTour(boolean Tour) {
        this.monTour =Tour;
    }
    public String getCouleur() {
        return this.couleur;
    }


	public Jeu getJeu() {
		return jeu;
	}


	public void setJeu(Jeu jeu) {
		this.jeu = jeu;
	}
}
