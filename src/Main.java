
import VueControleur.VueControleur;

import modele.jeu.*;
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
                        Jeu jeu = new JeuxEchecs(jeuChoisi,adversaire);

                        if (adversaire.equals("Humain")) {
                            VueControleur vc = new VueControleur(jeu);
                            vc.setVisible(true);
                        } else {
                            VueControleur vc=new VueControleur(jeu);
                            vc.setVisible(true);
                        }
                        return;
                    }

                    if (jeuChoisi.equals("Dames")) {
                        Jeu jeu=new JeuDames(jeuChoisi,adversaire);

                        if (adversaire.equals("Humain")) {
                            VueControleur vc=new VueControleur(jeu);
                            vc.setVisible(true);
                        }
                        else {
                            VueControleur vc=new VueControleur(jeu);
                            vc.setVisible(true);
                        }
                        return;
                    }
                }
            }
        }
    }
