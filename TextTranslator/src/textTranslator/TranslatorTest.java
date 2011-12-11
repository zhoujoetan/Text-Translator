package textTranslator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {
    WrapTextTranslator trans = new WrapTextTranslator();
    DetabTranslator detab = new DetabTranslator();
    DoubleSpaceTranslator ds = new DoubleSpaceTranslator();
    EntabTranslator entab = new EntabTranslator();
    FixIndentationTranslator fixIndent = new FixIndentationTranslator();
    FlowTextTranslator flowText = new FlowTextTranslator();
    IdentityTranslator identity = new IdentityTranslator();
    SingleSpaceTranslator ss = new SingleSpaceTranslator();

    @Test
    public void testWrapTextTranslate() {
        String lessThanALine = "Hello world!";
        String lessThanALine2 = "Helloworld!";
        
        String sample = "123456789 ";
        String twoLines = "";
        for (int i = 0; i < 10; i++) {
            twoLines += sample;
        }
        int breakpoint = 72 / sample.length() * sample.length();
        String translatedTwoLines = twoLines.substring(0, breakpoint - 1) + "\n" + twoLines.substring(breakpoint);
        
        String fiveLines = "";
        for (int i = 0; i < 40; i++) {
            fiveLines += sample;
        }
        String translatedFiveLines = fiveLines.substring(0, breakpoint - 1) + "\n" +
					                 fiveLines.substring(breakpoint, breakpoint * 2 - 1) + "\n" + 
					                 fiveLines.substring(breakpoint * 2, breakpoint * 3 - 1) + "\n" + 
					                 fiveLines.substring(breakpoint * 3, breakpoint * 4 - 1) + "\n" + 
					                 fiveLines.substring(breakpoint * 4, breakpoint * 5 - 1) + "\n" + 
					                 fiveLines.substring(breakpoint * 5);
        
        String linesWithEnters = sample + "\n" + twoLines + "\n" + sample;
        String translatedLinesWithEnters = sample + "\n" + translatedTwoLines + "\n" + sample;
        
        String sample2 = "123456789";
        String longLines = "";
        for (int i = 0; i < 20; i++) {
            longLines += sample2;
        }
        
        String longLinesWithSpaces = sample + longLines + " " + sample;
        String translatedLongLinesWithSpaces = sample2 + "\n" + longLines + "\n" + sample;
        
        String longLinesWithEnters = sample2 + "\n" + longLines + "\n" + sample2;
        
        String sample3 = "";
        for (int i = 0; i < 9; i++) {
            sample3 += "12345678";
        }
        String unchanged = "";
        for (int i = 0; i < 3; i++) {
            unchanged += sample3;
            unchanged += " ";
        }
        String translatedUnchaned = sample3 + "\n" + sample3 + "\n" + sample3 + "\n";
        
        assertEquals(lessThanALine, trans.translate(lessThanALine));
        assertEquals(lessThanALine2, trans.translate(lessThanALine2));
        assertEquals(translatedTwoLines, trans.translate(twoLines));
        assertEquals(translatedFiveLines, trans.translate(fiveLines));
        assertEquals(translatedLinesWithEnters, trans.translate(linesWithEnters));
        assertEquals(longLines, trans.translate(longLines));
        assertEquals(translatedLongLinesWithSpaces, trans.translate(longLinesWithSpaces));
        assertEquals(longLinesWithEnters, trans.translate(longLinesWithEnters));
        assertEquals(translatedUnchaned, trans.translate(unchanged));
        }
    
    
    @Test
    public void testDetabTranslate() {
        String test1 = "\t\t  This is a simple \t  test.\n";
        String result = "          This is a simple \t  test.\n";
        assertEquals(result, detab.translate(test1));
    }
    
    
    @Test
    public void testEntabTranslate() {
        String test1 = "          This is a simple \t  test.\n";
        String result = "\t\t  This is a simple \t  test.\n";
        assertEquals(result, entab.translate(test1));
    }
    
    @Test
    public void testIdentityTranslate() {
        String test1 = "          This is a simple \t  test.\n";
        assertEquals(test1, identity.translate(test1));
    }
    
    @Test
    public void testDoubleSpaceStranslate() {
        String test1 = "\n  This is \n\na simple \t  test.\n";
        String result = "\n\n  This is \n\n\n\na simple \t  test.\n\n";
        assertEquals(result, ds.translate(test1));
    }
    
    @Test
    public void testSingleSpaceStranslate() {
        String test1 = "\n\n\n  This is \n\n\n\na simple \t  test.\n\n";
        String result = "\n\n  This is \n\na simple \t  test.\n";
        assertEquals(result, ss.translate(test1));
    }
    
    @Test
    public void testFlowTexttranslate() {
        String test1 = "\t  This is \na simple \t  test.\n    \n This is another one.\n";
        String result = "      This is  a simple       test.\n\n This is another one.\n";
        assertEquals(result, flowText.translate(test1));
    }
    
    @Test
    public void testFixIndentationtranslate() {
        //ordinary indentation
        String test1 = "  public void testFlowTexttranslate() {\nint a = 0;\nint b = 1;\n  }";
        String result1 = "  public void testFlowTexttranslate() {\n      int a = 0;\n      int b = 1;\n  }";
        
        //braces cancel out
        String test2 = "  public void testFlowTexttranslate() {}\nint a = 0;\nint b = 1;\n  }";
        String result2 = "  public void testFlowTexttranslate() {}\n  int a = 0;\n  int b = 1;\n  }";
        
        //"negative" indentation
        String test3 = "  public void testFlowTexttranslate() }{\nint a = 0;\nint b = 1;\n  }";
        String result3 = "  public void testFlowTexttranslate() }{\n  int a = 0;\n  int b = 1;\n  }";
        
        //multiple indentation
        String test4 = "  public void testFlowTexttranslate() }{{{\nint a = 0;\nint b = 1;\n  }";
        String result4 = "  public void testFlowTexttranslate() }{{{\n          int a = 0;\n          int b = 1;\n      }";
        
        assertEquals(result1, fixIndent.translate(test1));
        assertEquals(result2, fixIndent.translate(test2));
        assertEquals(result3, fixIndent.translate(test3));
        assertEquals(result4, fixIndent.translate(test4));
    }
}
