package utilities;

import java.util.HashSet;
import java.util.List;
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
	/*
	public void setNewStart(State s, Character c) {
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
	*/
	private void traverseStates(State start) {
		if(start.getAccept()) {
			this.endStates.add(start);
		}
		if(start.hasTransitions()) {
			Set<Character> keys = start.getTransitions();
			for(Character c : keys) {
				List<State> states = start.getNextState(c);
				for(State s : states){
					if(!this.states.contains(s)) {
						this.states.add(s);
						traverseStates(s);
					}
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
		traverseStates(first);
	}
	
	public List<State> getNextState(State first, Character c) {
		return first.getNextState(c);
	}
	
	public void setAllAccept(boolean bool) {
		for(State s : this.states) {
			s.setAccept(bool);
			if(bool) {
				this.endStates.add(s);
			}
		}
		if(!bool) {
			this.endStates = new HashSet<State>();
		}
	}
	
	public Set<State> getEndStates(){
		return this.endStates;
	}
	
	public void setEndState(State end) {
		this.endStates = new HashSet<State>();
		end.setAccept(true);
		this.endStates.add(end);
		this.states.add(end);
	}
	
	public String toString() {
		String s = "";
		s = toStringHelper();
		return s;
	}
	
	private String toStringHelper() {
		String table = "^ means Start\n* means Accept\n";
		table+= "        0      1      e\n";
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
