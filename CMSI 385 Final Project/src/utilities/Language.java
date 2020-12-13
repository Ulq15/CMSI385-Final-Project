package utilities;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Language {
	private String language;
	private Stack<Character> characters;
	private Set<Character> operators;
	private Stack<NFA> stackNfa;
	private Stack<Character> stackOperator;
	private static char stateID = 'a';
	private NFA nfa;
	
	public Language(String input) {
		this.language = formatString(input);
		this.characters = new Stack<Character>();
		this.operators = new HashSet<Character>();
		this.stackNfa = new Stack<NFA>();
		this.stackOperator = new Stack<Character>();
		populateOpSet();
		populateStack();
		generateNFA();
		this.nfa = stackNfa.peek();
	}
	
	private void populateOpSet() {
		this.operators.add('.');
		this.operators.add('*');
		this.operators.add('|');
		this.operators.add('+');
		this.operators.add('(');
		this.operators.add(')');
	}
	
	private void populateStack() {
		for(int i=this.language.length()-1; i>=0; i--) {
			this.characters.push(this.language.charAt(i));
		}
	}
	
	private boolean isInputChar(char symbol) {
		return (symbol=='0' || symbol=='1' || symbol=='e' || symbol=='l');
	}
	
	private String formatString(String regular) {
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
	
	private boolean isOperator(Character symbol) {
		return this.operators.contains(symbol);
	}
	
	private void operate(Character op) {
		switch (op) {
		case ('|'):
			union();
			break;
			
		case ('+'):
			union();
			break;

		case ('.'):
			concatenation();
			break;

		case ('*'):
			star();
			break;
			
		case ('('):
			break;
		
		case (')'):
			break;
			
		default :
			System.out.println("Unkown Symbol: "+op);
			System.exit(1);
			break;
		}		
	}
	
	private void star() {
		NFA nfa1 = this.stackNfa.pop();
		State before = new State(stateID++);
		State after = new State(stateID++);
		after.setAccept(true);
		Set<State> goals = nfa1.getEndStates();
		nfa1.setAllAccept(false);
		
		before.addState('e', after);
		
		for(State s : goals) {
			s.addState('e', after);
		}
		after.addState('e', nfa1.getStart());
		NFA nfa2 = new NFA();
		nfa2.setStart(before);
		nfa2.setEndState(after);
		this.stackNfa.push(nfa2);
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
		NFA nfa2 = this.stackNfa.pop();
		NFA nfa1 = this.stackNfa.pop();
		Set<State> goals = nfa1.getEndStates();
		nfa1.setAllAccept(false);
		for(State s : goals) {
			nfa1.addState(s, 'e', nfa2.getStart());
		}
		for(State s : nfa2.getEndStates()) {
			nfa1.setEndState(s);
		}
		this.stackNfa.push(nfa1);
	}
		
	public void generateNFA() {
		char[] array = this.language.toCharArray();
		for(int i = 0; i < array.length; i++) {
			if(isOperator(array[i])) {
				this.stackOperator.push(array[i]);
				if(array[i] == '*') {
					Character op = this.stackOperator.pop();
					operate(op);
				}
			}
			else if(isInputChar(array[i])) {
				State start = new State(stateID++);
				State curState = new State(stateID++);
				curState.setAccept(true);
				NFA subNFA = new NFA();
				start.addState(array[i], curState);
				subNFA.setStart(start);
				this.stackNfa.push(subNFA);
				if(!this.stackOperator.isEmpty()) {
					Character op = this.stackOperator.pop();
					operate(op);
				}
			}
			else {
				System.out.println("Unkown Symbol: "+array[i]);
				System.exit(1);
			}
		}	
	}
	
	public NFA getNFA() {
		return this.nfa;
	}
	
	public String testNFA() {
		Language.stateID='a';
		State a = new State(stateID++);
		State b = new State(stateID++);
		State c = new State(stateID++);
		State d = new State(stateID++);
		State e = new State(stateID++);
		e.setAccept(true);
		a.addState('e', b);
		a.addState('e', c);
		b.addState('0', d);
		b.addState('1', a);
		c.addState('0', a);
		c.addState('1', d);
		d.addState('1', e);
		e.addState('e', a);
		NFA nf = new NFA();
		nf.setStart(a);
		return nf.toString();
	}
	
	public String getLanguage() {
		return this.language;
	}
}
