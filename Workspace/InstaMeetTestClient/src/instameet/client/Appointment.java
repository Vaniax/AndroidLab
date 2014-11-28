
package instameet.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für appointment complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="appointment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hoster" type="{http://service/}user" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lattitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="startingTime" type="{http://service/}time" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="visitingUsers" type="{http://service/}user" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "appointment", propOrder = {
    "description",
    "hoster",
    "id",
    "lattitude",
    "longitude",
    "startingTime",
    "title",
    "visitingUsers"
})
public class Appointment {

    protected String description;
    protected User hoster;
    protected int id;
    protected double lattitude;
    protected double longitude;
    protected Time startingTime;
    protected String title;
    @XmlElement(nillable = true)
    protected List<User> visitingUsers;

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der hoster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getHoster() {
        return hoster;
    }

    /**
     * Legt den Wert der hoster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setHoster(User value) {
        this.hoster = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

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

    /**
     * Ruft den Wert der startingTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Time }
     *     
     */
    public Time getStartingTime() {
        return startingTime;
    }

    /**
     * Legt den Wert der startingTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Time }
     *     
     */
    public void setStartingTime(Time value) {
        this.startingTime = value;
    }

    /**
     * Ruft den Wert der title-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Legt den Wert der title-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the visitingUsers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the visitingUsers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVisitingUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link User }
     * 
     * 
     */
    public List<User> getVisitingUsers() {
        if (visitingUsers == null) {
            visitingUsers = new ArrayList<User>();
        }
        return this.visitingUsers;
    }

}
