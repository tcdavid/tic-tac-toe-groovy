package org.tcd.game.tictactoe.domain;

import org.springframework.data.annotation.Id;
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Game {

    @Id
    String id

    List<Move> moves
    Level level
    Player computerPlaysAs
    Status status
    Player winner
    Long sequence

    Game() {}

    Game(String id, List<Move> moves, Level level, Player computerPlaysAs, Long sequence) {
        this.moves = moves;
        this.level = level;
        this.computerPlaysAs = computerPlaysAs;
        this.id = id;
        this.status = Status.OPEN;
        this.sequence = sequence;
    }

    Game( Level level, Player computerPlaysAs, Long sequence) {
        this(null, new ArrayList<Move>(), level, computerPlaysAs, sequence)
    }

    void addMove(Move move){
        moves.add(move);
    }

    Player playerAt(int row, int col) {
        playerAt(new Position(row, col))
    }

    Player playerAt(Position position) {
        movesAsMap().get(position)
    }

    Map<Position, Player> movesAsMap() {
        Map<Position, Player> map = new HashMap<Position, Player>()
        moves.each() {
            map.put(it.position, it.player)
        }
        return map
    }
}