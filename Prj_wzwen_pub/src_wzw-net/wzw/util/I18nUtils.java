package wzw.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtils {

	// ”Ô—‘∂®“Â
	public static String LANGUAGE_CHINESE_CN="language.chinese.cn";
	public static String LANGUAGE_CHINESE_TW="language.chinese.tw";
	public static String LANGUAGE_ENGLISH   ="language.english";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static ResourceBundle getBundle( String bundleName){

			  // Get the locale: default, or specified on command-line    
			  Locale locale;    
			 // if (args.length == 2) 
			//	  locale = new Locale(args[0], args[1]);    
			 // else locale = Locale.getDefault( );    
			  
			  locale = Locale.getDefault( );  
			  
			  // Get the resource bundle for that Locale. This will throw an    
			  // (unchecked) MissingResourceException if no bundle is found.     com.davidflanagan.examples.i18n.Menus
			  //System.out.println( "resource = resource."+bundleName );
			  ResourceBundle bundle =   ResourceBundle.getBundle( "resource."+bundleName, locale); 
			  //◊¢“ª
			  //System.out.println(bundle.getString( "mainMenu.jm_File.Text" ));
			  return bundle;
	}
}
