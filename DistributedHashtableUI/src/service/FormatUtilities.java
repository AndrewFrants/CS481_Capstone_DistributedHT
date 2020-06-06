package service;

//formats utilities
public class FormatUtilities {

    public static Integer SafeConvertStrToInt(final String convert) {
        Integer result = null;

        try {
        	//formatting
            result = Integer.parseInt(convert);
        } catch (final NumberFormatException e) 
        {
            return null;
        }

        return result;
    }
}