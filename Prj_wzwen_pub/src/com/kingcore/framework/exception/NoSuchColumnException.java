package com.kingcore.framework.exception ;

/**
 * This exception is thrown when trying to get a column from a Row
 * using an invalid index or name.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class NoSuchColumnException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchColumnException(String message) {
        super(message);
    }
}
