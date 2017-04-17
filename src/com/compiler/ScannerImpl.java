package com.compiler;

/*In total, 3 classes:
 * 1). KeywordtokenClass(contains logic for tokens that matches keywords)
 * 2). OthertokenClasses(contains logic for token matching comments, string literal, 
 * identifier or any other token).
 * 3). ScannerImpl(contains Main method)
 * Flow of Main- 
 * -gets list of all the lines of input file and stores each line at each index of string array.
 * -Checks if line contains comment and calls ifComment method.
 * -Checks if line contains string literal and calls ifStringLiteral method
 * -breaks a particular line into words and calls checkKeyword(), idStartingWithKeyword() and
 * findOthertokens() methods.
 * */
import java.util.*; //package contains the collection framework
import java.nio.charset.*;//package defines charsets
import java.nio.file.*;//package defines interfaces and classes for JVM

/* this class contains information of token: token and token number.  */
class Token{
	static public int tokennum;
	static public String lexeme;
}
//check if token matches reserved keywords or not.If matches, it gives its token number.
class KeywordtokenClass {
	int ind=0; //index of arraylist that is used to store token object
	Token token=new Token();  //object of class Token
	
	/* the method checks if token is keyword or not. If yes, it returns true,
	 * else false. */
	public boolean checkKeyword(String s) {
		
		//object of OthertokenClasses
		OthertokenClasses oClasses = new OthertokenClasses();
		int flag = 0;
		
		HashMap<String, Integer> Hmap = new HashMap<>();
		Hmap.put("begin", 7);
		Hmap.put("end", 8);
		Hmap.put("if", 9);
		Hmap.put("then", 10);
		Hmap.put("while", 11);
		Hmap.put("do", 12);
		Hmap.put("integer", 3);
		Hmap.put("string", 3);
		Hmap.put("logical", 3);
		Hmap.put("true", 2);
		Hmap.put("false", 2);
		Hmap.put("div", 5);
		Hmap.put("rem", 5);
		Hmap.put("or", 4);
		Hmap.put("read", 13);
		Hmap.put("write", 20);
		Hmap.put("writeln", 20);
		
		/*if token matches any keyword, flag=1, and token object is assigned
		 * token number and tokenname and its inserted into arraylist by
		 * calling method putInArrayList() */
		if (Hmap.containsKey(s)) {
			System.out.println("token no : " + Hmap.get(s));
			flag = 1;
			token.tokennum=Hmap.get(s);
			token.lexeme=s;
			oClasses.putInArrayList(token);
		}
		if (flag == 1)
			return true;
		else
			return false;
	}
}


/*
 * The class has 4 methods. 
 * 1). putInArrayList(): adds token object into arraylist
 * 2). gettoken(): gets the next token.
 * 3). idStartingwithKeyword(): accepts identifier like int123, etc.
 * 4). findOtherTokens(): checks if input token falls in any of the different 
 * classes of tokens. 
 */
class OthertokenClasses {
	Token token=new Token(); //object of Token Class
	//arraylist that stores all token objects.
	List<Token> arraylist = new ArrayList<>(); 
	int ind=0; // index of arraylist initialized to zero.
	//object of KeywordtokenClass
	KeywordtokenClass kClass = new KeywordtokenClass();
	Boolean b;
	int t = 0;
	int flag = 0, flag1 = 0;
	String[] keywords = { "begin", "end", "if", "then", "while", "do", "integer", "string", "logical", "true", "false",
			"div", "rem", "end", "or", "read", "write", "writeln" };

	//adds token object into arraylist
	public void putInArrayList(Token token) {
		
		arraylist.add(token);
	}
	
	//returns token for next index-ind from arraylist of token objects
	public Token gettoken() {
	
		Token tokenobj=null;
		if(ind<=arraylist.size() && arraylist.get(ind)!=null){
			tokenobj=arraylist.get(ind++);
		}
		return tokenobj;	
	}
	
	
	/* checks if identifier contains any keyword or not.*/
	public String idStartingWithKeyword(String s) {
		char[] charArray = s.toCharArray();
		String temp = "", temp1 = "";

		if (s.matches("[a-zA-Z]+")) {
			System.out.println("token no:: 1");
			flag = 1;
		} else {
			loop: for (int i = 0; i < charArray.length; i++) {
				temp = temp + Character.toString(charArray[i]);
				 for (int j = 0; j < keywords.length; j++) {
					if (temp.equals(keywords[j])) {
						b = kClass.checkKeyword(temp);
						t = i + 1;
						break loop;
					}
				}
			}
		}

		// checks if token matches digits
		if (s.matches("\\d+")) {
			System.out.println("token no:: 2");
			token.tokennum=2;
			token.lexeme=s;
			putInArrayList(token);
			flag1 = 1;
		}
		for (int j = t; j < charArray.length; j++) {
			temp1 = temp1 + Character.toString(charArray[j]);
		}
		return temp1;
	}

	/* checks tokens and their token numbers. assigns token object with token number
	 * and tokenname and puts it into arraylist.*/
	public void findOthertokens(String s) {
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			loop: {
				if (charArray[i] == 'a' || charArray[i] == 'b' || charArray[i] == 'c' || 
					charArray[i] == 'd' || charArray[i] == 'e' || charArray[i] == 'f' ||
					charArray[i] == 'g' || charArray[i] == 'h' || charArray[i] == 'i' ||
					charArray[i] == 'j' || charArray[i] == 'k' || charArray[i] == 'l' || 
					charArray[i] == 'm' || charArray[i] == 'n' || charArray[i] == 'o' ||
					charArray[i] == 'p' || charArray[i] == 'q' || charArray[i] == 'r' ||
					charArray[i] == 's' || charArray[i] == 't' || charArray[i] == 'u' || 
					charArray[i] == 'v' || charArray[i] == 'w' || charArray[i] == 'x' || 
					charArray[i] == 'y' || charArray[i] == 'z') {
					if (flag == 0)
					System.out.println("token no: 1");
					token.tokennum=1;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] >= 1 && charArray[i] <= 9) {
					if (flag1 == 0) {
						System.out.println("token no: 2");
						token.tokennum=2;
						token.lexeme=s;
						putInArrayList(token);			
					}else
						System.out.println("Invalid Number");
					break loop;
					
				} else if (charArray[i] == '+' || charArray[i] == '-') {
					System.out.println("token no: 4");
					token.tokennum=4;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '/' || charArray[i] == '*') {
					System.out.println("token no: 5");
					token.tokennum=5;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '=') {
					if (charArray[i - 1] == ':'){
						int flag=0;
					if(flag==0){
						System.out.println("token no: 19");
						token.tokennum=19;
						token.lexeme=s;
						putInArrayList(token);
					}
					else
						System.out.println("Not an assignment token");
						
					} else {
						System.out.println("token no: 6");
						token.tokennum=6;
						token.lexeme=s;
						putInArrayList(token);
						break loop;
					} 
					
				} else if (charArray[i] == '<' || charArray[i] == '>') {
					System.out.println("token no: 6");
					token.tokennum=6;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '(') {
					System.out.println("token no: 14");
					token.tokennum=14;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == ')') {
					System.out.println("token no: 15");
					token.tokennum=15;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == ';') {
					System.out.println("token no: 16");
					token.tokennum=16;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '!') {
					System.out.println("token no: 17");
					token.tokennum=17;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '!' && charArray[i + 1] == '=') {
					System.out.println("token no: 6");
					token.tokennum=6;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == '.') {
					System.out.println("token no: 18");
					token.tokennum=18;
					token.lexeme=s;
					putInArrayList(token);
					break loop;
					
				} else if (charArray[i] == ':' && charArray[i + 1] == '=') {
					break loop;
					
				} else {
					System.out.println("Not a legal token!");
					break loop;
				}
			}
		}
	}

	
	
	/*
	 * checks if the word read currently is "comment" or not.If it is, then it
	 * reads all he words following COMMENT until a semicolon is encountered.
	 * The entire comment portion is deleted.
	 */
	public void ifComment(String s) {
		String[] sarr = null;
		sarr = s.split("\\s+"); // splits a line into words.
		// loop traverses each word of line.
		// System.out.println("In comment method");
		loop: for (int j = 0; j < sarr.length; j++) {
			if (sarr[j].equals("comment")) {
				for (int m = j + 1; m < sarr.length; m++) {
					if (sarr[m].contains(";"))
						break loop;
					else {
					}
				}
			}
		}
	}

	
	
	/*
	 * checks if word currently read is a string or not.If it is, it checks that
	 * the length of string does not exceed 256 characters.
	 */
	public void ifStringLiteral(String s) {

		String[] sarr = null;
		String temp = "";
		int k, f = 0;
		sarr = s.split("\\s+"); // splits a line into words.
		// loop traverses each word of line.
		// System.out.println("In String literal method...");
		loop: for (int j = 0; j < sarr.length; j++) {
			if (sarr[j].equals("\"")) {
				k = j + 1;
				for (int m = j + 1; m < sarr.length; m++) {
					if (sarr[m].contains("\""))
						f = m - 1;
					for (int t = k; t <= f; t++) {
						temp = temp.concat(sarr[t]);
					}
					int length = temp.length();
					if (length >= 10)
						System.out.println("Length of string exceeded 256!");
				}
				break loop;
				
			} else {
				System.out.println(sarr[j]);
			}
		}
	}
}



// Main Class
public class ScannerImpl {

	public static void main(String[] args) throws Exception {
		//object of OthertokenClasses
		OthertokenClasses otc = new OthertokenClasses();
		//object of KeywordtokenClass
		KeywordtokenClass kCheck = new KeywordtokenClass();
		//object of Token class
		Token token=new Token();
		String str;
		Boolean b;
		// All the lines from the input file are stored in the list.
		List<String> list = Files.readAllLines(
				Paths.get("C:\\Users\\Aneri Patel\\Documents\\CS 4110 Compiler"
						+ "\\Infile.txt"), StandardCharsets.UTF_8);
		
		// String array-arr stores each line for input file at each index.
		String[] arr = list.toArray(new String[list.size()]);
		
		// String array-sarr stores words of a particular line of the file.
		String[] sarr = null;
		
		// loop traverses one line of the file at a time.
		for (int i = 0; i < arr.length; i++) {
			
			// if the line contains comment, its corresponding method is called.
			if (arr[i].contains("COMMENT")) {
				otc.ifComment(arr[i]);
				i++;
			}
			// if the line contains string literal, its corresponding method is
			// called.
			if (arr[i].contains("\"")) {
				otc.ifStringLiteral(arr[i]);
				i++;
			}
			sarr = arr[i].split("\\s+"); // splits a line into words.
			
			// loop traverses each word of line.
			loop: for (int j = 0; j < sarr.length; j++) {
				System.out.println(sarr[j]);
				b = kCheck.checkKeyword(sarr[j].toLowerCase());

				if (!b) {
					str = otc.idStartingWithKeyword(sarr[j].toLowerCase());
					otc.findOthertokens(str);
				}
			}
		}	
	}
}