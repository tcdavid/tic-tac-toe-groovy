package org.tcd.game.tictactoe.service

import groovy.transform.Memoized
import org.tcd.game.tictactoe.domain.*

public class GameLogic {

    private final Game game

    public GameLogic(Game game) {
        this.game = game
    }

    Move nextMove() {
        new Move(nextPosition(), turn())
    }

    Game addMove(Move move) {

        if (isOpen(move.getPosition())) {
            game.addMove(move)
        }
        // else throw exception?

        if (isDraw()) {
            game.status = Status.DRAW
        } else if (isWinner(Player.X)) {
            game.status = Status.WIN
            game.winner = Player.X
        } else if (isWinner(Player.O)) {
            game.status = Status.WIN
            game.winner = Player.O
        }
        return game
    }

    boolean isDraw() {
        game.getMoves().size() == 9 && !isWinner(Player.X) && !isWinner(Player.O)
    }

    boolean isWinner(Player player) {
        isWinner(game.movesAsMap(), player)
    }

    boolean isInPosition(Player player, int row, int col) {
        player == game.playerAt(row, col)
    }

    Player turn() {
        int count = game.getMoves().size();
        if ((count & 1) == 0) {
            return Player.X;
        }
        return Player.O;
    }

    boolean isOpen(Position position) {
        null == game.playerAt(position)
    }

    List<Position> getOpenPositions() {
        openPositions(game.movesAsMap())
    }

    Player lastTurn() {
        turn() == Player.X ? Player.O : Player.X;
    }

    Position oppositeOpen(Position position1, Position position2) {
        Player player = lastTurn()
        if (isOpen(position1) && player == game.playerAt(position2)) {
            return position1
        }
        return null
    }

    Position sandwichOpen(Position position1, Position position2) {
        Position middle = new Position(2, 2)
        if (isOpen(position1) &&
                (Player.O == game.playerAt(middle)) &&
                (Player.X == game.playerAt(position2))) {

            return position1
        }
        return null
    }

    boolean multipleWinningPositions(Position position) {
        Map<Position, Player> map = game.movesAsMap()
        map.put(position, turn())
        winningPositions(turn(), map).size() > 1
    }

    Position or(Position... positions) {
        positions.find { position -> position != null }
    }

    Position nextPosition() {
        switch (game.getLevel()) {
            case Level.HARD:
                return or(
                        win(),
                        block(),
                        fork(),
                        blockFork(),
                        firstMove(),
                        center(),
                        oppositeCorner(),
                        sandwich(),
                        emptyCorner(),
                        emptySide(),
                        firstOpen())

            case Level.MEDIUM_HARD:
                return or(
                        win(),
                        block(),
                        firstMove(),
                        center(),
                        oppositeCorner(),
                        sandwich(),
                        emptyCorner(),
                        emptySide(),
                        firstOpen())

            case Level.MEDIUM:
                return or(
                        win(),
                        block(),
                        emptyCorner(),
                        emptySide(),
                        firstOpen())

            case Level.EASY:
                return or(
                        emptyCorner(),
                        emptySide(),
                        firstOpen())

            default:
                return firstOpen()
        }
    }

    Position openPosition(int row, int col) {
        Position position = new Position(row, col)
        if (isOpen(position)) {
            return position
        }
        return null
    }

    Position firstOpen() {
        getOpenPositions().first()
    }

    Position win() {
        winningPositions(turn(), game.movesAsMap()).find { true }
    }

    Position block() {
        winningPositions(lastTurn(), game.movesAsMap()).find { true }
    }

    Position fork() {
        getOpenPositions()
                .find { position -> multipleWinningPositions(position) }
    }

    Position blockFork() {
        if (game.getMoves().size() == 3) {
            if (isInPosition(Player.X, 1, 1) && isInPosition(Player.X, 3, 3)) {
                return new Position(1, 2)
            } else if (isInPosition(Player.X, 1, 3) && isInPosition(Player.X, 3, 1)) {
                return new Position(1, 2)
            } else if (isInPosition(Player.X, 3, 2) && isInPosition(Player.X, 2, 3)) {
                return new Position(3, 3)
            }
        }
        return null
    }

    Position firstMove() {
        if (game.getMoves().isEmpty()) {
            return new Position(1, 1)
        }
        return null
    }

    Position oppositeCorner() {
        or(oppositeOpen(new Position(1, 1), new Position(3, 3)),
                oppositeOpen(new Position(3, 3), new Position(1, 1)),
                oppositeOpen(new Position(3, 1), new Position(1, 3)),
                oppositeOpen(new Position(1, 3), new Position(3, 1)))
    }

    Position center() {
        openPosition(2, 2)
    }

    Position sandwich() {
        or(sandwichOpen(new Position(3, 3), new Position(1, 1)),
                sandwichOpen(new Position(1, 1), new Position(3, 3)),
                sandwichOpen(new Position(3, 1), new Position(1, 3)),
                sandwichOpen(new Position(1, 3), new Position(3, 1)))
    }

    Position emptyCorner() {
        or(openPosition(1, 1),
                openPosition(1, 3),
                openPosition(3, 1),
                openPosition(3, 3))
    }

    Position emptySide() {
        or(openPosition(1, 2),
                openPosition(2, 1),
                openPosition(2, 3),
                openPosition(3, 2))
    }

    @Memoized
    List<Position> getAllPositions() {
        List<Position> positions = []
        for (row in 1..3) {
            for (col in 1..3) {
                positions << new Position(row, col)
            }
        }
        return positions
    }

    @Memoized
    static List<List<Position>> getWinningCombos() {
        List<List<Position>> wins = []

        wins << [
                new Position(1, 1),
                new Position(1, 2),
                new Position(1, 3)
        ]
        wins << [
                new Position(2, 1),
                new Position(2, 2),
                new Position(2, 3)
        ]
        wins << [
                new Position(3, 1),
                new Position(3, 2),
                new Position(3, 3)
        ]

        wins << [
                new Position(1, 1),
                new Position(2, 1),
                new Position(3, 1)
        ]
        wins << [
                new Position(1, 2),
                new Position(2, 2),
                new Position(3, 2)
        ]
        wins << [
                new Position(1, 3),
                new Position(2, 3),
                new Position(3, 3)
        ]

        wins << [
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3)
        ]
        wins << [
                new Position(3, 1),
                new Position(2, 2),
                new Position(1, 3)
        ]

        return wins
    }

    static boolean matches(Map<Position, Player> moves, Player player, List<Position> winningCombo) {
        winningCombo.every { position -> moves.get(position) == player }
    }

    static boolean isWinner(Map<Position, Player> moves, Player player) {
        getWinningCombos().any { combo -> matches(moves, player, combo) }
    }

    boolean isWinnerWithMove(Map<Position, Player> moves, Player player, Position position) {
        Map<Position, Player> alteredMoves = new HashMap<Position, Player>(moves)
        alteredMoves.put(position, player)
        getWinningCombos().any { combo -> matches(alteredMoves, player, combo) }
    }

    boolean isOpenInMap(Map<Position, Player> moves, Position position) {
        moves.get(position) == null
    }

    List<Position> openPositions(Map<Position, Player> moves) {
        getAllPositions().findAll { it -> isOpenInMap(moves, it) }
    }

    List<Position> winningPositions(Player player, Map<Position, Player> moves) {
        openPositions(moves)
                .findAll { position -> isWinnerWithMove(moves, player, position) }
    }
}
