package VueControleur;
import java.util.ArrayList;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import modele.jeu.Cavalier;
import modele.jeu.Coup;
import modele.jeu.Fou;
import modele.jeu.Jeu;
import modele.plateau.Case;
import modele.jeu.Piece;
import modele.jeu.Pion;
import modele.jeu.Reine;
import modele.jeu.Roi;
import modele.jeu.Tour;
import modele.plateau.Plateau;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (clic position départ -> position arrivée pièce))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 50; // nombre de pixel par case
    // icones affichées dans la grille
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

    

    private Case caseClic1; // mémorisation des cases cliquées
    private Case caseClic2;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JPanel panelSidebar;
    private JPanel joueur1;
    private JPanel joueur2;
    private JLabel labelTemps;
    
    private ArrayList<Case> casesAccessiblesActuelles;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;
        
        casesAccessiblesActuelles=new ArrayList<>();



        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


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

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        ImageIcon icon = new ImageIcon(urlIcone);

        // Redimensionner l'icône
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        return resizedIcon;
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Jeu d'Échecs");
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
                            if (plateau.getCases()[xx][yy].getPiece() != null &&
                                    plateau.getCases()[xx][yy].getPiece().getCouleur().equals(jeu.getCouleurJoueurActuel())) {

                                caseClic1 = plateau.getCases()[xx][yy];
                                casesAccessiblesActuelles = caseClic1.getPiece().getCasesAccessibles();
                                mettreAJourAffichage();
                            } else {
                                System.out.println("Ce n'est pas à vous de jouer");
                            }
                        } else {
                            caseClic2 = plateau.getCases()[xx][yy];

                            if (!caseClic2.equals(caseClic1)) {
                                if (plateau.contenuDansGrille(new Point(xx,yy)) && jeu.coupValide(caseClic1, caseClic2)) {
                                    jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                                }
                            }

                            // Toujours réinitialiser, même si le coup est invalide
                            caseClic1 = null;
                            caseClic2 = null;
                            casesAccessiblesActuelles.clear();
                            mettreAJourAffichage();
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
                    } else if (piece instanceof Reine) {
                        jlab.setIcon(couleur.equals("B") ? icoRein : icoReinN);
                    } else if (piece instanceof Pion) {
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
        }
        // Affichage du roi en échec
        String couleurJoueur = jeu.getCouleurJoueurActuel();
        if (jeu.estLancer()) {
            if (jeu.enEchec(couleurJoueur)) {
                Case caseRoi = plateau.getCaseRoi(couleurJoueur);
                int x = caseRoi.getX();
                int y = caseRoi.getY();

                tabJLabel[x][y].setBackground(new Color(255, 0, 0, 150)); // rouge translucide
            }
        }

        for (Case caseAcc : casesAccessiblesActuelles) {
            int x = caseAcc.getX();
            int y = caseAcc.getY();

            tabJLabel[x][y].setBackground(new Color(0, 255, 0, 100));
        }
    }
    public void mettreAJourPiecesPrises(ArrayList<Piece> piecesPrisesJ1, ArrayList<Piece> piecesPrisesJ2) {
        joueur1.removeAll();
        joueur2.removeAll();

        ArrayList<Piece> copieJ1 = new ArrayList<>(piecesPrisesJ1);
        ArrayList<Piece> copieJ2 = new ArrayList<>(piecesPrisesJ2);

        for (Piece piece : copieJ1) {
            JLabel lblPiece = getIconeCorrespondante(piece, false);
            joueur1.add(lblPiece);
        }

        for (Piece piece : copieJ2) {
            JLabel lblPiece = getIconeCorrespondante(piece, true);
            joueur2.add(lblPiece);
        }

        joueur1.revalidate();
        joueur1.repaint();
        joueur2.revalidate();
        joueur2.repaint();
    }

    private JLabel getIconeCorrespondante(Piece piece, boolean estBlanc) {
        if (piece instanceof Tour) return new JLabel(estBlanc ? icoTour : icoTourN);
        if (piece instanceof Pion) return new JLabel(estBlanc ? icoPion : icoPionN);
        if (piece instanceof Cavalier) return new JLabel(estBlanc ? icoChevalier : icoChevalierN);
        if (piece instanceof Fou) return new JLabel(estBlanc ? icoFou : icoFouN);
        if (piece instanceof Reine) return new JLabel(estBlanc ? icoRein : icoReinN);
        if (piece instanceof Roi) return new JLabel(estBlanc ? icoRoi : icoRoiN);
        return new JLabel();
    }
    private void gererFinDePartie() {
        JOptionPane.showMessageDialog(this, "Fin de partie ! Le roi a été mis en échec.");
        System.exit(0);
    }
    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
    if (!jeu.estLancer()){
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
