
import java.util.*;

public class ExpressionParser
{
    private static final int LEFT_ASSOC  = 0;
    private static final int RIGHT_ASSOC = 1;

    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static
    {
        OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
        OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
    }

    private static boolean isOperator(String token)
    {
        return OPERATORS.containsKey(token);
    }

    private static boolean isAssociative(String token, int type)
    {
        if (!isOperator(token))
        {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    private static final int cmpPrecedence(String token1, String token2)
    {
        if (!isOperator(token1) || !isOperator(token2))
        {
            throw new IllegalArgumentException("Invalid tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }


    public static String[] infixToRPN(String[] inputTokens)
    {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();


        for (String token : inputTokens)
        {

            if (isOperator(token))
            {

                while (!stack.empty() && isOperator(stack.peek()))
                {
                    if ((isAssociative(token, LEFT_ASSOC)         &&
                            cmpPrecedence(token, stack.peek()) <= 0) ||
                            (isAssociative(token, RIGHT_ASSOC)        &&
                                    cmpPrecedence(token, stack.peek()) < 0))
                    {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }

                stack.push(token);
            }
            else if (token.equals("("))
            {
                stack.push(token);  //
            }
            else if (token.equals(")"))
            {
                while (!stack.empty() && !stack.peek().equals("("))
                {
                    out.add(stack.pop());
                }
                stack.pop();
            }
            else
            {
                out.add(token);
            }
        }
        while (!stack.empty())
        {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static double RPNtoDouble(String[] tokens)
    {
        Stack<String> stack = new Stack<String>();

        // For each token
        for (String token : tokens)
        {
            // If the token is a value push it onto the stack
            if (!isOperator(token))
            {
                stack.push(token);
            }
            else
            {
                // Token is an operator: pop top two entries
                Double d2 = Double.valueOf( stack.pop() );
                Double d1 = Double.valueOf( stack.pop() );

                //Get the result
                Double result = token.compareTo("+") == 0 ? d1 + d2 :
                        token.compareTo("-") == 0 ? d1 - d2 :
                                token.compareTo("*") == 0 ? d1 * d2 :
                                        d1 / d2;

                // Push result onto stack
                stack.push( String.valueOf( result ));
            }
        }

        return Double.valueOf(stack.pop());
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String abc = sc.nextLine();
        String[] input = abc.split(" ");
        String[] output = infixToRPN(input);

        // Build output RPN string minus the commas
        for (String token : output) {
            System.out.print(token + " ");
        }
        System.out.println("");
        // Feed the RPN string to RPNtoDouble to give result
        Double result = RPNtoDouble( output );
        System.out.println(result);
    }
}