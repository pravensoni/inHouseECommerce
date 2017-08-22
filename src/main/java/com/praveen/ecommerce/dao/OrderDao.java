package com.praveen.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.cj.api.jdbc.Statement;
import com.praveen.ecommerce.cache.OrderCache;
import com.praveen.ecommerce.models.CustomerInfo;
import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.models.Order.PaymentType;
import com.praveen.ecommerce.models.OrderStatus;
import com.praveen.ecommerce.models.OrderStatus.OrderStatusEnum;
import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.Variant;
import com.praveen.ecommerce.utils.SendMail;

@Component
public class OrderDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SendMail sendMail;

	@Autowired
	OrderCache orderCache;

	@Autowired
	ProductDao productDao;

	@Autowired
	PaymentDao paymentDao;

	@Autowired
	UserDao userDao;

	@Transactional(rollbackFor = Exception.class)
	public Order placeOrder(Order order) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long orderId = null;
		Long customerId = userDao.insertCustomerDetail(order.getCustomerInfo());
		Long paymentId = paymentDao.insertPaymentDetail(order.getPaymentType(), "PENDING");
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
					productDao.upgradeInventory(var.getQty(), product.getId(), var.getId());
				}
			else {
				insertOrderProductMapping(orderId, product.getId(), 0l, product.getQty());
				productDao.upgradeInventory(product.getQty(), product.getId());
			}
		}

		order.setId(orderId.intValue());
		order.setDispOrderId(updateDispOrderId(orderId, "FC"));
		paymentDao.setPayInfo(order);
		if (PaymentType.COD == order.getPaymentType()) {
			sendMail.orderConfirmation(order.getCustomerInfo().getEmail(), order);
		}

		orderCache.put(order.getDispOrderId(), order);

		return order;
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

	public Order getOrder(String dispOrderId) {
		Order order = orderCache.get(dispOrderId);
		if (order == null) {
			// TODO : get order from db
		}
		return order;
	}

	public String getOrderStatus(String dispOrderId) {
		return jdbcTemplate.queryForObject(
				"SELECT os.description_customer FROM order_details od left join order_status os on od.order_status=os.status where od.disp_order_id=?;",
				new Object[] { dispOrderId }, String.class);
	}

	public List<Order> getOrderList(int offset, OrderStatusEnum orderStatus, int limit) {
		String sql = "select od.id,od.disp_order_id,od.order_status,od.date,cd.fname,cd.lname,p.payment_status,p.total from order_details od "
				+ "left join customer_details cd on od.customer_id = cd.id "
				+ "left join payments p on od.payment_id = p.id ";
		Object[] obj = new Object[]{};
		if(orderStatus!=null){
			sql += "where od.order_status=? ";
			obj[0] = orderStatus;
		}
		sql+= "order by od.date desc limit "+offset+","+limit+"";
		
		List<Order> list = jdbcTemplate.query(sql, obj, orderRowMapper());
		return list;

	}

	private RowMapper<Order> orderRowMapper() {
		return new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
				Order order = new Order();
				CustomerInfo cuInfo = new CustomerInfo();
				order.setId(rs.getInt("od.id"));
				order.setDispOrderId(rs.getString("disp_order_id"));
				OrderStatus os = new OrderStatus();
				os.setStatus(OrderStatusEnum.valueOf(rs.getString("od.order_status")));
				order.setOrderStatus(os);
				order.setDate(new Date(rs.getLong("date")));
				cuInfo.setFname(rs.getString("cd.fname"));
				cuInfo.setLname(rs.getString("cd.lname"));
				order.setCustomerInfo(cuInfo);
				order.setPaymentStatus(rs.getString("p.payment_status"));
				order.setTotal(rs.getInt("total"));
				return order;
			}

		};
	}

}
