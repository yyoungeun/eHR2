package com.ehr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {
	
	private User user;
	public DeleteAllStatement() {}
	public DeleteAllStatement(User user) {
		this.user = user;
	}

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		
		//03.PreparedStatement
			PreparedStatement ps = c.prepareStatement(" DELETE FROM users WHERE u_id =?   \n");
			ps.setString(1, user.getU_id());
			return ps;
	}

}
