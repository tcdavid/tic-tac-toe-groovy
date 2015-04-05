package org.tcd.game.tictactoe.domain;

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
public class Move {
	Position position;
	Player player;
		
	// for marshalling, unmarshalling
	public Move() {}
	
	public Move(int row, int column, Player player) {
		this(new Position(row, column), player);
	}
	
	public Move(Position position, Player player) {
		super();
		this.position = position;
		this.player = player;
	}
	
	@Override
	public String toString() {
		return "Move [position=" + position + ", player=" + player + "]";
	}
	
}
