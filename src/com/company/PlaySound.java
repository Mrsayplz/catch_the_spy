package com.company;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

    public class PlaySound {
        public static void playSound(String soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
            File file =new File("./"+soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }
    }

