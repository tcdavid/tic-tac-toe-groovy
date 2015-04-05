package org.tcd.game.tictactoe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.tcd.game.tictactoe.domain.Game;
import org.tcd.game.tictactoe.domain.Move;
import org.tcd.game.tictactoe.domain.Level;
import org.tcd.game.tictactoe.domain.Player;
import org.tcd.game.tictactoe.domain.Position;
import org.tcd.game.tictactoe.domain.Status;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class GameLogic  {

	private final Game game
	
	public GameLogic(Game game) {
		this.game = game
	}
	
	public Move nextMove() {
		new Move(nextPosition(), turn());
	}

	public Game addMove(Move move) {
		
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
		return (player == game.playerAt(row, col));
	}
	
	Player turn() {
		int count = game.getMoves().size();
		if ( (count & 1) == 0 ) {
			return Player.X;
		}
		return Player.O;
	}

	boolean isOpen(Position position) {
		return (null == game.playerAt(position));
	}
	
	List<Position> getOpenPositions() {
		return openPositions(game.movesAsMap());
	}
	
	Player lastTurn() {
		return turn()==Player.X? Player.O: Player.X;
	}
	
	Position oppositeOpen(Position position1, Position position2) {
		Player player = lastTurn();
		if (isOpen(position1) && player == game.playerAt(position2)) {
			return position1;
		}
		return null;
	}
	
	Position sandwichOpen(Position position1, Position position2 ) {
		Position middle = new Position(2,2);
		if (isOpen(position1) && 
				(Player.O == game.playerAt(middle)) &&
				(Player.X == game.playerAt(position2))) {
			
			return position1;
		}
		return null;
	}
	
	boolean multipleWinningPositions(Position position) {
		Map<Position, Player> map = game.movesAsMap();
		map.put(position, turn());
		return (winningPositions(turn(), map).size() > 1);
	}
	
	Position or(Position ...positions){
		return positions.find { position -> position != null}
	}
	
	Position nextPosition() {
		switch (game.getLevel()) {
			case Level.HARD:
				return or (
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
	                    firstOpen());
				
			case Level.MEDIUM_HARD:
				return or ( 
						win(),
						block(),
						firstMove(),
						center(),
						oppositeCorner(),
						sandwich(),
						emptyCorner(),
						emptySide(),
						firstOpen());
				
			case Level.MEDIUM:
				return or ( 
						win(),
						block(),							
						emptyCorner(),
						emptySide(),
						firstOpen());
				
			case Level.EASY:
				return or (
						emptyCorner(),
						emptySide(),
						firstOpen());
				
			default :
				return firstOpen();
			}

	}
		
	Position openPosition(int row, int col) {
		Position position = new Position(row, col);
		if ( isOpen(position)) {
			return position;
		};
		return null;
	}
	
	Position firstOpen() {
		return getOpenPositions().first()
	}
	
	Position win() {
		return winningPositions(turn(), game.movesAsMap()).find {true}

	}
	
	Position block() {
		return winningPositions(lastTurn(), game.movesAsMap()).find {true}

	}
		
	Position fork() {
		return getOpenPositions()
				.find { position -> multipleWinningPositions(position)}
	}
	
	Position blockFork() {
		if (game.getMoves().size() == 3) {				
			if (isInPosition(Player.X, 1, 1) && isInPosition(Player.X, 3, 3)) {
				return new Position(1, 2);
			} else if (isInPosition(Player.X, 1, 3) && isInPosition(Player.X, 3, 1)) {
				return new Position(1, 2);
			} else if (isInPosition(Player.X, 3, 2) && isInPosition(Player.X, 2, 3)) {
				return new Position(3, 3);
			} 				
		}
		return null;
	}
	
	Position firstMove() {
		if (game.getMoves().isEmpty()) {
			return new Position(1,1);
		}
		return null;
	}
	
	Position oppositeCorner() {
		return or (	oppositeOpen(new Position(1,1), new Position(3,3)),
					oppositeOpen(new Position(3,3), new Position(1,1)),
					oppositeOpen(new Position(3,1), new Position(1,3)),
					oppositeOpen(new Position(1,3), new Position(3,1)));
	}
	
	Position center() {
		return openPosition(2,2);
	}
	
	Position sandwich() {
		return or ( sandwichOpen(new Position(3,3), new Position(1,1)),
				    sandwichOpen(new Position(1,1), new Position(3,3)),
				    sandwichOpen(new Position(3,1), new Position(1,3)),
				    sandwichOpen(new Position(1,3), new Position(3,1)));
	}
	
	Position emptyCorner() {
		return or(openPosition(1,1),
				  openPosition(1,3),
				  openPosition(3,1),
				  openPosition(3,3));
	}
	
	Position emptySide() {
		return or(openPosition(1,2),
				  openPosition(2,1),
				  openPosition(2,3),
				  openPosition(3,2));
				
	}
	
	// static methods
	
	List<Position> getAllPositions() {
		List<Position> positions = new ArrayList<Position>();
		for (int row = 1; row <= 3; row++) {
			for (int col = 1; col <= 3; col++) {
				positions.add(new Position(row, col));
			}
		}
		return positions;
	}
	
	static List<List<Position>> getWinningCombos() {
		List<List<Position>> wins = new ArrayList<List<Position>>();
		
		wins.add(asList(new Position(1,1), new Position(1,2), new Position(1,3)));
		wins.add(asList(new Position(2,1), new Position(2,2), new Position(2,3)));
		wins.add(asList(new Position(3,1), new Position(3,2), new Position(3,3)));
		
		wins.add(asList(new Position(1,1), new Position(2,1), new Position(3,1)));
		wins.add(asList(new Position(1,2), new Position(2,2), new Position(3,2)));
		wins.add(asList(new Position(1,3), new Position(2,3), new Position(3,3)));
		
		wins.add(asList(new Position(1,1), new Position(2,2), new Position(3,3)));
		wins.add(asList(new Position(3,1), new Position(2,2), new Position(1,3)));
		
		return wins;	
	}
		
	static boolean matches(Map<Position, Player> moves, Player player,  List<Position> winningCombo) {
		return winningCombo.every {position -> moves.get(position) == player}				
	}
	
	static boolean isWinner(Map<Position, Player> moves, Player player) {
		return getWinningCombos()
								.any { combo -> matches(moves, player, combo) };
	}
			
	boolean isWinnerWithMove(Map<Position, Player> moves, Player player, Position position) {
		Map<Position, Player> alteredMoves = new HashMap<Position, Player>(moves);
		alteredMoves.put(position, player);
		return getWinningCombos().any { combo -> matches(alteredMoves, player, combo)};
	}
	
	boolean isOpenInMap(Map<Position, Player> moves, Position position) {
		return (moves.get(position) == null);
	}
	
	
	List<Position> openPositions(Map<Position, Player> moves) {
		return getAllPositions().findAll {it -> isOpenInMap(moves, it)};
	}
		
	 List<Position> winningPositions(Player player, Map<Position, Player> moves) {
		return openPositions(moves)
				.findAll { position -> isWinnerWithMove(moves, player, position)}
	}
}
