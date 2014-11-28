
package instameet.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für location complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lattitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "location", propOrder = {
    "lattitude",
    "longitude"
})
public class Location {

    protected double lattitude;
    protected double longitude;

    /**
     * Ruft den Wert der lattitude-Eigenschaft ab.
     * 
     */
    public double getLattitude() {
        return lattitude;
    }

    /**
     * Legt den Wert der lattitude-Eigenschaft fest.
     * 
     */
    public void setLattitude(double value) {
        this.lattitude = value;
    }

    /**
     * Ruft den Wert der longitude-Eigenschaft ab.
     * 
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Legt den Wert der longitude-Eigenschaft fest.
     * 
     */
    public void setLongitude(double value) {
        this.longitude = value;
    }

}
