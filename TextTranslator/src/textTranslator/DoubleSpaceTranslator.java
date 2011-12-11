package textTranslator;

/**
 * Double Space Translator
 * Replaces each newline in the input with two newlines
 * @author Zhou Tan
 *
 */
public class DoubleSpaceTranslator implements TranslatorInterface {
    
    public String getName() {
        return "Double Space";
    }
    
    public String getDescription() {
        return "Replaces each newline in the input with two newlines.";
    }
    
    public String translate(String text) {
        return text.replaceAll("\n", "\n\n");
    }

}
