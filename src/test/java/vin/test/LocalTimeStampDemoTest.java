package vin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import vin.LocalTimeStampDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration("/testContext.xml")
// @ContextConfiguration("C:/My-projects/workspace/1/src/main/resources/testContext.xml")
@Transactional
public class LocalTimeStampDemoTest {

	@Autowired
	private SessionFactory sessionFactory;
	private Session currentSession;
	
	String dateStr = "11/1/15 01:10 AM EST";
	String dateStr1 = "11/1/15 01:10 AM EDT";
	String dateStr2 = "11/1/15 00:50 AM EDT";

	Timestamp ts1 = new Timestamp(stringToDate(dateStr).getTimeInMillis());
	Timestamp ts2 = new Timestamp(stringToDate(dateStr1).getTimeInMillis());
	Timestamp ts3 = new Timestamp(stringToDate(dateStr2).getTimeInMillis());


	@Before
	public void openSession() {
		currentSession = sessionFactory.getCurrentSession();
	}

	@Test
	public void test1shouldHaveASessionFactory() {
		assertNotNull(sessionFactory);
	}

	@Test
    public void test2deleteAllRow() throws HibernateException, SQLException {
        currentSession.createQuery("delete LocalTimeStampDemo").executeUpdate();
        currentSession.connection().commit();
       }

	@Test
	public void test3insertDSTDate() throws HibernateException, SQLException {
		LocalTimeStampDemo demo = new LocalTimeStampDemo();

		
		demo.setId(1);
		demo.setT1(ts1);
		demo.setT2(ts2);
		demo.setT3(ts3);

		currentSession.persist(demo);
		currentSession.flush();
		assertEquals(1, currentSession.createQuery("from LocalTimeStampDemo")
				.list().size());
		currentSession.connection().commit();
	}

	@Test
	public void test4selectDSTDate() {

		List<LocalTimeStampDemo> list = currentSession.createQuery(
				"from LocalTimeStampDemo").list();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm aaa z");
		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));

		System.out.println("****test4selectDSTDate****");
		for (LocalTimeStampDemo localTimeStampDemo : list) {
			
			System.out.println(sdf.format(localTimeStampDemo.getT1()));
			System.out.println(sdf.format(localTimeStampDemo.getT2()));
			System.out.println(sdf.format(localTimeStampDemo.getT3()));
			
			assertEquals(ts1.getTime(),localTimeStampDemo.getT1().getTime());
			assertEquals(ts2.getTime(),localTimeStampDemo.getT2().getTime());
			assertEquals(ts3.getTime(),localTimeStampDemo.getT3().getTime());
			
			
		}

	}
	
	@Test
	public void test5WhereClause() {

		assertEquals(1,currentSession.createQuery("from LocalTimeStampDemo where t1 = :t1").setParameter("t1",ts1).list().size());
		assertEquals(1,currentSession.createQuery("from LocalTimeStampDemo where t2 = :t2").setParameter("t2",ts2).list().size());
		assertEquals(1,currentSession.createQuery("from LocalTimeStampDemo where t3 = :t3").setParameter("t3",ts3).list().size());


	}

	
	

	private static Calendar stringToDate(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm aaa z");

		try {

			Date dt = sdf.parse(str);
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("GMT"));
			c.setTime(dt);
			System.out.println(c.getTime());
			System.out.println(c.getTimeInMillis());
			// System.out.println(dt);
			// System.out.println(dt.getTime());
			return c;

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;

	}

	/*
	 * @Test public void shouldBeAbleToPersistAnObject() { assertEquals(0,
	 * currentSession.createQuery("from Foo").list().size()); Foo jobUser = new
	 * Foo("Bar"); currentSession.persist(jobUser); currentSession.flush();
	 * assertEquals(1, currentSession.createQuery("from Foo").list().size()); }
	 */

	/*
	 * @Test public void shouldBeABleToQueryForObjects() {
	 * shouldBeAbleToPersistAnObject();
	 * 
	 * assertEquals(1,
	 * currentSession.createQuery("from Foo where name = 'Bar'").list().size());
	 * assertEquals(0,
	 * currentSession.createQuery("from Foo where name = 'Baz'").list().size());
	 * }
	 */
}
