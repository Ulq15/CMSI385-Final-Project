package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class State {
	private boolean isAccept;
	private Map<Character, List<State>> transitions;
	private char id;
	public State(char id) {
		this.isAccept=false;
		this.transitions = new HashMap<Character, List<State>>();
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
		List<State> stateList;
		if(this.transitions.get(c) == null) {
			stateList = new ArrayList<State>();
		}else {
			stateList = this.transitions.get(c);
		}
		
		stateList.add(s);
		this.transitions.put(c, stateList);
	}
	
	public List<State> getNextState(Character c) {
		return this.transitions.get(c);
	}
	
	public boolean hasTransitions(){
		return this.transitions.size()>0;
	}
	
	public Set<Character> getTransitions() {
		return this.transitions.keySet();
	}
	
	public String toString() {
		String name =" "+this.id;
		if(this.isAccept) {
			name=name.replaceFirst(" ", "*");
		}
		String zeros = "";
		String ones = "";
		String lambda = "";
		for(Character c : this.transitions.keySet()) {
			List<State> stateList = this.transitions.get(c);
			for(State s : stateList) {
				if(c=='0')
					zeros+=" "+s.getID();
				else if(c =='1')
					ones+=" "+s.getID();
				else if(c == 'e' || c == 'l')
					lambda+=" "+s.getID();
			}
		}
		String format = String.format("%7s%7s%7s", zeros, ones, lambda);
		
		return name+format;
	}
}
