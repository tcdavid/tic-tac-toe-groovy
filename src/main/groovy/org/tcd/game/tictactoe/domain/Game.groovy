package org.tcd.game.tictactoe.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
public class Game {
	
	@Id
	String id;

	List<Move> moves;
	Level level;
	Player computerPlaysAs;
	Status status;
	Player winner;
	Long sequence;
	
	public Game() {}
	
	public Game(String id, List<Move> moves, Level level, Player computerPlaysAs, Long sequence) {
		this.moves = moves;
		this.level = level;
		this.computerPlaysAs = computerPlaysAs;
		this.id = id;
		this.status = Status.OPEN;
		this.sequence = sequence;
	}
	
	public Game( Level level, Player computerPlaysAs, Long sequence) {
		this(null, new ArrayList<Move>(), level, computerPlaysAs, sequence);
	}
		
	public void addMove(Move move){
		moves.add(move);
	}
	
	public Player playerAt(int row, int col) {
		return playerAt(new Position(row, col));
	}
	
	public Player playerAt(Position position) {
		return movesAsMap().get(position);
	}
	
	public Map<Position, Player> movesAsMap() {
		
		Map<Position, Player> map = new HashMap<Position, Player>();
		
		for (Move move: moves) {
			map.put(move.getPosition(), move.getPlayer());
		}
		return map;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", moves=" + moves + ", level=" + level
				+ ", computerPlaysAs=" + computerPlaysAs + ", status=" + status
				+ ", winner=" + winner + "]";
	}

	
}