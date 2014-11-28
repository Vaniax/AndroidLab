
package instameet.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für chatmessage complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="chatmessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="friendid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messageDate" type="{http://service/}timestamp" minOccurs="0"/>
 *         &lt;element name="receiver" type="{http://service/}user" minOccurs="0"/>
 *         &lt;element name="sender" type="{http://service/}user" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chatmessage", propOrder = {
    "friendid",
    "id",
    "message",
    "messageDate",
    "receiver",
    "sender"
})
public class Chatmessage {

    protected int friendid;
    protected int id;
    protected String message;
    protected Timestamp messageDate;
    protected User receiver;
    protected User sender;

    /**
     * Ruft den Wert der friendid-Eigenschaft ab.
     * 
     */
    public int getFriendid() {
        return friendid;
    }

    /**
     * Legt den Wert der friendid-Eigenschaft fest.
     * 
     */
    public void setFriendid(int value) {
        this.friendid = value;
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
     * Ruft den Wert der message-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Legt den Wert der message-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Ruft den Wert der messageDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Timestamp }
     *     
     */
    public Timestamp getMessageDate() {
        return messageDate;
    }

    /**
     * Legt den Wert der messageDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Timestamp }
     *     
     */
    public void setMessageDate(Timestamp value) {
        this.messageDate = value;
    }

    /**
     * Ruft den Wert der receiver-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Legt den Wert der receiver-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setReceiver(User value) {
        this.receiver = value;
    }

    /**
     * Ruft den Wert der sender-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getSender() {
        return sender;
    }

    /**
     * Legt den Wert der sender-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setSender(User value) {
        this.sender = value;
    }

}
