package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Friends")
@DiscriminatorValue(value = "0")
@NamedQuery(name="Friend.findRequest", query="SELECT f FROM UnconfirmedFriend f WHERE f.user.id = :friendId AND f.friend.id = :userId")
public class UnconfirmedFriend extends Friend {
	public UnconfirmedFriend() {
		// TODO Auto-generated constructor stub
		super();
	}
}