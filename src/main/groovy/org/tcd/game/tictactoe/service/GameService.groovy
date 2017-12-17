package org.tcd.game.tictactoe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.tcd.game.tictactoe.domain.*

import java.util.concurrent.atomic.AtomicLong

@Service
public class GameService {

    private AtomicLong sequence = new AtomicLong(1)

    @Autowired
    GameRepository store

    public GameService() {}

    Game addMove(Game game, Move move) {
        Game modifiedGame = gameLogic(game).addMove(move)
        store.save(modifiedGame)
    }

    Move nextMove(Game game) {
        gameLogic(game).nextMove()
    }

    Game newGame(Level level, Player computerPlaysAs) {
        store.save(new Game(level, computerPlaysAs, sequence.getAndIncrement()))
    }

    List<Game> getGames() {
        store.findAll()
    }

    public Optional<Game> find(String id) {
        Optional.ofNullable(store.findOne(id))
    }

    public void delete(String id) {
        store.delete(id)
    }

    protected GameLogic gameLogic(Game game) {
        new GameLogic(game)
    }

    void deleteAll() {
        store.deleteAll()
    }
}
