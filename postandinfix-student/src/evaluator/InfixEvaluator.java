package evaluator;

import parser.ArithParser;
import stack.LinkedStack;

public class InfixEvaluator extends Evaluator {

  private LinkedStack<String> operators = new LinkedStack<String>();
  private LinkedStack<Integer> operands  = new LinkedStack<Integer>();

  /** return stack object (for testing purpose). */
  public LinkedStack<String> getOperatorStack() { 
    return operators; 
  }
  
  public LinkedStack<Integer> getOperandStack() { 
    return operands;
  }


  /** This method performs one step of evaluation of a infix expression.
   *  The input is a token. Follow the infix evaluation algorithm
   *  to implement this method. If the expression is invalid, throw an
   *  exception with the corresponding exception message.
   */
  public void evaluate_step(String token) throws Exception {
	if (isOperand(token)) {
	   operands.push(Integer.parseInt(token));
	}
    else {
    	if(token.equals("(")) {
    		operators.push(token);
    	}
    	else if(token.equals("+") || token.equals("-") || token.equals("/") || token.equals("*") || token.equals("!")) {
    		if(operators.top() == null) operators.push(token);
    		else {
    			if(Evaluator.precedence(token) > Evaluator.precedence(operators.top())) {
    				operators.push(token);
    			}
    			else {
    				while(true) {
    					String next = operators.top();
    					if(next == null || Evaluator.precedence(next) < Evaluator.precedence(token)) break;
    					process_operator(operators.pop());
    				}
    			    operators.push(token);
    			}
    		}
    	}
    	else if (token.equals(")")) {
    		while (operators.top() != null) {
    			if(operators.top().equals("("))break;
    			process_operator(operators.pop());
    			
    		}
    		if(operators.top() == null) {
    			throw new Exception("missing (");
    		}
    		else {
    			operators.pop();
    		}
    	}
    	else {
    		throw new Exception("invalid operator");
    	}
    }
  }
  
  
  public void process_operator(String token) throws Exception {
	  if(operands.top() == null) {
	     throw new Exception("too few operands");
	  }
	  int op2 = operands.pop();
	      
	  if(token.equals("!")) {
         operands.push(-1*op2);	  
      }
      else if(token.equals("+") || token.equals("-") || token.equals("/") || token.equals("*")) {
	         
	    if(operands.top() == null) {
	       throw new Exception("too few operands");
	    }
	    int op1 = operands.pop();
	         
	    if(token.equals("+")) 
	    	operands.push(op1 + op2);
	    else if(token.equals("-"))
	       	operands.push(op1 - op2);
	    else if(token.equals("*")) 
	        operands.push(op1 * op2);
	    else if(token.equals("/")) {
	        if(op2 == 0) {
	           throw new Exception("division by zero"); 
	        }
	        operands.push(op1 / op2);
	    }  
	  }
      else {
    	  throw new Exception("invalid operator");
      }
  }
  
  /** This method evaluates an infix expression (defined by expr)
   *  and returns the evaluation result. It throws an Exception object
   *  if the infix expression is invalid.
   */
  public Integer evaluate(String expr) throws Exception {

    for (String token : ArithParser.parse(expr)) {
      evaluate_step(token);
    }

    while(operators.top() != null) {
    	process_operator(operators.pop());
    }

    // The operand stack should have exactly one operand after the evaluation is done
    if (operands.size() > 1) {
      throw new Exception("too many operands");
    } else if (operands.size() < 1) {
      throw new Exception("too few operands");
    }

    return operands.pop();
  }

  public static void main(String[] args) throws Exception {
    System.out.println(new InfixEvaluator().evaluate("5+(5+2*(5+9))!/8"));
  }
}
