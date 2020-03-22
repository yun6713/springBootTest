package com.bonc.test.qs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.RowSet;
import javax.sql.rowset.RowSetFactory;

public class Jdbc {
	
	public void statement() throws SQLException{
		DriverManager dm;
		Connection c=null;
		c.setSchema("");
		Statement s;
		PreparedStatement ps;
		CallableStatement cs;
		ResultSet rs;
		RowSet row;
		RowSetFactory rsf;
	}
}
