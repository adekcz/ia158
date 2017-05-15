package cz.fi.muni.ia158.messenger;

import java.util.HashMap;

public class MorseDecoder {
	private static final HashMap<String, String> MORSE_TABLE = new HashMap<String, String>();
	public static final String MORSE_SEPARATOR = "/";
	static {
		MORSE_TABLE.put(".-","A");
		MORSE_TABLE.put("-...","B");
		MORSE_TABLE.put("-.-.","C");
		MORSE_TABLE.put("-..","D");
		MORSE_TABLE.put(".","E");
		MORSE_TABLE.put("..-.","F");
		MORSE_TABLE.put("--.","G");
		MORSE_TABLE.put("....","H");
		MORSE_TABLE.put("..","I");
		MORSE_TABLE.put(".---","J");
		MORSE_TABLE.put("-.-","K");
		MORSE_TABLE.put(".-..","L");
		MORSE_TABLE.put("--","M");
		MORSE_TABLE.put("-.","N");
		MORSE_TABLE.put("---","O");
		MORSE_TABLE.put(".--.","P");
		MORSE_TABLE.put("--.-","Q");
		MORSE_TABLE.put(".-.","R");
		MORSE_TABLE.put("...","S");
		MORSE_TABLE.put("-","T");
		MORSE_TABLE.put("..-","U");
		MORSE_TABLE.put("...-","V");
		MORSE_TABLE.put(".--","W");
		MORSE_TABLE.put("-..-","X");
		MORSE_TABLE.put("-.--","Y");
		MORSE_TABLE.put("--..","Z");
		MORSE_TABLE.put("-----","0");
		MORSE_TABLE.put(".----","1");
		MORSE_TABLE.put("..---","2");
		MORSE_TABLE.put("...--","3");
		MORSE_TABLE.put("....-","4");
		MORSE_TABLE.put(".....","5");
		MORSE_TABLE.put("-....","6");
		MORSE_TABLE.put("--...","7");
		MORSE_TABLE.put("---..","8");
		MORSE_TABLE.put("----.","9");
	}
	

	public static String decode(String input){
		String[] split = input.split(MORSE_SEPARATOR);
		String result = "";
		for(String c: split){
			result += decodeChar(c);
		}
		return result;
	}

	private static String decodeChar(String c) {
		if(MORSE_TABLE.containsKey(c)){
			return MORSE_TABLE.get(c);
		}
		return "";
	}

}
