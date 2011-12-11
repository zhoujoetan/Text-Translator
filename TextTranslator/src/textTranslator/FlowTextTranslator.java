package textTranslator;

/**
 * The Flow Text Translator class
 * Flows text (the way web pages do). Consecutive nonblank lines should be joined (with a space in between), 
 * then wrapped to 72 characters, as in Wrap Text above. 
 * Blank lines separate paragraphs, and paragraphs are never joined.
 * @author Zhou Tan
 *
 */
public class FlowTextTranslator implements TranslatorInterface {
    public String getName() {
        return "Flow Text";
    }
    
    public String getDescription() {
        return "Flows text.";
    }
    
    public String translate(String text) {
        String detabbedText = text.replaceAll("\t", "    ");
        
        //eliminate all spaces within the blank lines
        String despacedText = detabbedText.replaceAll("\\n[ ]*\\n", "\n\n");
        
        //replace the new line with space meanwhile keeping the blank lines
        int oldIndex = 0;
        String translation = "";
        for (int i = 1; i < despacedText.length() - 1; i++) {
            if (despacedText.charAt(i) == '\n' && despacedText.charAt(i - 1) != '\n' &&
                despacedText.charAt(i + 1) != '\n') {
                translation += despacedText.substring(oldIndex, i) + " ";
                oldIndex = i + 1;
            }
        }
        //there is a tail string left out
        translation += despacedText.substring(oldIndex);
        
        //after the join, perform the wrap text function
        return (new WrapTextTranslator().translate(translation));
    }
}
