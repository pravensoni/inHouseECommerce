package com.praveen.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mysql.cj.api.jdbc.Statement;
import com.praveen.ecommerce.models.CustomerInfo;
import com.praveen.ecommerce.models.UserInfo;
import com.praveen.ecommerce.models.UserInfo.UserRole;

@Component
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Long insertCustomerDetail(CustomerInfo cd) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long custumerId = null;
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO customer_details (email, phone, fname, lname, address, city, pin_code) VALUES (?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, cd.getEmail());
				ps.setString(2, cd.getPhone());
				ps.setString(3, cd.getFname());
				ps.setString(4, cd.getLname());
				ps.setString(5, cd.getAddress());
				ps.setString(6, cd.getCity());
				ps.setString(7, cd.getPincode());
				return ps;
			}
		}, holder);
		if (i == 1) {
			custumerId = holder.getKey().longValue();
		}
		return custumerId;
	}

	public UserInfo validateUser(String userName, String pass) {
		UserInfo userInfo = null;
		try {
			userInfo = jdbcTemplate.queryForObject(
					"select id,user_name,role from user where user_name=? and password=? ",
					new Object[] { userName, pass }, new RowMapper<UserInfo>() {

						@Override
						public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
							UserInfo userInfo = new UserInfo();
							userInfo.setUserName(rs.getString("user_name"));
							userInfo.setRole(UserRole.valueOf(rs.getString("role")));
							return userInfo;
						}

					});
		} catch (EmptyResultDataAccessException e) {
			userInfo = null;
		}
		return userInfo;
	}

}
