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
	static Pattern SENSOR = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		//STMT ::= ACT ";" | LOOP
		while(s.hasNext()) {
			statements.add(Parser.makeStatement(s));
		}
		RobotProgramNode program = new ProgramNode(statements);
		return program;
	}
	
	public static StatementNode makeStatement(Scanner s) {
		StatementNode statement = null;
		if(Parser.checkFor("loop", s)) {
			statement = new StatementNode(new LoopNode(Parser.makeBlock(s)));
		}else if(Parser.checkFor("if", s)){
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ConditionNode condition = Parser.makeCondition(s);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			BlockNode block = Parser.makeBlock(s);
			statement = new StatementNode(new IfNode(condition, block));
		}else if(Parser.checkFor("while", s)){
			Parser.require(OPENPAREN, "\"(\" expected", s);
			ConditionNode condition = Parser.makeCondition(s);
			Parser.require(CLOSEPAREN, "\")\" expected", s);
			BlockNode block = Parser.makeBlock(s);
			statement = new StatementNode(new WhileNode(condition, block));
		}else{
			ActionNode action = new ActionNode(Parser.require(ACTION, "Action expected", s));
			Parser.require(";", "Semicolon expected", s);
			statement = new StatementNode(action);
		}
		return statement;
	}
	
	public static BlockNode makeBlock(Scanner s) {
		ArrayList<StatementNode> blockStatements = new ArrayList<StatementNode>();
		Parser.require(OPENBRACE, "\"{\" expected", s);
		while(!Parser.checkFor(CLOSEBRACE, s)) {
			blockStatements.add(Parser.makeStatement(s));
		}
		return new BlockNode(blockStatements);
	}
	
	public static ConditionNode makeCondition(Scanner s) {
		String name = Parser.require(RELOP, "Relational operator expected", s);
		Parser.require(OPENPAREN, "\"(\" expected", s);
		SensorNode sensor = new SensorNode(Parser.require(SENSOR, "Sensor expected", s));
		Parser.require(",", "\",\" expected", s);
		int number = Integer.parseInt(Parser.require(NUMPAT, "Number expected", s));
		Parser.require(CLOSEPAREN, "\")\" expected", s);
		return new ConditionNode(name, sensor, number);
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
