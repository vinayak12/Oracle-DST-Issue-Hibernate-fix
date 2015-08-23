package vin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.TIMESTAMPTZ;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class TimestampLTZTypeDescriptor implements SqlTypeDescriptor{
	public static final TimestampLTZTypeDescriptor INSTANCE = new TimestampLTZTypeDescriptor();

	public int getSqlType() {
		return Types.TIMESTAMP;
	}

	public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return new BasicBinder<X>( javaTypeDescriptor, this ) {
			@Override
			protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
				if(st instanceof OraclePreparedStatement){
				  ((OraclePreparedStatement)st).setTIMESTAMPTZ( index, new TIMESTAMPTZ(((OraclePreparedStatement)st).getConnection(),javaTypeDescriptor.unwrap( value, Timestamp.class, options )));
				}else{
				 st.setTimestamp( index, javaTypeDescriptor.unwrap( value, Timestamp.class, options ) );
				}
			}
		};
	}

	public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return new BasicExtractor<X>( javaTypeDescriptor, this ) {
			@Override
			protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
				return javaTypeDescriptor.wrap( rs.getTimestamp( name ), options );
			}
		};
	}
	
	
	
}

