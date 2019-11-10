/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author madhulika
 */
public class SpecialChars {
    private Pattern pattern;
    private Matcher matcher;
    
    private static final String SPECIAL_CHARS="^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
    "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";
    
    public  boolean check(final char c)
    {
        if(Pattern.compile(SPECIAL_CHARS).matcher(String.valueOf(c)).matches())
            return true;
        return false;
    }
    
}
