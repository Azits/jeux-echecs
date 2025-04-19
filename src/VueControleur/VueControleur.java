package VueControleur;
import java.util.ArrayList;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import modele.jeu.*;
import modele.plateau.Case;
import modele.plateau.Plateau;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (clic position départ -> position arrivée pièce))
 *
 */
public class VueControleur extends JFrame implements Observer {
    /** Plateau de jeu utilisé pour l'affichage et la détection des clics. */
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    /** Référence au jeu (modèle). */
    private Jeu jeu;
    /** Dimensions de la grille. */
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    /** Nombre de pixels par case. */
    private static final int pxCase = 50;
    /** icones affichées dans la grille*/
    private ImageIcon icoRoi;
    private ImageIcon icoRein;
    private ImageIcon icoPion;
    private ImageIcon icoFou;
    private ImageIcon icoChevalier;
    private ImageIcon icoTour;
 
    private ImageIcon icoRoiN;
    private ImageIcon icoReinN;
    private ImageIcon icoPionN;
    private ImageIcon icoFouN;
    private ImageIcon icoChevalierN;
    private ImageIcon icoTourN;


    /** Cases sélectionnées par l'utilisateur. */
    private Case caseClic1; // mémorisation des cases cliquées
    private Case caseClic2;

    /**cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)*/
    private JLabel[][] tabJLabel;
    /** Panneaux latéraux pour les joueurs et informations. */
    private JPanel panelSidebar;
    private JPanel joueur1;
    private JPanel joueur2;
    private JLabel labelTemps;
    /** Liste des cases accessibles pour une pièce sélectionnée. */
    private ArrayList<Case> casesAccessiblesActuelles;
    /** Paneau pour montrer la fin du jeu */
    private JLabel labelFinPartie;

    /**
     * Constructeur principal. Initialise les composants graphiques, les icônes et les observateurs.
     * @param _jeu Le jeu auquel cette vue/contrôleur est liée.
     */
    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;
        
        casesAccessiblesActuelles=new ArrayList<>();
        chargerLesIcones();
        placerLesComposantsGraphiques(jeu.getClass().getSimpleName());

        plateau.addObserver(this);

        mettreAJourAffichage();

    }

    /**
     * Charge les icônes à utiliser pour l'affichage des pièces sur le plateau.
     */
    private void chargerLesIcones() {
        icoRoi = chargerIcone("Images/wK.png");
        icoRein=chargerIcone("Images/wQ.png");
        icoPion=chargerIcone("Images/wP.png");
        icoFou=chargerIcone("Images/wB.png");
        icoChevalier=chargerIcone("Images/wN.png");
        icoTour=chargerIcone("Images/wR.png");
     
        icoPionN=chargerIcone("Images/bP.png");
        icoRoiN = chargerIcone("Images/bK.png");
        icoReinN=chargerIcone("Images/bQ.png");
        icoFouN=chargerIcone("Images/bB.png");
        icoChevalierN=chargerIcone("Images/bN.png");
        icoTourN=chargerIcone("Images/bR.png");
       


    }
    /**
     * Charge et redimensionne une icône à partir de son chemin.
     * @param urlIcone chemin relatif vers l'image.
     * @return l'icône redimensionnée.
     */
    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        ImageIcon icon = new ImageIcon(urlIcone);

        // Redimensionner l'icône
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        return resizedIcon;
    }
    /**
     * Place tous les composants graphiques de la fenêtre.
     */
    private void placerLesComposantsGraphiques(String typeJeu) {
        if(typeJeu.equals("JeuxEchecs")) {
            setTitle("Jeu d'Échecs");
        }
        else {
            setTitle("Jeu Dames");
        }

        setResizable(false);
        int largeurFenetre=(sizeX * pxCase)+300;
        setSize(largeurFenetre,(sizeX * pxCase));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille


        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (caseClic1 == null) {

                            if (plateau.getCases()[xx][yy].getPiece() != null && plateau.getCases()[xx][yy].getPiece().getCouleur().equals(jeu.getCouleurJoueurActuel())) {
                                caseClic1 = plateau.getCases()[xx][yy];
                                casesAccessiblesActuelles = caseClic1.getPiece().getCasesAccessibles(new ArrayList<>());
                                mettreAJourAffichage();
                            } else {
                                System.out.println("Ce n'est pas à vous de jouer");
                            }
                        } else {
                            caseClic2 = plateau.getCases()[xx][yy];
                            if (plateau.contenuDansGrille(new Point(xx, yy))) {
                                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                            }
                            // Toujours réinitialiser, même si le coup est invalide
                            caseClic1 = null;
                            caseClic2 = null;
                            casesAccessiblesActuelles.clear();
                        }
                    }
                });


                jlab.setOpaque(true);

                if ((y%2 == 0 && x%2 == 0) || (y%2 != 0 && x%2 != 0)) {
                    tabJLabel[x][y].setBackground(new Color(50, 50, 110));
                } else {
                    tabJLabel[x][y].setBackground(new Color(150, 150, 210));
                }

                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels,BorderLayout.CENTER);
        
        panelSidebar=new JPanel();
        joueur1=new JPanel();
        joueur2=new JPanel();
        
        panelSidebar.setLayout(new BorderLayout());
        panelSidebar.setBackground(new Color(150, 150, 210));
        panelSidebar.setPreferredSize(new Dimension(largeurFenetre-(sizeX * pxCase),( sizeX * pxCase)));
        
        joueur1.setLayout(new FlowLayout());
        joueur1.setBackground(new Color(50, 50, 110));
        panelSidebar.add(joueur1, BorderLayout.SOUTH);
        
        joueur2.setLayout(new FlowLayout());
        joueur2.setBackground(new Color(50, 50, 110));
        panelSidebar.add(joueur2, BorderLayout.NORTH);
        
  
        add(panelSidebar,BorderLayout.EAST);

        labelFinPartie = new JLabel("");
        labelFinPartie.setFont(new Font("Arial", Font.BOLD, 16));
        labelFinPartie.setHorizontalAlignment(SwingConstants.CENTER);
        labelFinPartie.setForeground(Color.RED);

        panelSidebar.add(labelFinPartie, BorderLayout.CENTER);

    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        // Met à jour l'affichage des pièces capturées
        mettreAJourPiecesPrises(jeu.getPiecesPrise(1), jeu.getPiecesPrise(2));

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = plateau.getCases()[x][y];
                JLabel jlab = tabJLabel[x][y];

                // Réinitialiser les couleurs de fond (couleur damier)
                if ((y % 2 == 0 && x % 2 == 0) || (y % 2 != 0 && x % 2 != 0)) {
                    jlab.setBackground(new Color(50, 50, 110));
                } else {
                    jlab.setBackground(new Color(150, 150, 210));
                }

                //  Réinitialiser les icônes
                if (c != null && c.getPiece() != null) {
                    Piece piece = c.getPiece();
                    String couleur = piece.getCouleur();

                    if (piece instanceof Roi) {
                        jlab.setIcon(couleur.equals("B") ? icoRoi : icoRoiN);
                    } else if (piece instanceof Reine || piece instanceof  PionDame_dame) {
                        jlab.setIcon(couleur.equals("B") ? icoRein : icoReinN);
                    } else if (piece instanceof Pion || piece instanceof PionDame_simple) {
                        jlab.setIcon(couleur.equals("B") ? icoPion : icoPionN);
                    } else if (piece instanceof Cavalier) {
                        jlab.setIcon(couleur.equals("B") ? icoChevalier : icoChevalierN);
                    } else if (piece instanceof Fou) {
                        jlab.setIcon(couleur.equals("B") ? icoFou : icoFouN);
                    } else if (piece instanceof Tour) {
                        jlab.setIcon(couleur.equals("B") ? icoTour : icoTourN);
                    }
                } else {
                    jlab.setIcon(null); // pas de pièce
                }
            }
        } // Affichage du roi en échec
        String couleurJoueur = jeu.getCouleurJoueurActuel();
        if (!jeu.partieGagner()) {
            if (jeu.enEchec(couleurJoueur,plateau)){
                Case caseRoi = plateau.getCaseRoi(couleurJoueur);
                int x = caseRoi.getX();
                int y = caseRoi.getY();

                tabJLabel[x][y].setBackground(new Color(255, 0, 0, 150));
            }
        }

        for (Case caseAcc : casesAccessiblesActuelles) {
            int x = caseAcc.getX();
            int y = caseAcc.getY();

            tabJLabel[x][y].setBackground(new Color(0, 255, 0, 100));
        }
    }
    /**
     * Met à jour l'affichage des pièces capturées.
     * @param piecesPrisesJ1 Pièces capturées par le joueur 1.
     * @param piecesPrisesJ2 Pièces capturées par le joueur 2.
     */
    public void mettreAJourPiecesPrises(ArrayList<Piece> piecesPrisesJ1, ArrayList<Piece> piecesPrisesJ2) {
        joueur1.removeAll();
        joueur2.removeAll();
        for (Piece piece : piecesPrisesJ1) {
            JLabel lblPiece = getIconeCorrespondante(piece, false);
            joueur1.add(lblPiece);
        }

        for (Piece piece : piecesPrisesJ2) {
            JLabel lblPiece = getIconeCorrespondante(piece, true);
            joueur2.add(lblPiece);
        }
        joueur1.revalidate();
        joueur1.repaint();
        joueur2.revalidate();
        joueur2.repaint();
    }
    /**
     * Retourne un JLabel contenant l'icône correspondant au type de pièce passé en paramètre.
     * <p>
     * Cette méthode prend en compte si la pièce est blanche ou noire, et sélectionne l'icône appropriée.
     * Elle gère aussi bien les pièces d’échecs classiques que les variantes du jeu de dames (pion simple ou dame).
     * </p>
     *
     * @param piece La pièce dont on souhaite obtenir l'icône.
     * @param estBlanc Vrai si la pièce est blanche, faux si elle est noire.
     * @return Un JLabel contenant l'icône associée à la pièce.
     */
    private JLabel getIconeCorrespondante(Piece piece, boolean estBlanc) {
        if (piece instanceof Tour) return new JLabel(estBlanc ? icoTour : icoTourN);
        if (piece instanceof Pion || piece instanceof PionDame_simple) return new JLabel(estBlanc ? icoPion : icoPionN);
        if (piece instanceof Cavalier) return new JLabel(estBlanc ? icoChevalier : icoChevalierN);
        if (piece instanceof Fou) return new JLabel(estBlanc ? icoFou : icoFouN);
        if (piece instanceof Reine || piece instanceof PionDame_dame) return new JLabel(estBlanc ? icoRein : icoReinN);
        if (piece instanceof Roi) return new JLabel(estBlanc ? icoRoi : icoRoiN);
        return new JLabel();

    }
    /**
     * Gère la fin de la partie en affichant une boîte de dialogue avec le nom du gagnant,
     * puis ferme proprement l'application.
     * <p>
     * Le gagnant est déterminé en fonction de la couleur du joueur actif au moment de la fin.
     * </p>
     */
    private void gererFinDePartie() {
        String gagant= jeu.getCouleurJoueurActuel().equals("B")?"Blanc":"Noir";
        JOptionPane.showMessageDialog(this, "Fin de partie ! Le gagant est le jour "+gagant);
        System.exit(0);
    }
    /**
     * Appelé automatiquement lors d'un changement dans le modèle observé.
     * @param o Objet observable.
     * @param arg Argument de mise à jour (non utilisé ici).
     */
    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        if (jeu.partieGagner()){
            gererFinDePartie();
        }
        
        /*

        // récupérer le processus graphique pour rafraichir
        // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur, utiliser un autre processus, voir classe Executor)


        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
