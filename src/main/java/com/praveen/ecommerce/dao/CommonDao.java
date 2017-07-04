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

import com.mysql.cj.api.jdbc.Statement;
import com.praveen.ecommerce.models.CustomerDetail;
import com.praveen.ecommerce.models.Order.PaymentStatus;
import com.praveen.ecommerce.models.Order.PaymentType;
import com.praveen.ecommerce.models.OrderStatus.OrderStatusEnum;
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
		jdbcTemplate.query(
				"SELECT p.*,v.*,i.* FROM product_detail p " + "left join variants v on p.id = v.product_id "
						+ "left join images i on i.variant_id=v.id where p.id=?",
				new Object[] { productId }, new RowMapper<Product>() {

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

	@Transactional(rollbackFor = Exception.class)
	public Long placeOrder(CustomerDetail cd, long productId, long variantId, PaymentType pt) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long orderId = null;
		Long customerId = insertCustomerDetail(cd);
		Long paymentId = insertPaymentDetail(pt, PaymentStatus.PENDING);
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO order_details (customer_id, order_status, payment_id)  VALUES (?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, customerId);
				ps.setString(2, OrderStatusEnum.ORDER_PLACED.name());
				ps.setLong(3, paymentId);

				return ps;
			}
		}, holder);
		if (i == 1) {
			orderId = holder.getKey().longValue();
		}
		return orderId;
	}

	public Long insertCustomerDetail(CustomerDetail cd) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long custumerId = null;
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO customer_details (email, phone, fname, lname, address, city, pin_code) VALUES (?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, cd.getEmail());
				ps.setString(2, cd.getPhone());
				ps.setString(3, cd.getFirstName());
				ps.setString(4, cd.getLastName());
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

	public Long insertPaymentDetail(PaymentType pt, PaymentStatus pst) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long paymentId = null;
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO payments (payment_type, payment_status)  VALUES (?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, pt.name());
				ps.setString(2, pst.name());
				return ps;
			}
		}, holder);
		if (i == 1) {
			paymentId = holder.getKey().longValue();
		}
		return paymentId;
	}

	public List<Product> getHomePageProducts() {
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

	public boolean updatePaymentStatus(PaymentStatus ps, Long orderId) {
		int i = jdbcTemplate.update("UPDATE order_details o left join payments py on o.payment_id = py.id "
				+ "SET payment_status = ? WHERE o.id=?", new Object[] { ps.name(), orderId });
		return i == 1;
	}

}
