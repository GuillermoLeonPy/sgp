package py.com.kyron.sgp.persistence.dao.config.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class StringBooleanTypeHandler implements TypeHandler<Boolean> {

	public StringBooleanTypeHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		String s = rs.getString(columnName);
		if(s != null){
			if ("S".equalsIgnoreCase(s)) {
				return new Boolean(true);
			} else if ("N".equalsIgnoreCase(s)) {
				return new Boolean(false);
			} else {
				throw new SQLException("Unexpected value " + s + " found where S or N was expected.");
			}
		}else{
			return null;
		}
	}

	@Override
	public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		String s = rs.getString(columnIndex);
		if(s != null){
			if ("S".equalsIgnoreCase(s)) {
				return new Boolean(true);
			} else if ("N".equalsIgnoreCase(s)) {
				return new Boolean(false);
			} else {
				throw new SQLException("Unexpected value " + s + " found where S or N was expected.");
			}
		}else{
			return null;
		}
	}

	@Override
	public Boolean getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		String s = cs.getString(columnIndex);
		if(s != null){
			if ("S".equalsIgnoreCase(s)) {
				return new Boolean(true);
			} else if ("N".equalsIgnoreCase(s)) {
				return new Boolean(false);
			} else {
				throw new SQLException("Unexpected value " + s + " found where S or N was expected.");
			}
		}else{
			return null;
		}
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, Boolean parameter,JdbcType arg3) throws SQLException {
		// TODO Auto-generated method stub
		if (parameter != null) {
			boolean b = parameter;
			if (b) {
				ps.setString(i, "S");
			} else {
				ps.setString(i, "N");
			}
		}else{
			ps.setString(i, null);
		}
	}

}
