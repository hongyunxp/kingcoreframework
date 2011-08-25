package com.kingcore.framework.exception ;

/**
 * This exception is thrown when trying to call a get method on an
 * instance of a Value subclass with a return type that's not supported
 * for the data type represented by the subclass.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UnsupportedConversionException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedConversionException(String message) {
        super(message);
    }
}
