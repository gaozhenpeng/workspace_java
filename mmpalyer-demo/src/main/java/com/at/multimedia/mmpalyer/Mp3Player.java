package com.at.multimedia.mmpalyer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class Mp3Player {
 
    private String filename;
    private Player player;
    
    public void play() {
        try {
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(filename));
            player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            log.warn("Not able to play file '{}'.", filename, e);
        }
 
    }
 
    public static void main(String[] args) {
        Mp3Player mp3player = new Mp3Player();
        if(args.length < 1) {
            log.error("Usage: {} <path/to/mp3>", mp3player.getClass().getSimpleName());
            return;
        }
        mp3player.setFilename(args[0]);
        mp3player.play();
    }
}
