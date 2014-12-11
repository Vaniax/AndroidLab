package entities2;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the chatmessages database table.
 * 
 */
@Entity
@Table(name="ChatMessages")
@NamedQuery(name="Chatmessage.findAll", query="SELECT c FROM Chatmessage c")
public class Chatmessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String message;

	@Column(name="message_date")
	private Timestamp messageDate;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="senderId")
	private User sender;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="receiverId")
	private User receiver;

	public Chatmessage() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getMessageDate() {
		return this.messageDate;
	}

	public void setMessageDate(Timestamp messageDate) {
		this.messageDate = messageDate;
	}

	public User getSender() {
		return this.sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return this.receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

}