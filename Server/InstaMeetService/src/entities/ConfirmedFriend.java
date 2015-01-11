package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Friends")
@DiscriminatorValue(value = "1")
public class ConfirmedFriend extends Friend {
	public ConfirmedFriend() {
		// TODO Auto-generated constructor stub
		super();
	}
}