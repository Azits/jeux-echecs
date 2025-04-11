package modele.jeu;
import javax.sound.sampled.*;
import java.io.File;

public class JouerSon {
    public static void lectureSon(String cheminSon) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(cheminSon).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Erreur lors de la lecture du son : " + ex.getMessage());
        }
    }
}
