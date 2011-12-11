package textTranslator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.Border;

/**A simple class that puts the raw text in the upper text area,
 * and the translated text is put to the lower text area, and
 * users get to choose from various kinds of translators.
 * @author Zhou Tan
 *
 */
public class TextTranslator extends JFrame {
    JLabel description = new JLabel("Welcome to the text translator!", JLabel.LEFT);
    JMenuBar menuBar = new JMenuBar();  
    JMenu fileMenu = new JMenu("File");
    JMenu translateMenu = new JMenu("Translate");
    JMenuItem load = new JMenuItem("Load...");
    JMenuItem saveAs = new JMenuItem("Save As...");
    JMenuItem quit = new JMenuItem("Quit");
    JTextArea text = new JTextArea("");
    JTextArea translatedText = new JTextArea("");
    JButton translate = new JButton("Translate");
    String currentTranslator;
    
    /**Main function, create a instance of this class
     * @param args
     */
    public static void main(String[] args) {
        new TextTranslator().createGUI();
    }
    
    /**
     * The gui creation function
     */
    public void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create panels for all components
        JPanel descriptionPanel = new JPanel();
        JScrollPane rawTextPanel = new JScrollPane(text);
        JScrollPane translatedTextPanel = new JScrollPane(translatedText);
        JPanel buttonPanel = new JPanel();
        
        //description bar
        descriptionPanel.setLayout(new GridLayout(1, 1));
        descriptionPanel.setBackground(new Color(253, 243, 162));
        descriptionPanel.add(description);
        description.setForeground(Color.blue);
        descriptionPanel.setPreferredSize(new Dimension(600, 30));
        
        //raw text area
        rawTextPanel.setPreferredSize(new Dimension(600, 200));
        Border etched = BorderFactory.createEtchedBorder();
        Border titled = BorderFactory.createTitledBorder(etched, "Text to be translated");
        rawTextPanel.setBorder(titled);

        //translated text area
        translatedTextPanel.setPreferredSize(new Dimension(600, 200));
        Border etched2 = BorderFactory.createEtchedBorder();
        Border titled2 = BorderFactory.createTitledBorder(etched2, "Translated text");
        translatedTextPanel.setBorder(titled2);
        
        //translate button
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(600, 40));
        buttonPanel.add(translate);
        translate.setAlignmentX(Component.CENTER_ALIGNMENT);
        translate.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        //menu bar
        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(load);
        fileMenu.add(saveAs);
        fileMenu.add(quit);
        menuBar.add(translateMenu);

        //set all panels
        Container panel = getContentPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(descriptionPanel);
        panel.add(rawTextPanel);
        panel.add(translatedTextPanel);
        panel.add(buttonPanel);
        pack();
        setVisible(true);
        
        //add translator menu items
        addTranslator(new DetabTranslator());
        addTranslator(new DoubleSpaceTranslator());
        addTranslator(new EntabTranslator());
        addTranslator(new FixIndentationTranslator());
        addTranslator(new FlowTextTranslator());
        addTranslator(new IdentityTranslator());
        addTranslator(new SingleSpaceTranslator());
        addTranslator(new WrapTextTranslator());
        
        //add file menu items
        load.addActionListener(new loadFileListener());
        saveAs.addActionListener(new saveAsFileListener());
        quit.addActionListener(new quitListener());
        translate.addActionListener(new buttonListener());

    }
    
    /**Add a menu item of a particular translator and load settings to gui
     * @param translator: a translator typed object
     */
    private void addTranslator(TranslatorInterface translator) {
        //create menu item for this particular translator
        String name = translator.getName();
        JMenuItem translatorName = new JMenuItem(name);
        translateMenu.add(translatorName);
        
        //add an action listener on selecting this menu item
        translatorName.addActionListener(new TranslateListener(translator));
    }
    
    /**The ActionListener as a inner class. This class:
     * Selects the translator
     * Gets the name of the selected translator, and puts it in the title bar.
     * Gets the description of the selected translator, and puts it just under the title bar.
     * Gets the text from the upper text area, uses the selected translator to translate it,
     * and puts the result in the lower text area
     * @author Zhou Tan
     *
     */
    public class TranslateListener implements ActionListener {
        //set up an translator instance for actionPerformed method
        TranslatorInterface translator;
        
        /**Constructor of the ActionListener
         * @param translator
         */
        public TranslateListener(TranslatorInterface translator) {
            this.translator = translator;
        }
        
        public void actionPerformed(ActionEvent arg0) {
            //set current translator
            currentTranslator = translator.getName();
            
            //set title bar to be current translator
            TextTranslator.this.setTitle(currentTranslator);
            
            //set description to corresponding translator's one
            description.setText(translator.getDescription());
            
            //get raw text that is going to be translated from the interface
            String textToBeTranslated = text.getText();
            
            //translate the text using corresponding translator and display on below text area
            String textTranslated = translator.translate(textToBeTranslated);
            translatedText.setText(textTranslated);
        }
        
    }
    
    
    /**The ActionListener that Uses a JFileChooser to request a text file from the user,
     * and displays the contents of this file in the upper text area.
     * @author Zhou Tan
     *
     */
    public class loadFileListener implements ActionListener {
            
        public void actionPerformed(ActionEvent arg0) {

            BufferedReader myBufferedFileReader = null;
            String text = "";
            String s;

            //$ Display a JChooser load file dialog
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load which file?");

            //$ Get the file chosen in a JChooser load file dialog
            int result = chooser.showOpenDialog(TextTranslator.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                // ...and prepare to use the file
                try {
                    if (file != null) {
                        String fileName = file.getCanonicalPath();
                        myBufferedFileReader =
                            new BufferedReader(new FileReader(fileName));
                        while ((s = myBufferedFileReader.readLine()) != null) {
                            text += s + "\n";
                        }
                        //set raw text area by the file content
                        TextTranslator.this.text.setText(text);
                        
                    }
                }
                catch (IOException e) { }
            }
        }
        
    }
    
    
    /**The ActionListener that Uses a JFileChooser to ask the user where to save the file,
     * then saves the contents of the lower text area in this location.
     * @author Zhou Tan
     *
     */
    public class saveAsFileListener implements ActionListener {
            
        public void actionPerformed(ActionEvent arg0) {
            PrintWriter myPrintWriter = null;
            //$ Display a JChooser save file dialog
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save file as? (Nothing is really saved)");

            //$ Get the file chosen in a JChooser save file dialog
            int result = chooser.showSaveDialog(TextTranslator.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                // ...and prepare to use the file
                String fileName;
                try {
                    if (file != null) {
                        fileName = file.getCanonicalPath();
                        myPrintWriter =
                            new PrintWriter(new FileOutputStream(fileName), true);
                        //put translated text in to arrays
                        String[] inputLines = translatedText.getText().split("\n");
                        //write to file
                        for (String lines : inputLines) {
                            myPrintWriter.println(lines);
                        }
                        myPrintWriter.flush();
                    }
                }
                catch (IOException e) { }
                finally {
                    if (myPrintWriter != null) {
                        myPrintWriter.close();
                    }
                }
            }
        }
    }
    
    
    /**The ActionListener that Terminates the program.
     * @author 
     *
     */
    public class quitListener implements ActionListener {
            
        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }
    
    
    /**The ActionListener that performs the pre-chosen translation function when user
     * clicks on this "Translate" button.
     * @author Zhou Tan
     *
     */
    public class buttonListener implements ActionListener {
        
            public void actionPerformed(ActionEvent arg0) {
                if (currentTranslator == null) {
                    JOptionPane.showMessageDialog(TextTranslator.this,
                            "Please select a translator from the menu!");
                }
                else if (currentTranslator.equals("Detab")) {
                    translatedText.setText(new DetabTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Double Space")) {
                    translatedText.setText(new DoubleSpaceTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Entab")) {
                    translatedText.setText(new EntabTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Fix Indentation")) {
                    translatedText.setText(new FixIndentationTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Flow Text")) {
                    translatedText.setText(new FlowTextTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Identity")) {
                    translatedText.setText(new IdentityTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Single Space")) {
                    translatedText.setText(new SingleSpaceTranslator().translate(text.getText()));
                }
                else if (currentTranslator.equals("Wrap Text")) {
                    translatedText.setText(new WrapTextTranslator().translate(text.getText()));
                }
                else {}
            }
    }
}
