package com.praveen.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.Variant;
import com.praveen.ecommerce.models.Variant.VariantType;

@Component
public class ProductDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	// public static List<String> orderCacheList = new ArrayList<>();

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
		jdbcTemplate.query("SELECT p.*,v.*,i.* FROM product_detail p " + "left join variants v on p.id = v.product_id "
				+ "left join images i on ((i.variant_id=v.id and i.product_id=p.id) OR (i.variant_id is null and i.product_id=p.id)) "
				+ "where p.id=?", new Object[] { productId }, new RowMapper<Product>() {

					@Override
					public Product mapRow(ResultSet rs, int i) throws SQLException, DataAccessException {

						if (product.getId() == null) {
							product.setDescription(rs.getString("p.description"));
							product.setId(rs.getLong("p.id"));
							product.setOrigPrice(rs.getDouble("p.orig_price"));
							product.setPrice(rs.getDouble("p.price"));
							product.setTitle(rs.getString("p.title"));
							product.setImageLinks(new ArrayList<String>());
						}
						if (rs.getLong("v.id") == 0) {
							product.getImageLinks().add(rs.getString("i.link"));
							return null;
						}
						Variant variant = variantMap.get(rs.getLong("v.id"));
						if (variant == null) {
							variant = new Variant();
							variant.setId(rs.getLong("v.id"));
							variant.setImageLink(new ArrayList<String>());
							if (rs.getString("i.link") != null) {
								variant.getImageLink().add(rs.getString("i.link"));
								product.getImageLinks().add(rs.getString("i.link"));
							}
							variant.setVariantName(rs.getString("variant"));
							variant.setVariantType(VariantType.valueOf(rs.getString("variant_type")));
							variantMap.put(rs.getLong("v.id"), variant);
						} else if (rs.getString("i.link") != null) {
							variant.getImageLink().add(rs.getString("i.link"));
							product.getImageLinks().add(rs.getString("i.link"));
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

	public void upgradeInventory(int qty, long productId) {
		jdbcTemplate.update("UPDATE product_detail SET in_stock=(in_stock-" + qty + ") where id=?",
				new Object[] { productId });
	}

	public void upgradeInventory(int qty, long productId, long variantId) {
		jdbcTemplate.update("UPDATE variants SET in_stock=(in_stock-" + qty + ") where id=? and product_id=?",
				new Object[] { variantId, productId });
	}

	public List<Product> getHomePageProducts(int siteId) {
		return jdbcTemplate
				.query("select h.*,p.*,(select link from images where id=p.id limit 1) as image_link from homepage_products h "
						+ "left join product_detail p on h.product_id = p.id", getProductRM());
	}

	private RowMapper<Product> getProductRM() {
		return new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setDescription(rs.getString("p.description"));
				product.setId(rs.getLong("p.id"));
				product.setImageLinks(new ArrayList<>());
				product.getImageLinks().add(rs.getString("image_link"));
				product.setOrigPrice(rs.getDouble("p.orig_price"));
				product.setPrice(rs.getDouble("p.price"));
				product.setTitle(rs.getString("p.title"));
				return product;
			}
		};
	}

	public List<Product> getAllProducts(int offset) {
		return jdbcTemplate
				.query("select p.id,p.title,p.price,(select link from images where id=p.id limit 1) as image_link from "
						+ " product_detail p ", new RowMapper<Product>() {

							@Override
							public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
								Product product = new Product();
								product.setId(rs.getLong("p.id"));
								product.setImageLinks(new ArrayList<>());
								product.getImageLinks().add(rs.getString("image_link"));
								product.setPrice(rs.getDouble("p.price"));
								product.setTitle(rs.getString("p.title"));
								return product;
							}
						});
	}

	@Transactional(rollbackFor = Exception.class)
	public void addProduct(Product product) {
		boolean updated = false;
		KeyHolder holder = new GeneratedKeyHolder();
		int productId = 0;
		String sql = "INSERT INTO product_detail (title,price,orig_price,description,in_stock) VALUES (?, ?, ?, ?, ?)";
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, product.getTitle());
				ps.setDouble(2, product.getPrice());
				ps.setDouble(3, product.getOrigPrice());
				ps.setString(4, product.getDescription());
				ps.setInt(5, product.getInStock());

				return ps;
			}
		}, holder);
		if (i > 0) {
			productId = holder.getKey().intValue();
		}
		if (product.getImageLinks() != null && product.getImageLinks().size() > 0) {
			addProductImages(productId, product.getImageLinks());
		}

	}

	// insert batch example
	public void addProductImages(int productId, List<String> images) {

		String sql = "INSERT INTO images (link,product_id) VALUES (?,?)";

		List<Object[]> batchArgs = new ArrayList<>();
		for (String imageLink : images) {
			batchArgs.add(new Object[] { imageLink, productId });
		}

		jdbcTemplate.batchUpdate(sql, batchArgs);

	}

	public boolean updateProduct(Product product) {
		String sql = "UPDATE product_detail SET title=?,price=?,orig_price=?,description=?,in_stock=? where id=? ";
		int i = jdbcTemplate.update(sql, new Object[] { product.getTitle(), product.getPrice(), product.getOrigPrice(),
				product.getDescription(), product.getInStock()});
		if (i > 0) {
			return true;
		}
		return false;

	}

}
