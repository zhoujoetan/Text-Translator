package textTranslator;

/**
 * The Single Space Translator class
 * Replaces consecutive pairs of newlines with a single newline.
 * @author Zhou Tan
 *
 */
public class SingleSpaceTranslator implements TranslatorInterface {
    public String getName() {
        return "Single Space";
    }
    
    public String getDescription() {
        return "Replaces consecutive pairs of newlines with a single newline.";
    }
    
    public String translate(String text) {
        return text.replaceAll("\n\n", "\n");
    }
}
