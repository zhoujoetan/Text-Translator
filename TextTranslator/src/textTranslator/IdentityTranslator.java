package textTranslator;

/**
 * The Identity Translator class.
 * Makes no changes to the input
 * @author Zhou Tan
 *
 */
public class IdentityTranslator implements TranslatorInterface {
    public String getName() {
        return "Identity";
    }
    
    public String getDescription() {
        return "Makes no changes to the input.";
    }
    
    public String translate(String text) {
        //For identity, just return the raw text!
        return text;
    }
}
