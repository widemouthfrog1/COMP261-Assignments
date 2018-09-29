package code.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;

import nodes.*;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern ACTION = Pattern.compile("move|turnL|turnR|takeFuel|wait|shieldOn|shieldOff|turnAround");
	static Pattern RELOP = Pattern.compile("gt|lt|eq");
	static Pattern INFIXRELOP = Pattern.compile(">|<|=");
	static Pattern SENSOR = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern OP = Pattern.compile("add|sub|mul|div");
	static Pattern INFIXOP = Pattern.compile("+|-|*|/");
	static Pattern EXPROP = Pattern.compile("and|or|not");
	static Pattern INFIXEXPROP = Pattern.compile("&&|\\|\\||!");
	static Pattern VARPAT = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");
	
	
	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		Map<String, VariableNode> variables = new HashMap<String, VariableNode>();
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		//STMT ::= ACT ";" | LOOP
		while(s.hasNext()) {
			statements.add(Parser.parseStatement(s, variables));
		}
		RobotProgramNode program = new ProgramNode(statements);
		return program;
	}
	
	public static StatementNode parseStatement(Scanner s, Map<String, VariableNode> variables) {
		StatementNode statement = null;
		if(Parser.checkFor("loop", s)) {
			statement = new StatementNode(new LoopNode(Parser.parseBlock(s, variables)));
		}else if(Parser.checkFor("if", s)){
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ConditionNode condition = Parser.parseCondition(s, variables);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			BlockNode block = Parser.parseBlock(s, variables);
			ArrayList<ConditionNode> conditions = new ArrayList<ConditionNode>();
			ArrayList<BlockNode> blocks = new ArrayList<BlockNode>();
			while(Parser.checkFor("elif", s)) {
				Parser.require(OPENPAREN, "\"(\" expected", s);
				conditions.add(Parser.parseCondition(s, variables));
				Parser.require(CLOSEPAREN, "\")\" expected", s);
				blocks.add(Parser.parseBlock(s, variables));
			}
			
			if(Parser.checkFor("else", s)) {
				statement = new StatementNode(new IfNode(condition, block, conditions, blocks, Parser.parseBlock(s, variables)));
			}else {
				statement = new StatementNode(new IfNode(condition, block, conditions, blocks));
			}
		}else if(Parser.checkFor("while", s)){
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ConditionNode condition = Parser.parseCondition(s, variables);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			BlockNode block = Parser.parseBlock(s, variables);
			statement = new StatementNode(new WhileNode(condition, block));
		}else if(s.hasNext(VARPAT)){
			String variable = s.next();
			Parser.require("=", "\"=\" expected", s);
			ExpressionNode expression = Parser.parseExpression(s, variables);
			AssignmentNode assignment;
			if(variables.containsKey(variable)) {
				assignment = new AssignmentNode(variables.get(variable), expression);
			}else {
				variables.put(variable, new VariableNode(variable));
				assignment = new AssignmentNode(variables.get(variable), expression);
			}
			statement = new StatementNode(assignment);
			Parser.require(";", "Semicolon expected", s);
		}else{
			String action = Parser.require(ACTION, "Action expected", s);
			ActionNode node;
			if(Parser.checkFor(OPENPAREN, s)) {
				node = new ActionNode(action, Parser.parseExpression(s, variables));
				Parser.require(CLOSEPAREN, "\")\" expected", s);
			}else {
				node = new ActionNode(action);
			}
			Parser.require(";", "Semicolon expected", s);
			statement = new StatementNode(node);
		}
		return statement;
	}
	
	public static BlockNode parseBlock(Scanner s, Map<String, VariableNode> variables) {
		ArrayList<StatementNode> blockStatements = new ArrayList<StatementNode>();
		Parser.require(OPENBRACE, "\"{\" expected", s);
		while(!Parser.checkFor(CLOSEBRACE, s)) {
			blockStatements.add(Parser.parseStatement(s, variables));
		}
		return new BlockNode(blockStatements);
	}
	
	public static ConditionNode parseCondition(Scanner s, Map<String, VariableNode> variables) {
		if(s.hasNext(EXPROP)) {
			String operator = s.next();
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ConditionNode condition1 = Parser.parseCondition(s, variables);
			if(Parser.checkFor(CLOSEPAREN, s)) {
				return new ConditionNode(operator, condition1);
			}
			Parser.require(",", "\",\" expected", s);
			ConditionNode condition2 = Parser.parseCondition(s, variables);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			return new ConditionNode(operator, condition1, condition2);
		}
		String name = Parser.require(RELOP, "Relational operator expected", s);
		Parser.require(OPENPAREN, "\"(\" expected", s);
		ExpressionNode expression1 = Parser.parseExpression(s, variables);
		Parser.require(",", "\",\" expected", s);
		ExpressionNode expression2 = Parser.parseExpression(s, variables);
		Parser.require(CLOSEPAREN, "\")\" expected", s);
		return new ConditionNode(name, expression1, expression2);
	}
	
	public static ExpressionNode parseExpression(Scanner s, Map<String, VariableNode> variables) {
		ExpressionNode node = null;
		if(s.hasNext(NUMPAT)) {
			node = new ExpressionNode(s.nextInt());
		}else if(s.hasNext(SENSOR)) {
			node = new ExpressionNode(Parser.parseSensor(s, variables));
		}else if(s.hasNext(VARPAT)) {
			String variable = s.next();
			if(variables.containsKey(variable)) {
				node = new ExpressionNode(variables.get(variable));
			}else {
				variables.put(variable, new VariableNode(variable));
				node = new ExpressionNode(variables.get(variable));
			}
		}else if(s.hasNext(OP)) {
			String operation = s.next();
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ExpressionNode expression1 = Parser.parseExpression(s, variables);
			Parser.require(",", "Comma expected", s);
			ExpressionNode expression2 = Parser.parseExpression(s, variables);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			node = new ExpressionNode(operation, expression1, expression2);
		}else {
			fail("Expression expected", s);
		}
		return node;
	}
	
	public static SensorNode parseSensor(Scanner s, Map<String, VariableNode> variables) {
		String sensor = s.next();
		if(Parser.checkFor(OPENPAREN, s)) {
			ExpressionNode expression = Parser.parseExpression(s, variables);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			return new SensorNode(sensor, expression);
		}
		return new SensorNode(sensor, null);
	}

	// utility methods for the parser
	
	

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}
	
	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}
	
	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)
