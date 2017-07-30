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
import com.praveen.ecommerce.JavaIntegrationKit;
import com.praveen.ecommerce.models.CustomerInfo;
import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.models.Order.PaymentStatus;
import com.praveen.ecommerce.models.Order.PaymentType;
import com.praveen.ecommerce.models.OrderStatus.OrderStatusEnum;
import com.praveen.ecommerce.models.PayInfo;
import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.Variant;
import com.praveen.ecommerce.models.Variant.VariantType;

@Component
public class CommonDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	JavaIntegrationKit kit;

	public static Map<String, Order> orderCache = new HashMap<>();

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

	@Transactional(rollbackFor = Exception.class)
	public Order placeOrder(Order order) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long orderId = null;
		Long customerId = insertCustomerDetail(order.getCustomerInfo());
		Long paymentId = insertPaymentDetail(order.getPaymentType(), PaymentStatus.PENDING);
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

		for (Product product : order.getProducts()) {
			if (product.getVariants() != null && !product.getVariants().isEmpty())
				for (Variant var : product.getVariants()) {
					insertOrderProductMapping(orderId, product.getId(), var.getId(), var.getQty());
				}
			else {
				insertOrderProductMapping(orderId, product.getId(), null, product.getQty());
			}
		}

		order.setId(orderId.intValue());
		order.setDispOrderId(updateDispOrderId(orderId, "FC"));
		setPayInfo(order);
		sendEmail(order.getCustomerInfo().getEmail());

		orderCache.put(order.getDispOrderId(), order);

		return order;
	}

	private void setPayInfo(Order order) {
		PayInfo payInfo = new PayInfo("UFu3ed", "", order.getDispOrderId(), order.getSubTotal() + "",
				order.getCustomerInfo().getFname(), order.getCustomerInfo().getEmail(),
				order.getCustomerInfo().getPhone(), "testProductInfo", "http://localhost:8080/payment/success",
				"http://localhost:8080/payment/failure", "payu_paisa");
		Map<String, String> params = new HashMap<>();
		params.put("key", payInfo.getKey());
		params.put("txnid", payInfo.getTxnid());
		params.put("amount", payInfo.getAmount());
		params.put("firstname", payInfo.getFirstname());
		params.put("email", payInfo.getEmail());
		params.put("phone", payInfo.getPhone());
		params.put("productinfo",payInfo.getProductinfo());
		params.put("surl", payInfo.getSurl());
		params.put("furl", payInfo.getFurl());
		params.put("service_provider", payInfo.getServiceProvider());
		String hash = null;
		try {
			hash = kit.hashCalMethod(params).get("hash");
			payInfo.setHash(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		order.setPayInfo(payInfo);
	}

	public boolean sendEmail(String add) {
		// TODO : implement
		return true;
	}

	private String updateDispOrderId(Long orderId, String prefix) {
		String dispOrderId = prefix + orderId;
		int i = jdbcTemplate.update("UPDATE order_details SET disp_order_id = ? WHERE id=?",
				new Object[] { dispOrderId, orderId });
		if (i > 0) {
			return dispOrderId;
		} else {
			return null;
		}

	}

	private void insertOrderProductMapping(Long orderId, Long productId, Long variantId, int qty) {
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO product_order (order_id, product_id, variant_id,qty)  VALUES (?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, orderId);
				ps.setLong(2, productId);
				ps.setLong(3, variantId);
				ps.setInt(4, qty);

				return ps;
			}
		});

	}

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

	public Long insertPaymentDetail(PaymentType pt, PaymentStatus pst) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long paymentId = null;
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO payments (payment_type, payment_status)  VALUES (?,?)";
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

	public boolean updatePaymentStatus(PaymentStatus ps, Long orderId) {
		int i = jdbcTemplate.update("UPDATE order_details o left join payments py on o.payment_id = py.id "
				+ "SET payment_status = ? WHERE o.id=?", new Object[] { ps.name(), orderId });
		return i == 1;
	}

	public Order getOrder(String dispOrderId) {
		Order order = orderCache.get(dispOrderId);
		if (order == null) {
			// TODO : get order from db
		}
		return order;
	}

}
