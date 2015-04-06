package org.tcd.game.tictactoe.domain;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Move {
	Position position;
	Player player;
		
	// for marshalling, unmarshalling
	Move() {}
	
	Move(int row, int column, Player player) {
		this(new Position(row, column), player);
	}
	
	Move(Position position, Player player) {
		super();
		this.position = position;
		this.player = player;
	}
	
}
