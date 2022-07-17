package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class UserDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ******************************************************************************

    /**
     * DAO관련 함수코드의 전반부는 크게 String ~~~Query와 Object[] ~~~~Params, jdbcTemplate함수로 구성되어 있습니다.(보통은 동적 쿼리문이지만, 동적쿼리가 아닐 경우, Params부분은 없어도 됩니다.)
     * Query부분은 DB에 SQL요청을 할 쿼리문을 의미하는데, 대부분의 경우 동적 쿼리(실행할 때 값이 주입되어야 하는 쿼리) 형태입니다.
     * 그래서 Query의 동적 쿼리에 입력되어야 할 값들이 필요한데 그것이 Params부분입니다.
     * Params부분은 클라이언트의 요청에서 제공하는 정보(~~~~Req.java에 있는 정보)로 부터 getXXX를 통해 값을 가져옵니다. ex) getEmail -> email값을 가져옵니다.
     *      Notice! get과 get의 대상은 카멜케이스로 작성됩니다. ex) item -> getItem, password -> getPassword, email -> getEmail, userIdx -> getUserIdx
     * 그 다음 GET, POST, PATCH 메소드에 따라 jabcTemplate의 적절한 함수(queryForObject, query, update)를 실행시킵니다(DB요청이 일어납니다.).
     *      Notice!
     *      POST, PATCH의 경우 jdbcTemplate.update
     *      GET은 대상이 하나일 경우 jdbcTemplate.queryForObject, 대상이 복수일 경우, jdbcTemplate.query 함수를 사용합니다.
     * jdbcTeplate이 실행시킬 때 Query 부분과 Params 부분은 대응(값을 주입)시켜서 DB에 요청합니다.
     * <p>
     * 정리하자면 < 동적 쿼리문 설정(Query) -> 주입될 값 설정(Params) -> jdbcTemplate함수(Query, Params)를 통해 Query, Params를 대응시켜 DB에 요청 > 입니다.
     * <p>
     * <p>
     * DAO관련 함수코드의 후반부는 전반부 코드를 실행시킨 후 어떤 결과값을 반환(return)할 것인지를 결정합니다.
     * 어떠한 값을 반환할 것인지 정의한 후, return문에 전달하면 됩니다.
     * ex) return this.jdbcTemplate.query( ~~~~ ) -> ~~~~쿼리문을 통해 얻은 결과를 반환합니다.
     */

    /**
     * 참고 링크
     * https://jaehoney.tistory.com/34 -> JdbcTemplate 관련 함수에 대한 설명
     * https://velog.io/@seculoper235/RowMapper%EC%97%90-%EB%8C%80%ED%95%B4 -> RowMapper에 대한 설명
     */

    // 회원가입
    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (eMail, passWord, nickName, phoneNumber, birth) VALUES (?,?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassWord(), postUserReq.getNickName(),postUserReq.getPhoneNumber(),postUserReq.getBirth()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        // email -> postUserReq.getEmail(), password -> postUserReq.getPassword(), nickname -> postUserReq.getNickname() 로 매핑(대응)시킨다음 쿼리문을 실행한다.
        // 즉 DB의 User Table에 (email, password, nickname)값을 가지는 유저 데이터를 삽입(생성)한다.

        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        int userIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.

        String createUserAddressQuery = "INSERT INTO UserAddress (userId, name, address) VALUES (?, ?, ?)";
        Object[] createUserAddressParams = new Object[]{userIdx, postUserReq.getAddressName(), postUserReq.getAddress()};
        this.jdbcTemplate.update(createUserAddressQuery,createUserAddressParams);

        String lastInsertUserAddressIdQuery = "select last_insert_id()";
        int  orderAddressId = this.jdbcTemplate.queryForObject(lastInsertUserAddressIdQuery, int.class);

        String selectOrderAddressIdQuery = "UPDATE User SET orderAddressId = ? WHERE id = ?";
        Object[] selectOrderAddressIdParams = new Object[]{orderAddressId, userIdx};
        this.jdbcTemplate.update(selectOrderAddressIdQuery,selectOrderAddressIdParams);

        return userIdx;

    }

    // 로그인: 해당 email에 해당되는 user의 암호화된 비밀번호 값을 가져온다.
    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select id as userIdx, eMail as email, passWord , nickName from User where eMail =?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
        String getPwdParams = postLoginReq.getEmail(); // 주입될 email값을 클라이언트의 요청에서 주어진 정보를 통해 가져온다.

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("passWord"),
                        rs.getString("nickName")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getPwdParams
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }


    // 회원정보 변경
    public int modifyUserName(PatchUserReq patchUserReq) {
        String modifyUserNameQuery = "UPDATE User SET nickName = ?, passWord = ?, phoneNumber = ? WHERE id = ?"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(),patchUserReq.getPassWord(),patchUserReq.getPhoneNumber(), patchUserReq.getUserIdx()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0) 
    }

    //유저 쿠폰 조회
    public List<GetUserCouponRes> getUserCoupons(int userIdx){
        String getUserCouponQuery = "select DeliveryCoupon.id as couponIdx, name as couponName, contents, minPrice,\n" +
                "    date_format(startDate , '%Y.%m.%d') as startDate,\n" +
                "    date_format(finishDate , '%Y.%m.%d') as finishDate\n" +
                "from DeliveryCoupon\n" +
                "inner join User on DeliveryCoupon.userId = User.id\n" +
                "where User.id = ?";
        int getUserCouponParams = userIdx;
        return this.jdbcTemplate.query(getUserCouponQuery,
        (rs, rowNum) -> new GetUserCouponRes(
                rs.getInt("couponIdx"),
                rs.getString("couponName"),
                rs.getString("contents"),
                rs.getInt("minPrice"),
                rs.getString("startDate"),
                rs.getString("finishDate")),
        getUserCouponParams);
    }

    // 해당 userIdx를 갖는 유저조회
    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select id as userIdx, nickName, email as eMail, phoneNumber from User where id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 특정 회원 주문 내역 조회
    public List<GetUserOrdersDPRes> getUserOrdersDP(int userIdx) {
        String getUserOrdersDPQuery = "select userId,date_format(BaeminOrder.createdAt , '%m/%d') as orderDate, (SELECT  SUBSTR( _UTF8'일월화수목금토', DAYOFWEEK(BaeminOrder.createdAt), 1)) as orderDay,\n" +
                "       (case when BaeminOrder.status = 'FINISH'\n" +
                "           then '완료'\n" +
                "           end) orderStatus,\n" +
                "       Shop.name as menuName,\n" +
                "       (case when count(CartPlus.menuId) >= 2\n" +
                "           then  concat(menuName,' 외 ',count(CartPlus.menuId)-1,'개')\n" +
                "           when count(CartPlus.menuId) = 1\n" +
                "           then menuName\n" +
                "           end ) orderMenuInfo\n" +
                "from Cart\n" +
                "inner join BaeminOrder  on BaeminOrder.cartId = Cart.id\n" +
                "inner join CartPlus on CartPlus.cartId = Cart.id\n" +
                "inner join Menu on CartPlus.menuId = Menu.id\n" +
                "inner join Shop on Menu.shopId = Shop.id\n" +
                "inner join User on Cart.userId = User.id\n" +
                "where userId = ?\n" +
                "group by Cart.id";
        int getUserOrdersDPParams = userIdx;
        return this.jdbcTemplate.query(getUserOrdersDPQuery,
                (rs, rowNum) -> new GetUserOrdersDPRes(
                        rs.getInt("userId"),
                        rs.getString("orderDate"),
                        rs.getString("orderDay"),
                        rs.getString("orderStatus"),
                        rs.getString("menuName"),
                        rs.getString("orderMenuInfo")),
                getUserOrdersDPParams);
    }

    // 특정 회원 장바구니 메뉴들만 조회
    public List<GetUserCartMenuDPRes> getUserCartMenuDP(int userIdx) {
        String getUserCartMenuDPQuery = "select userId, Shop.name as shopName, menuName, format(price,'###,###') menuPrice, CartPlus.qty as qty,\n" +
                "       format(Menu.price*CartPlus.qty,'###,###') menuTotalPrice\n" +
                "from Cart\n" +
                "inner join CartPlus on CartPlus.cartId = Cart.id\n" +
                "inner join Menu on CartPlus.menuId = Menu.id\n" +
                "inner join Shop on Menu.shopId = Shop.id\n" +
                "inner join User on Cart.userId = User.id\n" +
                "where Cart.status = 'ACTIVE' and userId = ?";
        int getUserCartMenuDPParams = userIdx;
        return this.jdbcTemplate.query(getUserCartMenuDPQuery,
                (rs, rowNum) -> new GetUserCartMenuDPRes(
                        rs.getInt("userId"),
                        rs.getString("shopName"),
                        rs.getString("menuName"),
                        rs.getString("menuPrice"),
                        rs.getInt("qty"),
                        rs.getString("menuTotalPrice")),
                getUserCartMenuDPParams);
    }
    // 특정 회원 장바구니 가격 조회
    public GetUserCartPriceDPRes getUserCartPriceDP(int userIdx) {
        String getUserCartPriceDPQuery = "select userId, Cart.deliveryOrPickup as deliveryOrPickup , format(sum(Menu.price*CartPlus.qty),'###,###') totalOrderPrice\n" +
                "from Cart\n" +
                "inner join CartPlus on CartPlus.cartId = Cart.id\n" +
                "inner join Menu on CartPlus.menuId = Menu.id\n" +
                "inner join Shop on Menu.shopId = Shop.id\n" +
                "inner join User on Cart.userId = User.id\n" +
                "where Cart.status = 'ACTIVE' and userId = ?\n" +
                "group by Cart.id";
        int getUserCartPriceDPParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserCartPriceDPQuery,
                (rs, rowNum) -> new GetUserCartPriceDPRes(
                        rs.getInt("userId"),
                        rs.getString("deliveryOrPickup"),
                        rs.getString("totalOrderPrice")),
                getUserCartPriceDPParams);
    }

    // 회원 주소 추가
    public int postUserAddress(int userIdx, PostUserAddressReq postUserAddressReq) {
        String postUserAddressQuery = "INSERT INTO UserAddress (userId, name, address) VALUES (?, ?, ?)";
        Object[] postUserAddressParams = new Object[]{userIdx,postUserAddressReq.getName(),postUserAddressReq.getAddress()};
        return this.jdbcTemplate.update(postUserAddressQuery,postUserAddressParams);
    }
    // 회원 대표 주소 변경
    public int patchUserOrderAddress(int userIdx, int addressIdx) {
        String patchUserOrderAddressQuery = "UPDATE User SET orderAddressId = ? WHERE id = ?";
        Object[] patchUserOrderAddressParams = new Object[]{addressIdx, userIdx};

        return this.jdbcTemplate.update(patchUserOrderAddressQuery, patchUserOrderAddressParams);
    }
    // 회원 주소 삭제
    public int deleteUserAddress(int addressIdx) {
        String deleteUserAddressQuery = "DELETE FROM UserAddress WHERE id = ?";
        int deleteUserAddressParams = addressIdx;
        return this.jdbcTemplate.update(deleteUserAddressQuery, deleteUserAddressParams);
    }
    // 회원 주소 변경
    public int patchUserAddress(int addressIdx, String userAddress) {
        String patchUserAddressQuery = "UPDATE UserAddress SET address = ? WHERE id = ?";
        Object[] patchUserAddressParams = new Object[]{userAddress, addressIdx};

        return this.jdbcTemplate.update(patchUserAddressQuery, patchUserAddressParams);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // User 테이블에 존재하는 전체 유저들의 정보 조회
    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from User"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("email"),
                        rs.getString("passWord"))// RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        ); // 복수개의 회원정보들을 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    // 해당 nickname을 갖는 유저들의 정보 조회
    public List<GetUserRes> getUsersByNickname(String nickname) {
        String getUsersByNicknameQuery = "select * from User where nickname =?"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        String getUsersByNicknameParams = nickname;
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUsersByNicknameParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }
}
