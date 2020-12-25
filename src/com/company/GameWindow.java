package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    private static GameWindow gameWindow;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image zombie;
    private static Image covid;
    private static Image backgroundBlood;
    private static float covid_left=200;
    private static float covid_top = -100;
    private static float covid_v = 200;
    private static int score=0;

    public static void main(String[] args) throws IOException {
        backgroundBlood = ImageIO.read(GameWindow.class.getResourceAsStream("backgroundBlood.png"));
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        covid = ImageIO.read(GameWindow.class.getResourceAsStream("covid.png"));
        zombie = ImageIO.read(GameWindow.class.getResourceAsStream("zombie.png"));
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(120,80);
        gameWindow.setSize(1280,720);
        gameWindow.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        try {
            PlaySound.playSound("/src/com/company/sound/track.wav");
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
            ioException.printStackTrace();
        }
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float covid_right = covid_left+covid.getWidth(null);
                float covid_bottom = covid_top+covid.getHeight(null);
                boolean isCovid= x >= covid_left && x <= covid_right && y >= covid_top && y<= covid_bottom;
                if (isCovid){
                    try {
                        PlaySound.playSound("/src/com/company/sound/bloop.wav");
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                        ioException.printStackTrace();
                    }
                    covid_top = -100;
                    covid_left = (int) (Math.random() * (game_field.getWidth() - covid.getWidth(null)));
                    covid_v+= 10;
                    score++;
                    gameWindow.setTitle("Атак вирусов COVID-19 отражено: " + score);
                }
                if (!isCovid){
                    try {
                        PlaySound.playSound("/src/com/company/sound/bip-bop.wav");
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        gameWindow.add(game_field);
        gameWindow.setVisible(true);
    }

    private static void onRepaint(Graphics graph){
        long current_time = System.nanoTime();
        float delta_time = (current_time-last_frame_time)*0.000000001f;
        last_frame_time = current_time;

        covid_top = covid_top+covid_v * delta_time;
        graph.drawImage(background,0,0,null);
        graph.drawImage(covid,(int) covid_left,(int) covid_top,null);
        if (covid_top> gameWindow.getHeight()) {
            graph.drawImage(backgroundBlood,0,0,null);
            graph.drawImage(zombie,0,0,null);
            graph.drawImage(game_over,0,0,null);

        }
    }
    private static class GameField extends JPanel{
        @Override
        protected void paintComponent (Graphics graph){
            super.paintComponent(graph);
            onRepaint(graph);
            repaint();
        }
    }
}
