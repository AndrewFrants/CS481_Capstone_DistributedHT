package service;

public class FormatUtilities {

    public static Integer SafeConvertStrToInt(final String convert) {
        Integer result = null;

        try {
            result = Integer.parseInt(convert);
        } catch (final NumberFormatException e) 
        {
            return null;
        }

        return result;
    }
}