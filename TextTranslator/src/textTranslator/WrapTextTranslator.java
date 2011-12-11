package textTranslator;

/**
 * The Wrap Text Translator
 * Wraps lines so that no line is longer than 72 characters. 
 * Long lines should be broken at a space character, 
 * with the remainder put on a new line with the same indentation as the original. 
 * Very long lines will result in several shorter lines. 
 * If a line contains more than 72 consecutive non-space characters (as often happens with a URL), 
 * the long "word" is not broken, but is put on a line by itself. 
 * This method does not join lines, it only wraps long lines.
 * @author Zhou Tan
 *
 */
public class WrapTextTranslator implements TranslatorInterface {
    public String getName() {
        return "Wrap Text";
    }
    
    public String getDescription() {
        return "Wraps lines so that no line is longer than 72 characters.";
    }
    
    public String translate(String text) {
        final int LINELENGTH = 72;
        int oldIndex = 0;
        int nextIndex = 0;
        int nextNewLine = 0;
        int indentation = 0;
        boolean isNewLine = false;
        String translation = "";
        String indentSpaces = "";
        
        String detabbedText = text.replaceAll("\\t", "    ");
        
        while (detabbedText.substring(oldIndex).length() > LINELENGTH - indentation) {
            //if there is a new line within 72 characters, no change should be made
            while ((nextNewLine = findNextNewLine(detabbedText, oldIndex, LINELENGTH)) != -1) {
                //if there is a new line, copy what's before it
                translation += detabbedText.substring(oldIndex, nextNewLine + 1);
                oldIndex = nextNewLine + 1;
                isNewLine = true;
            }
            
            if (isNewLine) {
                //if it there is a new line, reset the indentations
                isNewLine = false;
                indentation = 0;
                indentSpaces = "";
                if (oldIndex >= detabbedText.length() - LINELENGTH) {
                break;
                }
            }
            
            //find indentation for current line
            while (detabbedText.charAt(oldIndex++) == ' ') {
                indentation++;
            }
            oldIndex--;
            
            //indentation string
            if (indentSpaces.equals("")) {
                for (int j = 0; j < indentation; j++) {
                    indentSpaces += " ";
                }
            }
            
            translation += indentSpaces; 
            nextIndex = findPreviousSpace(detabbedText, oldIndex + LINELENGTH - indentation, LINELENGTH - indentation);
            
            //if this line is too long, then put it on a separate line
            if (nextIndex == -1) {
                //no space within a line, search forwards for the next space or new line
                nextIndex = findNextSpaceOrNewLine(detabbedText, oldIndex);
                //there is no match
                if (nextIndex == detabbedText.length())
                {
                    break;
                }
            }
            
            //add this line to translation
            translation += detabbedText.substring(oldIndex, nextIndex) + "\n";
            
            if (detabbedText.charAt(nextIndex) == '\n') {
                //if find a new line, then reset indentation
                indentation = 0;
                indentSpaces = "";
            }
            
            //set the index of first character in next line
            oldIndex = nextIndex + 1;
            if (oldIndex >= detabbedText.length() - LINELENGTH) {
                break;
            }
            //ignore all consecutive spaces
            while (detabbedText.charAt(oldIndex++) == ' ');
            oldIndex--;
        }
        
        //add the last part of the text
        translation += indentSpaces + detabbedText.substring(oldIndex);
        return translation;
    }
    
    /**
     * Search backwards to find the first space. 
     * If there are multiple consecutive spaces, find the leftmost one.
     * @param text: the text that is being searched
     * @param index: starting index
     * @param length: search length
     * @return: index of the first leftmost space that is found (if no qualified space is found, return -1)
     */
    private int findPreviousSpace(String text, int index, int length) {
        //
        for (int i = Math.min(index, text.length() - 1); i > index - length; i--) {
            if (text.charAt(i) == ' ') {
                if ((i == index - length + 1) || (text.charAt(i - 1) != ' '))
                    return i;
            }
        }
        //if there is no space, return -1
        return -1;
    }
    
    /**
     * Search forwards to find the first space or new line character
     * @param text: the text being searched
     * @param index: the starting index
     * @return the found index of space or new line
     */
    private int findNextSpaceOrNewLine(String text, int index) {
        for (int i = index; i < text.length(); i++) {
            if (text.charAt(i) == ' ' || text.charAt(i) == '\n')
                return i;
        }
        //if there is no space, return the length of the text 
        return text.length();
    }
    
    /**
     * Search for next new line character in the given range in the text
     * @param text: the text being searched
     * @param index: the starting index
     * @param length: the searching range
     * @return the index that is found
     */
    private int findNextNewLine(String text, int index, int length) {
        for (int i = index; i < Math.min(index + length, text.length()); i++) {
            //return the index that the new line is at
            if (text.charAt(i) == '\n')
                return i;
        }
        //if no new line found, return -1
        return -1;
    }
}
