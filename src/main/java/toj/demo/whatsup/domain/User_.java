package toj.demo.whatsup.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, AssignedStatus> assignedStatus;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SetAttribute<User, User> followers;
	public static volatile SetAttribute<User, Keyword> keywords;
	public static volatile SingularAttribute<User, Integer> notificationPeriod;
	public static volatile SingularAttribute<User, Long> Id;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;

}

