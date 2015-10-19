package toj.demo.whatsup.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Message.class)
public abstract class Message_ {

	public static volatile SingularAttribute<Message, Date> creationTimestamp;
	public static volatile SingularAttribute<Message, Long> msgId;
	public static volatile SingularAttribute<Message, String> message;
	public static volatile SingularAttribute<Message, User> user;

}

