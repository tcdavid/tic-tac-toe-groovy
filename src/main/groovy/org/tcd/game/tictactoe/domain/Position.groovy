package org.tcd.game.tictactoe.domain;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
public class Position {
	int row;
	int column;
	
	// for marshalling / unmarshalling
	public Position() {}
	
	public Position(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

}
