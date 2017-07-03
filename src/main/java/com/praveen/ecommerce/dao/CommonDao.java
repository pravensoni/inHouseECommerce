package com.praveen.ecommerce.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.Variant;
import com.praveen.ecommerce.models.Variant.VariantType;

@Component
public class CommonDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/*
	 * public Product getProduct(int productId) { try { return
	 * jdbcTemplate.queryForObject("SELECT * FROM product_detail where id=?",
	 * new Object[] { productId }, getProductRM()); } catch
	 * (EmptyResultDataAccessException e) { return null; } catch (Exception e) {
	 * e.printStackTrace(); return null; } }
	 */

	public Product getProduct(int productId) {

		Product product = new Product();
		Map<Long, Variant> variantMap = new HashMap<>();
		jdbcTemplate.query("SELECT p.*,v.*,i.* FROM product_detail p "
				+ "left join variants v on p.id = v.product_id "
				+ "left join images i on i.variant_id=v.id where p.id=?", new Object[] { productId },
				new RowMapper<Product>() {

					@Override
					public Product mapRow(ResultSet rs,int i) throws SQLException, DataAccessException {

						if (product.getId() == null) {
							product.setDescription(rs.getString("p.description"));
							product.setId(rs.getLong("p.id"));
							product.setOrigPrice(rs.getDouble("p.orig_price"));
							product.setPrice(rs.getDouble("p.price"));
							product.setTitle(rs.getString("p.title"));
						}
						if (rs.getLong("v.id") == 0)
							return null;
						Variant variant = variantMap.get(rs.getLong("v.id"));
						if (variant == null) {
							variant = new Variant();
							variant.setId(rs.getLong("v.id"));
							variant.setImageLink(new ArrayList<String>());
							variant.getImageLink().add(rs.getString("i.link"));
							variant.setVariantName(rs.getString("variant"));
							variant.setVariantType(VariantType.valueOf(rs.getString("variant_type")));
							variantMap.put(rs.getLong("v.id"), variant);
						} else {
							variant.getImageLink().add(rs.getString("i.link"));
						}

						return null;
					}

				});
		if (variantMap.size() > 0)
			product.setVariants(new ArrayList<>(variantMap.values()));
		return product;
	}

	/*
	 * public List<Variant> getVariantList(int productId) { return
	 * jdbcTemplate.query("SELECT * FROM variants WHERE product_id=?", new
	 * Object[] { productId }, new RowMapper<Variant>() { public Variant
	 * mapRow(ResultSet rs, int rowNum) throws SQLException { Variant variant =
	 * new Variant(); variant.setId(rs.getLong("id"));
	 * //variant.setImageLink(rs.getString(""));
	 * variant.setVariantName(rs.getString(""));
	 * variant.setVariantType(VariantType.valueOf(rs.getString(""))); return
	 * variant; } }); }
	 */

	private RowMapper<Product> getProductRM() {
		return new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setDescription(rs.getString("description"));
				product.setId(rs.getLong("id"));
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
