package com.kingcore.framework.exception ;

/**
 * This exception is thrown when trying to create an instance of
 * Row from a ResultSet with a column of a type that is not supported,
 * e.g. a BINARY column.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UnsupportedTypeException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedTypeException(String message) {
        super(message);
    }
}
