package org.tcd.game.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tcd.game.tictactoe.domain.Game;
import org.tcd.game.tictactoe.domain.Level;
import org.tcd.game.tictactoe.domain.Move;
import org.tcd.game.tictactoe.domain.Player;
import org.tcd.game.tictactoe.service.GameService;

@SpringBootApplication
public class Application  {
	
	Logger logger = LoggerFactory.getLogger(Application.class)
	
	@Autowired
	GameService service;
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }
}
