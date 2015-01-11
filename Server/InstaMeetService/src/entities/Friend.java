package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Friends database table.
 * 
 */
@Entity
@Table(name="Friends")
@NamedQueries({
	@NamedQuery(name="Friend.findAll", query="SELECT f FROM Friend f")
})
@DiscriminatorColumn(name = "verified")
public class Friend implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FriendPK id;

	private int locationPrivacy;

	private boolean verified;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	//uni-directional many-to-one association to User
	@ManyToOne
	private User friend;


	public Friend() {
	}

	public FriendPK getId() {
		return this.id;
	}

	public void setId(FriendPK id) {
		this.id = id;
	}

	public int getLocationPrivacy() {
		return this.locationPrivacy;
	}

	public void setLocationPrivacy(int locationPrivacy) {
		this.locationPrivacy = locationPrivacy;
	}

	public boolean getVerified() {
		return this.verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFriend() {
		return this.friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}


}