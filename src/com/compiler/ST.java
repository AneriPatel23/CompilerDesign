package com.compiler;

import java.io.BufferedReader;
import java.io.File; //this package handles I/O operations
import java.io.FileReader;//package reads character files
import java.io.IOException; //package handles exceptions in I/O data streams
import java.util.HashMap; //package provides all the optional map operations
import java.util.Map; //package provides collection views for key-value
import java.util.Scanner; //package parses primitive types and strings
import java.util.Set;//package provides collection of values in set without duplicates
import java.util.Stack; //package handles a LIFO set of objects 

/* This class performs all the operations used to construct a symbol table.
 *  Data structure Hashmap is used for storing all the symbols and their respective
 *  scopes as key-value pairs. A stack is maintained to handle opening and closing
 *  of scopes. Variable 'scope' stores current scope. Variable 'top' retrieves top
 *  value in the stack. */
class SymbolTableFunctioning {
	
	//stores scope number and symbol as key value pair.
	HashMap<Integer, String> HMap = new HashMap<>();
	
	/*top is the top of the stack that stores the active scopes. 
	 * i.e it stores current scope.*/
	int scope = -1, top;
	
	//stack stores all the active scopes.
	Stack stack = new Stack();

	/*
	 * This method increments value of scope if { is encountered, pushes that
	 * value into the stack and inserts it into Hashmap.
	 */
	public void opens_a_scope() {
		scope = scope + 1;
		stack.push(scope);
		top = (Integer) (stack.peek());
		HMap.put(top, "{");
	}

	// This method pops top value of stack when } is encountered.
	public void closed_a_scope() {
		Set<Integer> keys = HMap.keySet();
		top = (Integer) (stack.peek());
		for (Integer j : keys) {
			if (j == top) {
				continue;
			}
		}
		stack.pop();
	}

	/*
	 * If the symbol is not present in current scope it checks it
	 * in other active scopes. If its present in any if the active scope,
	 *  it returns the value of the scope. If not, it returns -1.
	 */
	public int find_in_all(String s) {
		//k stores active scope value.
		int k=0,check=0;
		
		Set<Integer> keys = HMap.keySet();
		Set<Map.Entry<Integer, String>> entry = HMap.entrySet();
		for (Map.Entry<Integer, String> e : entry) {
			if ((e.getValue()).equals(s)) {
				k = e.getKey();
				check = 1;
			}
		}
		if (check == 1)
			return k;
		else{
			k=-1;
			return k;
		}
	}

	/*
	 * This method checks if the symbol already exists in the current scope and
	 * does not allow duplicate symbols in the same scope. If symbol is already
	 * present in current scope, it returns true, else returns false.
	 */
	public boolean find_in_local(String s) {
		int flag = 0;
		top = (Integer) (stack.peek());
		Set<Integer> keys = HMap.keySet();
		for (Integer j : keys) {
			if (j == top) {
				if (HMap.get(j).equals(s)) {
					flag = 1;

				} else {
					flag = 0;
				}
			}
		}
		if (flag == 1)
			return true;
		else
			return false;
	}

	// This method inserts symbol into hashmap.
	public void insert(String s) {
		HMap.put(top, s);
	}

	// This method displays the symbol with its scope.
	public void display() {
		System.out.println("scope:" + top + "  " + HMap.get(top));
	}
}

public class ST {

	public static void main(String args[]) throws Exception {

		SymbolTableFunctioning st = new SymbolTableFunctioning();
		BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\Aneri Patel\\Documents\\"
						+ "CS 4110 Compiler\\samplefile.txt"));

		StringBuilder sb = new StringBuilder();
		String line = br.readLine().toLowerCase();

		while (line!= null) {
			sb.append(line);
			sb.append("\n");
			line = br.readLine();
		}

		br.close();

		// reads data from text file.
		String text = sb.toString();
		String[] stringArr = text.split("\\s+"); // stores all symbols in string
		boolean a; 
		int x=0;
		
        //takes each symbol one by one.
		for (int i = 0; i < stringArr.length; i++) {
			
			//if symbol is { , opens_a_scope() is called.
			if (stringArr[i].equals("{")) {
				st.opens_a_scope();
		    //if symbol is } , closed_a_scope() is called.
			} else if (stringArr[i].equals("}")) {
				st.closed_a_scope();
				
			//if its a blank space	
			} else if (stringArr[i].equals("")) {
			}
			/*if its any variable, symbol is checked in find_in_local().
			 * If that return true, statement is printed, else find_in_all()
			 * is called. symbol is inserted and displayed with its scope.*/
			
			else {
				a = st.find_in_local(stringArr[i]);
				if (a) {
					System.out.println("'" + stringArr[i] + "'" + "  already exists in current scope!");
				} else
					x = st.find_in_all(stringArr[i]);
				st.insert(stringArr[i]);
				st.display();
				if (x!=-1) {
					System.out.println("'" + stringArr[i] + "'" + " is present in other scope: "+x);
				}
			}
		}
	}
}
