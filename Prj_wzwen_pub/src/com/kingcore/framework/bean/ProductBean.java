package com.kingcore.framework.bean ;

import java.io.Serializable;

/**
 * This class represents a product. It holds information about the
 * product's name, description and price. All setter methods have
 * package scope, since they are only used by the the CatalogBean.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class ProductBean implements Serializable {
    private String id;
    private String name;
    private String descr;
    private float price;

    /**
     * Returns the product id.
     *
     * @return the product id
     */


    /**
     * Returns the product name.
     *
     * @return the product name
     */


    /**
     * Returns the product description.
     *
     * @return the product description
     */

    /**
     * Returns the product price.
     *
     * @return the product price
     */

    /**
     * Sets the product id.
     *
     * @param id the product id
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the product name.
     *
     * @param name the product name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product description.
     *
     * @param descr the product description
     */
    void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * Sets the product price.
     *
     * @param price the product price
     */
    void setPrice(float price) {
        this.price = price;
    }

	public ProductBean(String id, String name, String descr, float price) {
		this.id = id;
		this.name = name;
		this.descr = descr;
		this.price = price;
	}



	public ProductBean(ProductBean other) {
		if(this != other) {
			this.id = other.id;
			this.name = other.name;
			this.descr = other.descr;
			this.price = other.price;
		}
	}
	

	public String toString() {
		String ret = null;
		ret = "id = " + id + "\n";
		ret += "name = " + name + "\n";
		ret += "descr = " + descr + "\n";
		ret += "price = " + price + "\n";
		return ret;
	}

	
	public String getId() {
		return (this.id); 
	}

	public String getName() {
		return (this.name); 
	}

	public String getDescr() {
		return (this.descr); 
	}

	public float getPrice() {
		return (this.price); 
	}

	

}