package textTranslator;

/**
 * The translator interface
 * @author Zhou Tan
 *
 */
interface TranslatorInterface {
    /**
     * @return Translator's name
     */
    String getName();
    
    /**
     * @return Translator's description
     */
    String getDescription();
    
    /**
     * Convert the raw text to translated text
     * @param text: raw text being translated
     * @return translated text
     */
    String translate(String text);

}
