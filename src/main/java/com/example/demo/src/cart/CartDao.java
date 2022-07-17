package com.example.demo.src.cart;

import com.example.demo.src.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class CartDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ****************************************************

    //장바구니에 메뉴 추가
    public int createCart(int userIdx, int menuIdx, PostCartReq postCartReq) {

        /*String getUserCartPlusNumQuery = "select  count(CartPlus.id) as num from Cart inner join CartPlus on Cart.id = CartPlus.cartId where Cart.userId = ?";
        int getUserCartPlusNumParams = userIdx;
        UserCartPlusNum userCartPlusNum = this.jdbcTemplate.queryForObject(getUserCartPlusNumQuery,
                (rs, rowNum) -> new UserCartPlusNum(
                        rs.getInt("num")
                ), getUserCartPlusNumParams
        );
        int cartPlusNum = userCartPlusNum.getNum();*/ //안써도될듯..

        String getMenuShopIdQuery = "select  Menu.shopId as shopId from Shop inner join Menu on Shop.id = Menu.shopId where Menu.id = ?";
        int getMenuShopIdParams = menuIdx;
        MenuShopId menuShopId = this.jdbcTemplate.queryForObject(getMenuShopIdQuery,
                (rs, rowNum) -> new MenuShopId(
                        rs.getInt("shopId")
                ), getMenuShopIdParams
        );
        int shopId = menuShopId.getShopId(); // 장바구니에 담을 메뉴의 가게 id 추출

        String getOrderShopIdQuery = "select Menu.shopId as orderShopId, Cart.status as orderStatus , Cart.id as cartId\n" +
                "from Cart\n" +
                "inner join CartPlus on Cart.id = CartPlus.cartId\n" +
                "inner join Menu on CartPlus.menuId = Menu.id\n" +
                "where Cart.userId =?";
        int getOrderShopIdParams = userIdx;
        List<OrderShopId> orderShopId = this.jdbcTemplate.query(getOrderShopIdQuery,
                (rs, rowNum) -> new OrderShopId(
                        rs.getInt("orderShopId"),
                        rs.getString("orderStatus"),
                        rs.getInt("cartId")
                ), getOrderShopIdParams
        ); // 이미 장바구니에 담은(+담아서 주문했던) 메뉴의 가게id, 상태, 장바구니 id들을 list로 추출

        int cartNum = orderShopId.size();

        int a = -1;
        int b;

        for ( int i = 0; i < cartNum; i++) {
            if (orderShopId.get(i).getOrderShopId() == shopId && orderShopId.get(i).getOrderStatus().equals("ACTIVE")) {

                String plusMenuQuery = "INSERT INTO CartPlus (cartId, menuId, qty) VALUES (?, ?, ?)";
                Object[] plusMenuParams = new Object[]{orderShopId.get(i).getCartId(), menuIdx, postCartReq.getQty()};

                a = this.jdbcTemplate.update(plusMenuQuery, plusMenuParams);

                break;
            } else {
            }
        }

        if ( a == 1 ) {

            b = 1;

        } else {
            String createCartQuery = "INSERT INTO Cart (userId, deliveryOrPickup) VALUES (?, ?)";
            Object[] createCartParams = new Object[]{userIdx, postCartReq.getDeliveryOrPickup()};
            this.jdbcTemplate.update(createCartQuery, createCartParams); //결과가 0/1

            String lastInsertIdQuery = "select last_insert_id()";
            int cartIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); //결과가 마지막 id

            String plusMenuQuery = "INSERT INTO CartPlus (cartId, menuId, qty) VALUES (?, ?, ?)";
            Object[] plusMenuParams = new Object[]{cartIdx, menuIdx, postCartReq.getQty()};

            b = this.jdbcTemplate.update(plusMenuQuery, plusMenuParams);
        }
        return b;
    }



    //장바구니에 메뉴 삭제
    public int deleteCartMenu(int cartMenuIdx) {
        String deleteCartMenuQuery = "DELETE FROM CartPlus WHERE id = ?";

        return this.jdbcTemplate.update(deleteCartMenuQuery, cartMenuIdx);
    }

}
