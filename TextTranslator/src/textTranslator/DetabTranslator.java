package textTranslator;


/**
 * Detab Translator class
 * Replaces tabs at the beginning of each line. Each tab is replaced by four spaces. 
 * Tabs within the line (that is, after the first non-tab character) are not replaced.
 * @author Zhou Tan
 *
 */
public class DetabTranslator implements TranslatorInterface {
    
    public String getName() {
        return "Detab";
    }
    
    public String getDescription() {
        return "Replaces tabs at the beginning of each line. Each tab is replaced by four spaces.";
    }
    
    public String translate(String text) {
        final String FOURSPACES = "    ";
        int tabCount = 0;
        
        //record how many consecutive tabs at the beginning of the text
        while (text.startsWith("\t", tabCount++));
        tabCount--;
        
        //convert each tab into four spaces
        String detabbedText = text.substring(tabCount);
        for (int i = 0; i < tabCount; i++) {
            detabbedText = FOURSPACES + detabbedText;
        }
        return detabbedText;
    }

}
