package vin;

import static javax.persistence.GenerationType.AUTO;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table
public class LocalTimeStampDemo {
    
    @Id
   // @GeneratedValue(strategy = AUTO)
    @Column
    private long id;
    
    @Column(nullable = true)
    @Type(type = "vin.TimestampLTZ")
    private Timestamp t1;
    
    @Column(nullable = true)
    @Type(type = "vin.TimestampLTZ")
    private Timestamp t2;
    
    @Column(nullable = true)
    @Type(type = "vin.TimestampLTZ")
    private Timestamp t3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getT1() {
		return t1;
	}

	public void setT1(Timestamp t1) {
		this.t1 = t1;
	}

	public Timestamp getT2() {
		return t2;
	}

	public void setT2(Timestamp t2) {
		this.t2 = t2;
	}

	public Timestamp getT3() {
		return t3;
	}

	public void setT3(Timestamp t3) {
		this.t3 = t3;
	}
    
}
