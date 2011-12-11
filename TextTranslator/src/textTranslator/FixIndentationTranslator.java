package textTranslator;

import java.util.Stack;

/**
 * The Fix Indentation class
 * Given a Java (or Java-like) program, indent it according to the following rules:
 * For each opening brace, {, indent the next line by 4 spaces. (If two open braces, indent the next line by 8, etc.)
 * For each closing brace, }, indent this line by 4 fewer spaces than the previous line; but don't indent less than zero.
 * If a line contains an opening brace and a later closing brace, {...}, they cancel each other out. However, 
 * if a closing brace comes before an opening brace, }...{, they do not cancel each other out; 
 * both have the effects described above.
 * @author Zhou Tan
 *
 */
public class FixIndentationTranslator implements TranslatorInterface {
    public String getName() {
        return "Fix Indentation";
    }
    
    public String getDescription() {
        return "Adds indentation to Java-like code segment.";
    }
    
    public String translate(String text) {
        String detabbedText = text.replaceAll("\\t", "    ");
        
        //get the indentation of the first line
        int firstIndex = 0;
        String indentSpaces = "";
        for (int i = 0; i < detabbedText.length(); i++) {
            if (detabbedText.charAt(i) != ' ' && detabbedText.charAt(i) != '\n') {
                firstIndex = i;
                break;
            }
        }
        //find all leading spaces before this character
        for (int i = firstIndex - 1; i >= 0; i--) {
            if (detabbedText.charAt(i) == ' ') {
                indentSpaces += " ";
            }
            else {
                break;
            }
        }
        
        String[] lines = detabbedText.substring(firstIndex).split("\n");
        Stack<Character> braceStack = new Stack<Character>();
        String currentIndentLevel = "";
        String oneLevelIndent = "    ";
        String translation = "";
        String nextIndentLevel = "";
        int negIndentation = 0;
        int minStackSize = 0;
        for (String line : lines) {
            minStackSize = braceStack.size();
            for (int i = 0; i < line.length(); i++) {
                //push a open brace into the stack
                if (line.charAt(i) == '{') {
                    //if there is a negative indentation, increment it by 1 and do not indent
                    if (negIndentation < 0) {
                        negIndentation++;
                    }
                    else {
                        braceStack.push(new Character('{'));
                    }
                }
                else if (line.charAt(i) == '}') {
                    //if stack is not empty, it means there exists open braces, pop it 
                    if (!braceStack.empty()) {
                        Character brace = braceStack.pop();
                        assert(brace.charValue() == '{');
                    }
                    else {
                        //if closing brace exceeds open braces, keep track of the count
                        negIndentation--;
                    }
                    //keep track of the minimum stack size during current line
                    if (minStackSize > braceStack.size()) {
                        minStackSize = braceStack.size();
                    }
                }
            }
            
            if (nextIndentLevel.length() > minStackSize) {
                //if min stack size is less than the set up indentation then dedent the current line
                currentIndentLevel = "";
                for (int j = 0; j < minStackSize; j++) {
                    currentIndentLevel += oneLevelIndent;
                }
            }
            else {
                //if this line should not be dedented, keep the indentation level unchanged
                currentIndentLevel = nextIndentLevel;
            }
            //add current line with proper indentation to translation text
            translation += indentSpaces + currentIndentLevel + line.substring(findFirstNonSpace(line)) + "\n";
            
            //set next line indentation level
            nextIndentLevel = "";
            for (int j = 0; j < Math.max(braceStack.size() - negIndentation, 0); j++) {
                nextIndentLevel += oneLevelIndent;
            }
        }
        return translation.substring(0, translation.length() - 1);
    }
    
    /**
     * Search for the first non-space character
     * @param s: the text being searched
     * @return the index of first non-space character
     */
    private int findFirstNonSpace(String s) {
        int i = 0;
        if (s.equals(""))
            return 0;
        while (s.charAt(i++) == ' ' && i < s.length());
        if (i == s.length() && s.charAt(s.length() - 1) == ' ')
            return s.length();
        return --i;
    }
}
