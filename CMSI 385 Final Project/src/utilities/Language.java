package utilities;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Language {
	private String language;
	private Stack<Character> characters;
	private Set<Character> operators;
	private Stack<NFA> stackNfa;
	private char stateID = 'a';
	private NFA nfa;
	
	public Language(String input) {
		this.language = addDotConcat(input);
		this.characters = new Stack<Character>();
		this.operators = new HashSet<Character>();
		this.stackNfa = new Stack<NFA>();
		this.nfa = new NFA();
		populateSet();
		populateStack();
		this.nfa = generateNFA();
	}
	
	private void populateSet() {
		this.operators.add('.');
		this.operators.add('*');
		this.operators.add('|');
		this.operators.add('+');
	}
	
	private void populateStack() {
		for(int i=this.language.length()-1; i>=0; i--) {
			this.characters.push(this.language.charAt(i));
		}
	}
	
	private boolean isInputChar(Character symbol) {
		return (symbol == '0' || symbol =='1');
	}
	
	private String addDotConcat(String regular) {
		String newRegular = "";
		for(int i=0; i<regular.length()-1; i++) {
			if(regular.charAt(i)!=' ') {
				if(isInputChar(regular.charAt(i)) && isInputChar(regular.charAt(i+1))) {
					newRegular += regular.charAt(i) + ".";
				}
				else if (isInputChar(regular.charAt(i)) && regular.charAt(i+1) == '(') {
					newRegular += regular.charAt(i) + ".";
				}
				else if ( regular.charAt(i) == ')' && isInputChar(regular.charAt(i+1)) ) {
					newRegular += regular.charAt(i) + ".";
				}
				else if (regular.charAt(i) == '*'  && isInputChar(regular.charAt(i+1)) ) {
					newRegular += regular.charAt(i) + ".";
				}
				else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
					newRegular += regular.charAt(i) + ".";
				}
				else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
					newRegular += regular.charAt(i) + ".";
				}
				else {
					newRegular += regular.charAt(i);
				}
			}
		}
		newRegular += regular.charAt(regular.length() - 1);
		return newRegular;
	}
	
	private boolean priority(Character first, Character second) {
		if(first == second) {	return true;	}
		if(first == '*') 	{	return false;	}
		if(second == '*')  	{	return true;	}
		if(first == '.') 	{	return false;	}
		if(second == '.') 	{	return true;	}
		if(first == '|' || first == '+') 	{	return false;	}
		else 				{	return true;	}
	}
	
	private boolean isOperator(Character symbol) {
		return this.operators.contains(symbol);
	}
	
	private void operate(Character op) {
		switch (op) {
		case ('|'):
			union ();
			break;
			
		case ('+'):
			union ();
			break;

		case ('.'):
			concatenation ();
			break;

		case ('*'):
			star ();
			break;
			
		default :
			System.out.println("Unkown Symbol: "+op);
			System.exit(1);
			break;
		}		
	}
	
	private void star() {
		NFA nfaStates = this.stackNfa.pop();
		State before = new State(stateID++);
		State after = new State(stateID++);
		after.setAccept(true);
		
		nfaStates.setAllAccept(false);
		
		before.addState('e', after);
		
		for(State s : nfaStates.getEndStates()) {
			s.addState('e', after);
			s.addState('e', nfaStates.getStart());
		}
		nfaStates.setStart(before, 'e');
		nfaStates.setEndState(after);
		this.stackNfa.push(nfaStates);
	}
	
	private void union() {
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		Set<State> end1 =  nfa1.getEndStates();
		Set<State> end2 =  nfa2.getEndStates();
		
		nfa1.setAllAccept(false);
		nfa2.setAllAccept(false);
		
		State start = new State (stateID++);
		State end = new State (stateID++);
		end.setAccept(true);
		
		start.addState('e', nfa1.getStart());
		start.addState('e', nfa2.getStart());
		
		for(State s :end1) {
			s.addState('e', end);
		}
		for(State s : end2) {
			s.addState('e', end);
		}
		
		NFA nfa3 = new NFA();
		nfa3.setStart(start);
		stackNfa.push(nfa3);
	}
	
	private void concatenation() {
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		Set<State> goals = nfa1.getEndStates();
		nfa1.setAllAccept(false);
		for(State s : goals) {
			s.addState('e', nfa2.getStart());
		}
		for(State s : nfa2.getEndStates()) {
			nfa1.setEndState(s);
		}
		stackNfa.push(nfa1);
	}
		
	public NFA generateNFA() {
		
	}
	
	public String testNFA() {
		State c = new State(stateID++);
		State b = new State(stateID++);
		State a = new State(stateID++);
		State d = new State(stateID++);
		State e = new State(stateID++);
		e.setAccept(true);
		a.addState('0', b);
		a.addState('1', c);
		b.addState('0', d);
		b.addState('1', a);
		c.addState('0', a);
		c.addState('1', d);
		d.addState('1', e);
		NFA nf = new NFA();
		nf.setStart(a);
		return nf.toString();
	}
}
