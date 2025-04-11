import javax.swing.*;

public class Menu {

    public static String choisirJeu() {
        String[] jeux = {"Échecs", "Dames", "Quitter"};

        int choix = JOptionPane.showOptionDialog(
                null,
                "Quel jeu voulez-vous jouer ?",
                "Menu Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                jeux,
                jeux[0]
        );

        if (choix == 0) return "Echecs";
        if (choix == 1) return "Dames";

        return "Quitter";
    }

    public static String choisirAdversaire() {
        String[] adversaires = {"Humain", "IA", "Retour"};

        int choix = JOptionPane.showOptionDialog(
                null,
                "Voulez-vous jouer contre :",
                "Choix Adversaire",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                adversaires,
                adversaires[0]
        );

        if (choix == 0) return "Humain";
        if (choix == 1) return "IA";

        return "Retour";
    }
}
