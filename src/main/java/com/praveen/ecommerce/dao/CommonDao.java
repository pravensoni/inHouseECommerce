package com.praveen.ecommerce.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.praveen.ecommerce.models.Product;

@Component
public class CommonDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Product getProduct(int productId) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM product_detail where id=?", new Object[] { productId },
					getProductRM());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private RowMapper<Product> getProductRM() {
		return new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setDescription(rs.getString("description"));
				product.setId(rs.getInt("id"));
				// product.setImageLinks(List<String>);
				product.setOrigPrice(rs.getDouble("orig_price"));
				product.setPrice(rs.getDouble("price"));
				product.setTitle(rs.getString("title"));
				// product.setVariants(List<Variant>);
				return product;
			}
		};
	}

}
