package org.tcd.game.tictactoe.domain;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Position {
	int row;
	int column;
	
	// for marshalling / unmarshalling
	Position() {}
	
	Position(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

}
