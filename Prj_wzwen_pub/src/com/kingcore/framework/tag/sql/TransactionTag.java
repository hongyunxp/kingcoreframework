package com.kingcore.framework.tag.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.DataSource;

/**
 * This class is a custom action for executing a SQL SELECT statement.
 * The statement must be defined in the body of the action. It can
 * contain ? place holders, replaced by the value of <ora:sqlValue>
 * elements before execution. The number and order of place holders must
 * match the number and order of <ora:sqlValue> elements in the body.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class TransactionTag extends TagSupport {
    private String dataSourceName;
    private Connection conn;

    /**
     * Sets the dataSource attribute.
     *
     * @param dataSource the name of the DataSource object available
     *   in the application scope
     */
    public void setDataSource(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * Sets the Connection to be used by all nested DB actions.
     */
    public int doStartTag() throws JspException {
        conn = getTransactionConnection();
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Commits the transaction, resets the auto commit flag and closes
     * the Connection (so it's returned to the pool).
     */
    public int doEndTag() throws JspException {
        try {
            conn.commit();
            conn.setAutoCommit(true);
            conn.close();
        }
        catch (SQLException e) {
            throw new JspException("SQL error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        dataSourceName =  null;
        conn = null;
        super.release();
    }

    /**
     * Returns the Connection used for the transaction. This
     * method is used by nested DB actions.
     */
    Connection getConnection() {
        return conn;
    }

    /**
     * Gets a DataSource from the application scope, with the
     * name specified by the dataSource attribute, and prepares
     * it for transaction handling.
     */
    private Connection getTransactionConnection() throws JspException {
        DataSource dataSource = (DataSource)
            pageContext.getAttribute(dataSourceName, PageContext.APPLICATION_SCOPE);
        if (dataSource == null) {
            throw new JspException("dataSource " + dataSourceName + " not found");
        }
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new JspException("SQL error: " + e.getMessage());
        }
        return conn;
    }
}
