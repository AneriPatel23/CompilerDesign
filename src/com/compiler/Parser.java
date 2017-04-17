/*the program is a recursive descent parser with symbol table routines integrated into 
 * a parser.
 * On variable declaration, program will add name to symbol table.
 * On variable use, program will look up the variable in all current scopes.
 * Input to the parser: correct little algol program with declarations and assignments.
 * Output to parser: leftmost derivation and dump of symbol table.
 */
/* There are 2 classes" 1). Info: whose object stores all the information of 
 * a variable - its scope, declaration type name and offset.
 * 2). Parser: it implements all the parser methods with symbol table
 * routines integrated to it.
 * */
package com.compiler;

import java.io.*; //package performs I/O operations
import java.nio.charset.StandardCharsets;//package contains definitions for charsets
import java.nio.file.Files;//package handles files,directories,etc.
import java.nio.file.Paths;//package handles path of file
import java.util.List;//package handles list of elements
import java.util.Scanner;//package breaks input into tokens



/* This class stores all the details of a variable i.e. its scope number(scopeNo),
 * name(str) , declaration type(type) and offset of its location(curr_offset).
 * */
class Info{
	SymbolTable st=new SymbolTable(); // st is object of SymbolTable file.
	
	/* token is object of token class in Scanner that stores 
	1). token name(token.lexeme) 2). token number(token.tokennum) 
	3). token index for Hash map (token.index)*/
	Token token=new Token();  
	String str;
	int scopeNo;
	String type;
	int curr_offset=0;
}


class ExpressionRecord {
	Info info = new Info();
	int location = info.curr_offset;
	char type = info.type.charAt(0);
}


public class Parser {
	
	static int index = 1; // index of the token list in Scanner,initialized to 1
	static int tok; // current token number is stored
    static String lexeme; //stores token name.
	/* otherTokenClasses is the object of class-OtherTokenlasses of Scanner */
	static OthertokenClasses otherTokenClasses = new OthertokenClasses();
	/* st is object of SymbolTable file. */
    static SymbolTable st=new SymbolTable();
    Info info=new Info();
    
    
	/* [1] program-> blockst '.'
	 *  token no. 18 -> '.' */
    
	public static void program(Token token) {
		System.out.print("1 ");
		writeProlog("codefile.txt");
		blockst(token);
		match(token.tokennum, 18);
		writePostlog("codefile.txt");
	}
	
	
	/* [2] blockst-> 'begin' stats 'end' 
	 * token no. 7 -> BEGIN token no. 8 -> END 
	 * When token number matches 7, st.openscope() is called.
	 * scope is opened. values of scope increases by 1. 
	 * When token number matches 8, st.closed_a_scope() is called. */
	
	public static void blockst(Token token) {
		System.out.print("2 ");
		if (token.tokennum == 7) {
			match(token.tokennum, 7);
			st.openscope();
			stats(token);
			match(token.tokennum, 8);
			st.closed_a_scope();
		} else
			System.exit(0);
	}
	
	
	/* [3,4] stats-> stmt ';' stats/ <empty> token no. 16 -> ';' token no. 3 ->
	 * INTEGER token no. 9 -> IF token no. 1 -> identifier token no. 7 -> BEGIN
	 * token no. 11 -> WHILE token no. 13 -> READ */
	
	public static void stats(Token token) {
		if (token.tokennum == 3 || token.tokennum == 9 || token.tokennum == 1 || token.tokennum == 7
				|| token.tokennum == 11 || token.tokennum == 13 || tok == 16) {
			System.out.print("3 ");
			stmt(token);
			match(token.tokennum, 16);
			stats(token);
		} else {
			System.out.print("4  ");
		}
	}

	
	
	/* [5] decl-> type id
	 * If token number matches 3, its a type token. The type is stored and 
	 * next token is seen if its a variable. If it is, it is checked if the variable is 
	 * already present in currently opened scope. It its there, duplicate declaration.
	 * If not, object of info is inserted into symbol table. offset is decreased by 4.*/
	
	public static void decl(Token token) {
		// info is object of Info class
		Info info = new Info();

		// st is object of Symbol Table file.
		SymbolTable st = new SymbolTable();
		System.out.print("5 ");

		// ch stores first letter of declaration type i.e. i, l or s.
		char ch = 0;

		/*
		 * if token is declaration type, its type and scope number is stored in
		 * info object
		 */
		if (token.tokennum == 3) {
			info.type = Token.lexeme;
			info.str = Token.lexeme;
			info.scopeNo = st.top;
			char[] charArr = Token.lexeme.toCharArray();
			for (int i = 0; i < charArr.length; i++) {
				ch = charArr[0];
			}
			char base_type = ch;
			// checks if type is integer, logical or string.
			if (base_type == 'i' || base_type == 'l' || base_type == 's') {
				match(token.tokennum, 3);

				Boolean b = st.find_in_local(Token.lexeme);
				if (b) {
					System.out.println("duplicate declaration!");
				} else {
					st.insert(info);
					info.curr_offset -= 4;
					match(token.tokennum, 1);
				}
			}
		} else
			System.out.println("error");
	}
	
	
	
	/* [6-12] stmt-> decl/ifst /asst /blockst /loopst /iost /<empty> token no. 3
	 * -> INTEGER token no. 9 -> IF token no. 1 -> identifier token no. 7 ->
	 * BEGIN token no. 11 -> WHILE token no. 13 -> READ  */
	public static void stmt(Token token) {
		if (token.tokennum == 3) {
			System.out.print("6 ");
			decl(token);
		} else if (token.tokennum == 9) {
			System.out.print("7 ");
			ifst(token);
		} else if (token.tokennum == 1) {
			System.out.print("8 ");
			asst(token);
		} else if (token.tokennum == 7) {
			System.out.print("9 ");
			blockst(token);
		} else if (token.tokennum == 11) {
			System.out.print("10 ");
			loopst(token);
		} else if (token.tokennum == 13) {
			System.out.print("11 ");
			iost(token);
		} else
			System.out.print("12 ");
	}

	
	
	/* [13] asst->idref ':=' expression 
	 * token no. 19 -> ':=' */
	public static void asst(Token token) {
		if (token.tokennum == 1) {
			System.out.print("13 ");
			Info info = new Info();

			info = st.find_in_all(token.lexeme);
			char id_type = info.str.charAt(0);
			int id_loc = info.curr_offset;

			match(token.tokennum, 1);
			match(token.tokennum, 19);

			ExpressionRecord er = null;
			er = expression(token, er);
			int exp_loc = er.location;
			char exp_type = er.type;
			if (id_loc != exp_loc) {
				System.out.println("types doesnot match!");
			} else {
				CodeGen("lw $t0  " + exp_loc);
				CodeGen("sw $t0  " + id_loc);
			}
			// idref(token);

		}
	}
	
	
	/* [14] ifst-> 'if' expression 'then' stmt 
	 * token no. 9 -> IF token no. 10 ->THEN */
	public static void ifst(Token token) {
		System.out.print("14 ");
		if (token.tokennum == 9) {
			match(token.tokennum, 9);
			ExpressionRecord er = null;
			er = expression(token, er);
			if (er.type != 'l') {
				System.out.println("non fatal error");
			} else {
				CodeGen("li $t0  " + token.lexeme);
				String s = genLabel("label125");
				CodeGen("beq  $t0  $0  " + s);
				CodeGen(s + ":");
				match(token.tokennum, 10);
				stmt(token);
			}
		} else
			System.exit(0);
	}

	
	
	/* [15] loopst-> 'while' expression 'do' stats
	 *  token no. 11 -> WHILE token no. 12 -> DO */
	public static void loopst(Token token) {
		System.out.print("15 ");
		if (token.tokennum == 11) {
			match(token.tokennum, 11);
			String s = genLabel("topwhile");
			CodeGen(s);
			ExpressionRecord er = null;
			er = expression(token, er);
			CodeGen("li $t0  " + token.lexeme);
			String endwhile = genLabel("endwhile");
			CodeGen("beq  $t0  $0  " + endwhile);
			CodeGen("j  " + s);
			match(token.tokennum, 12);
			stats(token);
		} else
			System.exit(0);
	}

	
	
	/* [16,17] iost-> 'read' (idref)/ 'write' (expression) token no. 13 -> READ
	 * token no. 20 -> WRITE token no. 14 -> '(' token no. 15 -> ')' WRITE is
	 * assigned token no. 20 because if its also given token no. 13 like READ,
	 * conflict would arise for which one to check for token no. 13 in if..else*/
	
	public static void iost(Token token) {
		ExpressionRecord er = null;
		if (token.tokennum == 13) {
			System.out.print("16 ");
			match(token.tokennum, 13);
			match(token.tokennum, 14);
			idref(token, er);
			if (er.type == 'i') {
				CodeGen("li $v0 5");
				CodeGen("sw $v0 " + er.location);
			}
			match(token.tokennum, 15);
		} else if (token.tokennum == 20) {
			System.out.print("17 ");
			boolean isWriteln = true;
			if (er.type == 'i') {
				CodeGen("li $v0 5");
				CodeGen("sw $v0 " + er.location);
			} else if (er.type == 'l') {

			} else if (er.type == 's') {
				CodeGen("la $a0  " + er.location);
				CodeGen("li ");
				CodeGen("syscall ");

			}
			match(token.tokennum, 20);
			match(token.tokennum, 14);
			er = expression(token, er);
			match(token.tokennum, 15);
		} else
			System.exit(0);
	}

	
	
	// [18] expression->term expprime
	public static ExpressionRecord expression(Token token, ExpressionRecord er) {
		System.out.print("18 ");
		term(token, er);
		expprime(token, er);
		return er;
	}

	
	
	// [19,20] expprime->'+' term expprime/<empty>
	public static void expprime(Token token, ExpressionRecord er) {
		if (token.tokennum == 4) {
			System.out.print("19 ");
			match(token.tokennum, 4);
			term(token, er);
			expprime(token, er);
		} else
			System.out.print("20 ");
	}

	
	
	// [21] term-> relfactor termprime
	public static void term(Token token, ExpressionRecord er) {
		System.out.println("21 ");
		relfactor(token, er);
		termprime(token, er);
	}

	
	
	/* [22,23] termprime-> '*' relfactor termprime/<empty>
	 * token no. 5 -> * */
	public static void termprime(Token token, ExpressionRecord er) {
		if (token.tokennum == 5) {
			System.out.print("22 ");
			match(token.tokennum, 5);
			relfactor(token, er);
			termprime(token, er);
		} else
			System.out.print("23 ");
	}

	
	
	// [24] relfactor-> factor factorprime
	public static void relfactor(Token token, ExpressionRecord er) {
		System.out.print("24 ");
		factor(token, er);
		factorprime(token, er);
	}

	
	
	/* [25,26] factorprime-> RELOPTOK factor/ <empty>
	 * token no. 6 -> < or >*/
	public static void factorprime(Token token, ExpressionRecord er) {
		if (token.tokennum == 6) {
			System.out.print("25 ");
			match(token.tokennum, 6);
			factor(token, er);
		} else
			System.out.print("26 ");
	}

	
	
	/* [27-30] factor-> '!' factor/ idref/ LITTOK/ ( expression )
	 * token no. 17 -> ! token no. 1 -> identifier token no. 2 -> literals
	 * token no. 14 -> ( token no. 15 -> )*/
	public static void factor(Token token, ExpressionRecord er) {
		Info info = new Info();
		if (token.tokennum == 17) {
			System.out.print("27 ");
			match(token.tokennum, 17);
			factor(token, er);
		} else if (token.tokennum == 1) {
			System.out.print("28 ");
			idref(token, er);
		} else if (token.tokennum == 2) {
			System.out.print("29 ");
			if (getType(token.lexeme) == 'i') { // we have an integer literal
												// like 523
				CodeGen("li  $t0   " + token.lexeme);
				CodeGen("sw  $t0  " + info.curr_offset + "($fp)");
				er.type = 'i';
				er.location = info.curr_offset;
				info.curr_offset -= 4;
				match(token.tokennum, 2);
			}

		} else if (token.tokennum == 14) {
			System.out.print("30 ");
			match(token.tokennum, 14);
			expression(token, er);
			match(token.tokennum, 15);
		} else
			System.exit(0);
	}

	
	
	/* [31] idref-> id
	 * token no. 1 -> identifier
	 * if its an identifier, st.find_in_all() is called. 
	 * variable name is stored in 'tokenname' and it scope number is stored
	 * in 'scope'. When variable is used, its details are printed. i.e 
	 * variable name, scope, declaration type and offset. */
	
	public static void idref(Token token, ExpressionRecord er) {
		Info info = new Info();
		info = st.find_in_all(token.lexeme);
		// copy type and location into ex;
		er.type = info.type.charAt(0);
		er.location = info.curr_offset;
		// don’t eat the token until you have info from it
		match(token.tokennum, 1);

		if (token.tokennum == 1) {
			System.out.print("31 ");
			info = st.find_in_all(Token.lexeme);
			String tokenname = info.str;
			int scope = info.scopeNo;
			System.out.println("variable used!");
			System.out.println("variable details: name: " + tokenname
					+ " scope: " + scope + " type: " + info.type
					+ " offset: " + info.curr_offset);
			match(token.tokennum, 1);
		} else
			System.exit(0);

	}

	
	
	/* it checks if current token number matches the expected token number. If
	 * yes, increments the index of the list, gets the next token number */
	public static void match(int tok, int exp) {
		Token token = new Token();
		if (tok == exp) {
			index = index + 1;
			token = otherTokenClasses.gettoken();
		} else
			System.exit(0);
	}

	
	//method returns a labelname
	public static String genLabel(String s){
		return s;
	}
	
	
	//method writes parameter code into output file
	public static void CodeGen(String s) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
						"codefile.txt"), "utf-8"))) {
			writer.write(s);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//method writes prolog to output file
	public static void writeProlog(String str) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
						"codefile.txt"), "utf-8"))) {
			writer.write(" .text");
			writer.write(".globl main");
			writer.write("main:");
			writer.write("move   $fp  $sp     #frame" 
			+ " pointer will be used to access variables");
			writer.write("la $a0 ProgStart   		#next" 
			+ " 3 will print a header");
			writer.write("li $v0 4");
			writer.write("syscall");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	//method writes postlog to output file.
	public static void writePostlog(String str) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(
						"codefile.txt"), "utf-8"))) {
			writer.write(" la $a0 ProgEnd 		#"
					+ "next 3 will print a goodbye");
			writer.write("li $v0 4");
			writer.write("syscall");
			writer.write("li $v0 10	    #10 is program exit");
			writer.write("syscall		#exits the MIPS program");
			writer.write(".data");
			writer.write("	ProgStart: .asciiz	Program Start\n");
			writer.write("ProgEnd:   .asciiz	Program End\n");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//method gets type of token
	public static char getType(String s) {
		// look at a literal token and decide its type
		char ch;
		if (s.charAt(0) == 't' || s.charAt(0) == 'f')
			ch = 'i';
		else if (s.contains("."))
			ch = 'r'; // if we had reals
		else
			ch = 'i';
		return ch;
	}


	/* main class: routines of Scanner are called and all getToken() method of
	 * Scanner is called that brings one token number every time from the list
	 * of token numbers.*/
	public static void main(String arg[]) throws Exception {

		/*
		 * otherTokenClasses and kcheck are objects of classes-OtherTokenlasses
		 * and KeywordTokenClass of Scanner respectively.
		 */
		/* token is object of Token() class of Scanner */
		Token token = new Token();
		OthertokenClasses dfa = new OthertokenClasses();
		KeywordtokenClass kCheck = new KeywordtokenClass();
		int k, f = 0, index = 0;
		String str, temp = "";
		Boolean b;
		// All the lines from the input file are stored in the list.
		List<String> list = Files.readAllLines(
				Paths.get("C:\\Users\\Aneri Patel\\Documents\\CS 4110 "
						+ "Compiler" + "\\Infile.txt"),
				StandardCharsets.UTF_8);

		// String array-arr stores each line for input file at each index.
		String[] arr = list.toArray(new String[list.size()]);

		// String array-sarr stores words of a particular line of the file.
		String[] sarr = null;

		// loop traverses one line of the file at a time.
		for (int i = 0; i < arr.length; i++) {
			// if the line contains comment, its corresponding method is called.
			if (arr[i].contains("COMMENT")) {
				dfa.ifComment(arr[i]);
				i++;
			}
			// if the line contains string literal, its corresponding method is
			// called.
			if (arr[i].contains("\"")) {
				dfa.ifStringLiteral(arr[i]);
				i++;
			}
			sarr = arr[i].split("\\s+"); // splits a line into words.
			// loop traverses each word of line.
			loop: for (int j = 0; j < sarr.length; j++) {
				System.out.println(sarr[j]);
				b = kCheck.checkKeyword(sarr[j].toLowerCase());

				if (!b) {
					str = dfa.idStartingWithKeyword(sarr[j].toLowerCase());
					dfa.findOthertokens(str);
				}
			}
		}

		/* gettoken() returns object of token. It contains token number, its
		 * name */
		token = otherTokenClasses.gettoken();
		lexeme = token.lexeme;
		tok = token.tokennum;
		program(token);
		// token no. -1 -> end of file
		if (token.tokennum != -1) {
			System.out.println("fail");
		} else
			System.out.println("success");
	}
}
