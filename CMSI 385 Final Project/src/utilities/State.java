package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {
	private boolean isAccept;
	private Map<Character, State> transitions;
	private char id;
	public State(char id) {
		this.isAccept=false;
		this.transitions = new HashMap<Character, State>();
		this.id = id;
	}
	
	public char getID() {
		return this.id;
	}
	public void setAccept(boolean status) {
		this.isAccept = status;
	}
	
	public boolean getAccept() {
		return this.isAccept;
	}
	
	public void addState(Character c, State s) {
		this.transitions.put(c, s);
	}
	
	public State getNextState(Character c) {
		return this.transitions.get(c);
	}
	
	public boolean hasTransitions(){
		return this.transitions.size()>0;
	}
	
	public Set<Character> getTransitions() {
		return this.transitions.keySet();
	}
	
	public String toString() {
		String s =" "+this.id;
		for(Character c : this.transitions.keySet()) {
			s+="       "+this.getNextState(c).getID();
		}
		if(this.isAccept) {
			s+="*";
		}
		return s;
	}
}
