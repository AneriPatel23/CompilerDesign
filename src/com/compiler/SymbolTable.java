package com.compiler;

import java.io.BufferedReader;
import java.io.File; //class inside this package handles input output operations
import java.io.FileReader;
import java.io.IOException; //class inside this package handles exceptions in input and output data streams
import java.util.HashMap; //class inside this package provides all the optional map operations
import java.util.Map; //class inside this package provides collection views for key-value
import java.util.Scanner; //class inside this package parses primitive types and strings
import java.util.Set; //class inside this package provides collection of values in a set with no duplicates allowed
import java.util.Stack; //class inside this package handles a LIFO set of objects 


/* This class performs all the operations used to construct a symbol table. Data structure Hashmap is used for
 storing all the symbols and their respective scopes as key-value pairs. A stack is maintained to handle opening
 and closing of scopes. Variable 'scope' stores current scope. Variable 'top' retrieves top value in the stack. */
public class SymbolTable {
	Info info=new Info();
	HashMap<Integer, Info> HMap = new HashMap<>();
	int scope = -1, top;
	Stack stack = new Stack();

	/*
	 * This method increments value of scope if { is encountered, pushes that
	 * value into the stack and inserts it into Hashmap.
	 */
	public void openscope() {
		scope = scope + 1;
		stack.push(scope);
		top = (Integer) (stack.peek());
		HMap.put(top, info);
	}
	
	

	// This method pops top value of stack when } is encountered.
	public void closed_a_scope() {
		Set<Integer> keys = HMap.keySet();
		top = (Integer) (stack.peek());
		for (Integer j : keys) {
			if (j == top) {
				// System.out.println();
			}
		}
		stack.pop();
	}
	
	

	/*
	 * This method finds if the current symbol is already present in some other scope.
	 * If yes,it returns scope number, current symbol and boolean value true.*/
	
	public Info find_in_all(String s) {
		
		Set<Integer> keys = HMap.keySet();
		Set<Map.Entry<Integer, Info>> entry = HMap.entrySet();
		for (Map.Entry<Integer, Info> e : entry) {
			if ((e.getValue()).equals(s)) {
				info.scopeNo = e.getKey();
				info.str=s;
			} 
		}
		return info;

	}
	
	

	/*
	 * This method checks if the symbol already exists in the current scope and
	 * does not allow duplicate symbols in the same scope. If the symbol is not
	 * present in current scope it checks it in other scopes. If its nor present
	 * there too, then it inserts symbol into the hashmap.
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
	public void insert(Info info) {
		HMap.put(top, info);
	}
}