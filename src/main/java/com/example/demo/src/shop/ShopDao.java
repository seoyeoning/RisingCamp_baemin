package com.example.demo.src.shop;

import com.example.demo.src.shop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class ShopDao<a> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ***************************************************************

    //가게 추가
    public int createShop(PostShopReq postShopReq) {
        String createShopQuery = "insert into Shop (categoryId , name , address, opemCloseTime, holiday, phoneNumber, introduce, introduceImageUrl, info, minPrice, deliveryArea, reviewNotice,tipRange)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createShopParams = new Object[]{postShopReq.getCategoryIdx(), postShopReq.getShopName(),
        postShopReq.getAddress(), postShopReq.getOpenCloseTime(), postShopReq.getHoliDay(),
        postShopReq.getPhoneNumber(), postShopReq.getIntroduce(), postShopReq.getIntroduceImageUrl(),
        postShopReq.getShopInfo(), postShopReq.getMinPrice(), postShopReq.getDeliveryArea(),
        postShopReq.getReviewNotice(), postShopReq.getTipRange()};

        this.jdbcTemplate.update(createShopQuery,createShopParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    //가게 삭제
    public int deleteShop(int shopIdx) {
        String deleteShopQuery = "UPDATE Shop SET status = 'DELETE' WHERE id = ?";
        int deleteShopParams = shopIdx;

        return this.jdbcTemplate.update(deleteShopQuery, deleteShopParams);
    }

    //가게에 메뉴 추가
    public int createMenu(int shopIdx, PostMenuReq postMenuReq) {
        String createMenuQuery = "INSERT INTO Menu (shopId, menuName, menuInfo, price) VALUES (?, ?, ?,?)";
        Object[] createMenuParams = new Object[]{shopIdx, postMenuReq.getMenuName(), postMenuReq.getMenuInfo(),postMenuReq.getPrice()};

        this.jdbcTemplate.update(createMenuQuery,createMenuParams);

        String lastInsertIdQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //메뉴 삭제
    public  int deleteMenu(int menuIdx) {
        String deleteMenuQuery = "UPDATE Menu SET status = 'DELETE' WHERE id = ?";
        int deleteMenuParams = menuIdx;

        return this.jdbcTemplate.update(deleteMenuQuery,deleteMenuParams);
    }

    // 가게 정보 조회
    public GetShopInfoRes getUserShopInfo(int shopIdx) {
        String getUserShopInfoQuery = "select shopLogoImgUrl ,Shop.name as name ,\n" +
                "       (select round(AVG(score),1) as shopScoreAvg\n" +
                "from ReviewWrite\n" +
                "inner join BaeminOrder on ReviewWrite.orderId = BaeminOrder.id\n" +
                "inner join Cart on BaeminOrder.cartId = Cart.id\n" +
                "inner join CartPlus on CartPlus.cartId = Cart.id\n" +
                "inner join Menu on CartPlus.menuId = Menu.id\n" +
                "where shopId = ?\n" +
                "group by shopId) as shopScoreAvg,minPrice, paymentWay,deliveryTime,tipRange as tipRange\n" +
                "from Shop\n" +
                "where Shop.id = ?";
        Object[] getUserShopInfoParams = new Object[]{shopIdx, shopIdx};

        return this.jdbcTemplate.queryForObject(getUserShopInfoQuery,
                (rs, rowNum) -> new GetShopInfoRes(
                        rs.getString("shopLogoImgUrl"),
                        rs.getString("name"),
                        rs.getFloat("shopScoreAvg"),
                        rs.getInt("minPrice"),
                        rs.getString("paymentWay"),
                        rs.getString("deliveryTime"),
                        rs.getString("tipRange")),
                getUserShopInfoParams);
    }


}
