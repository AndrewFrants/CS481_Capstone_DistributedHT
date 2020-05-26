package service;

public class AssertUtilities {

    public static void ThrowIfNull(final Object arg, final String text) {
       
    	if (arg == null)
    	{
    		throw new IllegalArgumentException(text);
    	}
    }
}