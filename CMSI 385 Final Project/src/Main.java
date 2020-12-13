import java.util.Scanner;

import utilities.Language;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Enter a language regex: ");
		Scanner input = new Scanner(System.in);
		String s = input.next();
		Language l = new Language(s);
		System.out.println(l.getLanguage());
		System.out.println(l.getNFA().toString());
		input.close();
	}
}

