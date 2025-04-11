
import VueControleur.VueControleur;

import modele.jeu.Jeu;
import modele.jeu.JeuxEchecs;
import modele.jeu.JoueurHumain;
import modele.jeu.JoueurIA;
import modele.plateau.Plateau;

import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Main {
        public static void main(String[] args) {

            while (true) {
                String jeuChoisi = Menu.choisirJeu();

                if (jeuChoisi.equals("Quitter")) {
                    System.exit(0);
                }

                while (true) {
                    String adversaire = Menu.choisirAdversaire();

                    if (adversaire.equals("Retour")) break;

                    if (jeuChoisi.equals("Echecs")) {
                        Jeu jeu = new JeuxEchecs();

                        if (adversaire.equals("Humain")) {
                            VueControleur vc = new VueControleur(jeu);
                            vc.setVisible(true);
                        } else {
                            /*jeu.setJoueurs(new JoueurHumain(jeu, "Humain", "N"), new JoueurIA(jeu, "Bot", "B"));
                            VueControleur vc = new VueControleur(jeu);
                            vc.setVisible(true)*/
                        }

                        return;
                    }

                    if (jeuChoisi.equals("Dames")) {
                        JOptionPane.showMessageDialog(null, "Mode Dames pas encore implémenté !");
                    }
                }
            }
        }
    }
