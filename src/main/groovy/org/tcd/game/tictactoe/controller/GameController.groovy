package org.tcd.game.tictactoe.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.tcd.game.tictactoe.domain.Game
import org.tcd.game.tictactoe.domain.Level
import org.tcd.game.tictactoe.domain.Move
import org.tcd.game.tictactoe.domain.Player
import org.tcd.game.tictactoe.service.GameService

@RestController
@RequestMapping("/api/games")
class GameController {

    Logger logger = LoggerFactory.getLogger(GameController.class)

    @Autowired
    GameService service

    GameController() {
        logger.debug("created");
    }

    // TODO -  take data in RequestBody of "Options"
    @RequestMapping(value = "", method = RequestMethod.POST)
    Game newGame(@RequestParam(value = "level", defaultValue = "HARD") Level level,
                 @RequestParam(value = "computerPlaysAs", defaultValue = "O") Player computerPlaysAs) {

        logger.debug("newGame: level=" + level + " player=" + computerPlaysAs);
        return service.newGame(level, computerPlaysAs)
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<Game> readGames() {
        logger.debug("games")
        return service.getGames()
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Game readGame(@PathVariable String id) {
        logger.debug("games")
        return service.find(id).get()
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    void deleteGame(@PathVariable String id) {
        logger.debug("delete " + id)
        service.delete(id)
    }

    @RequestMapping(value = "/{id}/turn ", method = RequestMethod.PUT)
    Game turn(@PathVariable String id, @RequestBody Move move) {

        validateGame(id)

        logger.debug("turn - " + move)

        Optional<Game> game = service.find(id)

        // TODO - check for open game, valid move

        return service.addMove(game.get(), move)
    }

    @RequestMapping(value = "/{id}/autoturn ", method = RequestMethod.PUT)
    Move autoTurn(@PathVariable String id) {

        validateGame(id)

        logger.debug("autoturn")

        Optional<Game> game = service.find(id)

        // TODO - check for open game

        Move move = service.nextMove(game.get())

        logger.debug("autoturn returning " + move)

        service.addMove(game.get(), move)

        return move
    }

    // TODO - overload with Game object
    private void validateGame(String id) {
        Optional<Game> game = this.service.find(id);
        if (!game.isPresent()) {
            throw new GameNotFoundException(id)
        }
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L

    public GameNotFoundException(String id) {
        super("could not find game '" + id + "'.")
    }
}
