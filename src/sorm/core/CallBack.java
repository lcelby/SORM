package sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 回调接口
 * @author lcelby
 *
 */
public interface CallBack {
	public Object doExecute(Connection conn, PreparedStatement ps,ResultSet rs);
}
