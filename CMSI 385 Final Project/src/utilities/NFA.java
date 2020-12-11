package utilities;

import java.util.HashSet;
import java.util.Set;

public class NFA {
	private State start;
	private Set<State> states;
	private Set<State> endStates;
	
	public NFA() {
		this.start = null;
		this.states = new HashSet<State>();
		this.endStates = new HashSet<State>();
	}
	
	public State getStart() {
		return this.start;
	}
	
	public void setStart(State s) {
		if(this.start == null) {
			this.start = s;
			this.states.add(s);
			traverseStates(this.start);
		}	
	}
	
	public void setStart(State s, Character c) {
		if(this.start == null) {
			this.start = s;
		}
		else {
			s.addState(c, this.start);
			this.start = s;
		}
		this.states.add(s);
		traverseStates(this.start);
	}
	
	private void traverseStates(State start) {
		if(start.getAccept()) {
			this.endStates.add(start);
		}
		if(start.hasTransitions()) {
			Set<Character> keys = start.getTransitions();
			for(Character c : keys) {
				if(!this.states.contains(start.getNextState(c))) {
					this.states.add(start.getNextState(c));
					traverseStates(start.getNextState(c));
				}
			}
		}
		else {
			this.states.add(start);
		}
	}
	
	public void addState(State first, Character c, State second) {
		first.addState(c, second);
		this.states.add(first);
		this.states.add(second);
	}
	
	public State getNextState(State first, Character c) {
		return first.getNextState(c);
	}
	
	public void setAllAccept(boolean bool) {
		if(!bool) {
			this.endStates = new HashSet<State>();
		}
		for(State s : this.states) {
			s.setAccept(bool);
			if(bool) {
				this.endStates.add(s);
			}
		}
	}
	
	public Set<State> getEndStates(){
		return this.endStates;
	}
	
	public void setEndState(State end) {
		this.endStates = new HashSet<State>();
		this.endStates.add(end);
	}
	
	public String toString() {
		String s = "";
		s = toStringHelper();
		return s;
	}
	
	private String toStringHelper() {
		String table = "^ means Start\n* means Accept\n";
		table+= "         0       1\n";
		for(State s : this.states) {
			String str = s.toString();
			if(this.start == s) {
				str=str.replaceFirst(" ", "^");
			}
			table+=str;
			table+="\n";
		}
		return table;
	}
	
	
}
