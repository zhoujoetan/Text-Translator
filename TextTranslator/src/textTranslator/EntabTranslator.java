package textTranslator;

/**
 * The Entab Translator class.
 * Replaces each group of four spaces at the beginning of each line with a tab character.
 * @author Zhou Tan
 *
 */
public class EntabTranslator implements TranslatorInterface {
    public String getName() {
        return "Entab";
    }
    
    public String getDescription() {
        return "Replaces each group of four spaces at the beginning of each line with a tab character.";
    }
    
    public String translate(String text) {
        final String FOURSPACES = "    ";
        int spaceCount = 0;
        
        //record how many consecutive tabs at the beginning of the text
        while (text.startsWith(FOURSPACES, (spaceCount++ * FOURSPACES.length())));
        spaceCount--;
        
        //convert each tab into four spaces
        String entabbedText = text.substring(spaceCount * FOURSPACES.length());
        for (int i = 0; i < spaceCount; i++) {
            entabbedText = '\t' + entabbedText;
        }
        return entabbedText;
    }
}
