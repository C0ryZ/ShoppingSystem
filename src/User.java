import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    String userName;
    private String userPassword;
    int userID;
    String userLevel;//累计消费金额到达多少升至铜牌、银牌、金牌
    String userRegisterDate;
    float userTotalConsume;
    String userPhoneNumber;
    String userEmail;
    public Scanner scanner = new Scanner(System.in);
    int userLock = 0;
    int judgeLogin = 0;
    int wrongLoginCount = 0;
    int changePassword = 0;
    String url = "jdbc:mysql://localhost:3306/shoppingSystem";
    String mysqlUsername = "root";//your_username
    String mysqlPassword = "hsp";//your_password
    Connection connection = DriverManager.getConnection(url, mysqlUsername, mysqlPassword);

    User() throws SQLException {
        try {
            connection = DriverManager.getConnection(url, mysqlUsername, mysqlPassword);//链接到数据库
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    User(String sqlUserID) throws SQLException {
        try {
            // 执行查询
            String queryUserID = "SELECT * FROM user WHERE userID = ?";
            PreparedStatement statement = connection.prepareStatement(queryUserID);
            statement.setString(1, sqlUserID);
            ResultSet resultSet = statement.executeQuery();
            // 判断查询结果
            if (resultSet.next()) {
                userID = Integer.parseInt(resultSet.getString("userID"));
                userPassword = resultSet.getString("userPassword");
                userName = resultSet.getString("username");
                userLevel = resultSet.getString("userLevel");
                userRegisterDate = resultSet.getString("userRegisterDate");
                userTotalConsume = resultSet.getFloat("userTotalConsume");
                userPhoneNumber = resultSet.getString("userPhoneNumber");
                userEmail = resultSet.getString("userEmail");
                userLock = resultSet.getInt("userLock");
                judgeLogin = resultSet.getInt("judgeLogin");
                wrongLoginCount = resultSet.getInt("wrongLoginCount");
                changePassword = resultSet.getInt("changePassword");
                // 关闭连接
                resultSet.close();
                statement.close();
            } else {
                System.out.println("该用户不存在！");
            }
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void SetUserPassword(String newPassword) throws SQLException {
        userPassword=newPassword;
        String updatePasswordSql = "UPDATE User SET userPassword = ? WHERE userID = ?";
        PreparedStatement statementUpdate = connection.prepareStatement(updatePasswordSql);
        statementUpdate.setString(1, userPassword);
        statementUpdate.setString(2, String.valueOf(userID));
        //检验是否更新成功
        int update = statementUpdate.executeUpdate();
        if (update > 0){
            System.out.println("密码设置成功！");
        } else {
            System.out.println("密码设置失败！");
        }
        statementUpdate.close();
    }

    String GetUserPassword(){
        return userPassword;
    }

    void UserInformation(){
        System.out.println("用户ID："+userID);
        System.out.println("用户名："+userName);
        System.out.println("用户级别："+userLevel);
        System.out.println("用户注册时间："+userRegisterDate);
        System.out.println("累计消费总金额："+userTotalConsume);
        System.out.println("用户手机号："+userPhoneNumber);
        System.out.println("用户邮箱："+userEmail);
    }

    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //大写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //小写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    //mysql修改完成
    void Register() throws SQLException {
        //用户注册用户名可以重复但用户ID唯一
        String selectQueryID = "SELECT * FROM User WHERE userID = ?";
        String selectQueryPhoneNumber = "SELECT * FROM User WHERE userPhoneNumber = ?";
        String selectQueryEmail = "SELECT * FROM User WHERE userEmail = ?";
        while (true){
            System.out.print("请输入用户名（不少于5个字符,不大于16）：");
            userName = scanner.next();
            if(userName.length()>=5&&userName.length()<=16){
                break;
            } else
                System.out.println("用户名输入不合法！请重新输入！");
        }
        while (true) {
            System.out.print("请输入密码（不少于8个字符，且必须包含大小写字母、数字和标点符号）：");
            userPassword = scanner.next();
            if (userPassword.length() >= 8 && userPassword.length() <= 18) {
                int i = 0;
                if (userPassword.matches(REG_NUMBER)) i++;
                if (userPassword.matches(REG_LOWERCASE)) i++;
                if (userPassword.matches(REG_UPPERCASE)) i++;
                if (userPassword.matches(REG_SYMBOL)) i++;
                if (i > 3) {
                    //密码符合要求
                    //检查是否有重复手机号码，直到无重复且满足11要求
                    while (true){
                        System.out.print("请输入手机号码：");
                        userPhoneNumber = scanner.next();
                        if (userPhoneNumber.length() == 11){
                            while (isUserPhoneNumberExists(connection, selectQueryPhoneNumber, userPhoneNumber)) {
                                System.out.println("该手机号码已被注册！请重新输入电话号码!");
                                while (true){
                                    System.out.print("请输入手机号码：");
                                    userPhoneNumber = scanner.next();
                                    if (userPhoneNumber.length() != 11){
                                        System.out.println("手机号码输入错误！请输入11位正确的手机号码！");
                                    } else {
                                        break;
                                    }
                                }
                            }
                            break;
                        }else{
                            System.out.println("手机号码输入错误！请输入11位正确的手机号码！");
                        }
                    }
                    String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                    boolean flag = false;
                    while (!flag){
                        System.out.print("请输入邮箱地址：");
                        userEmail = scanner.next();
                        while (isUserEmailExists(connection, selectQueryEmail, userEmail)) {
                            System.out.print("该邮箱已被注册！请重新输入邮箱:");
                            userEmail = scanner.next();
                        }
                        //邮箱无重复再检查是否符合要求
                        Pattern p;
                        Matcher m;
                        p = Pattern.compile(regEx1);
                        m = p.matcher(userEmail);
                        if(m.matches()){
                            //邮箱格式正确且无重复
                            flag = true;
                        }
                        else
                            System.out.println("输入邮箱格式错误!请重新输入！");
                    }
                    Random random = new Random();
                    userID = random.nextInt(900000) + 100000;
                    while (isUserIDExists(connection, selectQueryID, String.valueOf(userID))) {
                        userID = random.nextInt(900000) + 100000;
                    }
                    //ID满足要求
                    userLevel = "普通用户";
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    userRegisterDate = dateFormat.format(date);
                    System.out.println("注册日期："+ userRegisterDate);
                    userTotalConsume = 0;
                    userLock = 0;
                    judgeLogin = 0;
                    wrongLoginCount = 0;
                    changePassword = 0;
                    String insertSql = "INSERT INTO User (username, userPassword, userID,userLevel,userRegisterDate" +
                            ",userTotalConsume,userPhoneNumber,userEmail,userLock,judgeLogin,wrongLoginCount,changePassword) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement preparedStatement;
                    preparedStatement = connection.prepareStatement(insertSql);
                    preparedStatement.setString(1, userName);
                    preparedStatement.setString(2, userPassword);
                    preparedStatement.setString(3, String.valueOf(userID));
                    preparedStatement.setString(4, userLevel);
                    preparedStatement.setString(5, userRegisterDate);
                    preparedStatement.setFloat(6, userTotalConsume);
                    preparedStatement.setString(7, userPhoneNumber);
                    preparedStatement.setString(8, userEmail);
                    preparedStatement.setInt(9, 0);
                    preparedStatement.setInt(10, 0);
                    preparedStatement.setInt(11, 0);
                    preparedStatement.setInt(12, 0);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    System.out.println("帐号注册成功！");
                    UserInformation();
                    break;
                }else {
                    System.out.println("输入密码格式错误！请重新输入！");
                }
            } else
                System.out.println("密码输入长度不合法！请重新输入！");
        }
    }

    boolean isUserIDExists(Connection connection, String selectQueryID, String newID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryID);
        preparedStatement.setString(1, newID);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }

    boolean isUserPhoneNumberExists(Connection connection, String selectQueryPhoneNumber, String newUserPhoneNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryPhoneNumber);
        preparedStatement.setString(1, newUserPhoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }

    boolean isUserEmailExists(Connection connection, String selectQueryEmail, String newUserEmail) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryEmail);
        preparedStatement.setString(1, newUserEmail);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }

    //mysql修改完成
    boolean Login() {
        int userAccount = 0;
        while (true) {
            boolean validID = false;
            while (!validID) {
                System.out.print("请输入用户帐号ID：");
                if (scanner.hasNextInt()) {
                    userAccount = scanner.nextInt();
                    validID = true;
                } else {
                    System.out.println("输入无效，请重新输入ID。");
                    scanner.next(); // 清除非数字输入
                }
            }
            //new User(String.valueOf(userAccount));
            try {
                // 执行查询
                String queryUserID = "SELECT * FROM user WHERE userID = ?";
                PreparedStatement statement = connection.prepareStatement(queryUserID);
                statement.setString(1, String.valueOf(userAccount));
                ResultSet resultSet = statement.executeQuery();
                // 判断查询结果
                if (resultSet.next()) {
                    userID = Integer.parseInt(resultSet.getString("userID"));
                    userPassword = resultSet.getString("userPassword");
                    userName = resultSet.getString("username");
                    userLevel = resultSet.getString("userLevel");
                    userRegisterDate = resultSet.getString("userRegisterDate");
                    userTotalConsume = resultSet.getFloat("userTotalConsume");
                    userPhoneNumber = resultSet.getString("userPhoneNumber");
                    userEmail = resultSet.getString("userRegisterDate");
                    userLock = resultSet.getInt("userLock");
                    judgeLogin = resultSet.getInt("judgeLogin");
                    wrongLoginCount = resultSet.getInt("wrongLoginCount");
                    changePassword = resultSet.getInt("changePassword");
                    // 关闭连接
                    resultSet.close();
                    statement.close();
                    if (userLock == 1) {
                        System.out.println("该用户已被锁定，无法登录！");
                        return false;
                    } else {
                        if (judgeLogin == 1) {
                            System.out.println("该用户已登录！");
                        } else {
                            while (judgeLogin != 1) {
                                if (wrongLoginCount == 5) {
                                    userLock = 1;
                                    wrongLoginCount = 0;//归零
                                    System.out.println("该用户已被锁定，无法登录！");
                                    //更新表中部分数据
                                    String updateSql1 = "UPDATE User SET userLock = ? ,wrongLoginCount = ? WHERE userID = ?";
                                    PreparedStatement statementUpdate1 = connection.prepareStatement(updateSql1);
                                    statementUpdate1.setInt(1, userLock);
                                    statementUpdate1.setInt(2, wrongLoginCount);
                                    statementUpdate1.setString(3, String.valueOf(userID));
                                    //检验是否更新成功
                                    statementUpdate1.executeUpdate();
                                    statementUpdate1.close();
                                    return false;
                                }
                                System.out.print("请输入用户密码：");
                                String password = scanner.next();
                                if (password.equals(userPassword)) {
                                    wrongLoginCount = 0;
                                    userLock = 0;
                                    judgeLogin = 1;
                                    //更新表中数据
                                    //更新表中部分数据
                                    String updateSql2 = "UPDATE User SET userLock = ? ,wrongLoginCount = ? ,judgeLogin = ? where userID = ?";
                                    PreparedStatement statementUpdate2 = connection.prepareStatement(updateSql2);
                                    statementUpdate2.setInt(1, userLock);
                                    statementUpdate2.setInt(2, wrongLoginCount);
                                    statementUpdate2.setInt(3, judgeLogin);
                                    statementUpdate2.setString(4, String.valueOf(userID));
                                    //检验是否更新成功
                                    statementUpdate2.executeUpdate();
                                    // 关闭连接
                                    statementUpdate2.close();
                                    System.out.println("登录成功！");
                                    return true;
                                } else {
                                    wrongLoginCount++;
                                    String updateSql3 = "UPDATE User SET wrongLoginCount = ? where userID = ?";
                                    PreparedStatement statementUpdate3 = connection.prepareStatement(updateSql3);
                                    statementUpdate3.setInt(1, wrongLoginCount);
                                    statementUpdate3.setInt(2, userID);
                                    statementUpdate3.executeUpdate();
                                    // 关闭连接
                                    statementUpdate3.close();
                                    System.out.print("密码输入错误！是否请重新输入？（y/n）:");
                                    String next = scanner.next();
                                    if (next.equals("n")) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("该用户不存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    void UserPassword() throws SQLException {
        System.out.println("***欢迎来到密码管理系统！***");
        changePassword = 0;
        int judgePassword = -1;
        while (judgePassword!=0){
            System.out.print("请选择（1、修改密码 2、忘记密码 0、退出）:");
            judgePassword = scanner.nextInt();
            switch (judgePassword){
                case 0:{
                    judgePassword = 0;
                    break;
                }
                case 1:{
                    UpdateUserPassword();
                    judgePassword = 0;
                    break;
                }
                case 2:{
                    ForgetUserPassword();
                    judgePassword = 0;
                    break;
                }
                default:{
                    System.out.println("选择错误！请重新选择！");
                    break;
                }
            }
        }

    }

    void UpdateUserPassword() throws SQLException {
        if(judgeLogin == 1){
            while (true) {
                System.out.print("请输入新密码（不少于8个字符，且必须包含大小写字母、数字和标点符号）：");
                String newPassword1 = scanner.next();
                System.out.print("请再次确认新密码：");
                String newPassword2 = scanner.next();
                if(8<=newPassword1.length() && newPassword1.length()<=18 && 8<=newPassword2.length() && newPassword2.length()<=18){
                    if(newPassword1.equals(newPassword2)){
                        int i = 0;
                        if (newPassword1.matches(REG_NUMBER)) i++;
                        if (newPassword1.matches(REG_LOWERCASE)) i++;
                        if (newPassword1.matches(REG_UPPERCASE)) i++;
                        if (newPassword1.matches(REG_SYMBOL)) i++;
                        if (i > 3) {
                            SetUserPassword(newPassword1);
                            System.out.println("用户"+userName+"的新密码修改成功！新密码为："+GetUserPassword());
                            changePassword++;
                            judgeLogin = 0;
                            //更新表中部分数据
                            String updateSql = "UPDATE User SET judgeLogin = ? ,changePassword = ? WHERE userID = ?";
                            PreparedStatement statementUpdate = connection.prepareStatement(updateSql);
                            statementUpdate.setInt(1, judgeLogin);
                            statementUpdate.setInt(2, changePassword);
                            statementUpdate.setString(3, String.valueOf(userID));
                            //检验是否更新成功
                            statementUpdate.executeUpdate();
                            statementUpdate.close();
                            break;
                        }else{
                            System.out.println("密码格式错误！请重新输入！");
                        }
                    }
                    else{
                        System.out.println("两次密码输入不一致！请重新输入!");
                    }
                }else {
                    System.out.println("请输入不少于8个字符的密码！");
                }
            }
        }else{
            System.out.println("请先登录！");
        }
    }

    void ForgetUserPassword() throws SQLException {
        //System.out.print("请输入用户名");
        //用户名不在方法内输入，在方法外输入然后找到对应的类，然后再调用对应类的忘记密码方法
        while (true){
            System.out.print("请输入注册的邮箱：");
            String email = scanner.next();
            if(email.equals(userEmail)){
                Random random = new Random();
                int randomLength = random.nextInt(9) + 10;//随机生成8-18位的任意合法密码
                SetUserPassword(RandomPassword(randomLength));
                if(judgeLogin == 1){
                    System.out.println("密码已重置成功！将通过用户邮箱发送！即将退出登录！"+"****该密码通过邮箱发送，但为了方便后续登录验证，在这里看一下重置后的密码:"+GetUserPassword());
                }else{
                    System.out.println("密码已重置成功！将通过用户邮箱发送！"+"!!该密码通过邮箱发送，但为了方便后续登录验证，在这里看一下重置的密码:"+GetUserPassword());
                }
                changePassword++;
                judgeLogin = 0;
                //更新数据
                String updateSql = "UPDATE User SET judgeLogin = ? ,changePassword = ? WHERE userID = ?";
                PreparedStatement statementUpdate = connection.prepareStatement(updateSql);
                statementUpdate.setInt(1, judgeLogin);
                statementUpdate.setInt(2, changePassword);
                statementUpdate.setString(3, String.valueOf(userID));
                //检验是否更新成功
                statementUpdate.executeUpdate();
                statementUpdate.close();
                break;
            }else{
                System.out.println("输入邮箱错误！请重新输入！");
            }
        }
    }

    String RandomPassword(int len){
        // 1、定义基本字符串baseStr和出参password
        String randomPassword = null;
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+{}|<>?";
        boolean flag = false;
        // 2、使用循环来判断是否是正确的密码
        while (!flag) {
            // 密码重置
            randomPassword = "";
            // 个数计数
            int a = 0, b = 0, c = 0, d = 0;
            for (int i = 0; i < len; i++) {
                int rand = (int) (Math.random() * baseStr.length());
                randomPassword += baseStr.charAt(rand);
                if (0 <= rand && rand < 10) {//数字
                    a++;
                }
                if (10 <= rand && rand < 36) {//小写字母
                    b++;
                }
                if (36 <= rand && rand < 62) {//大写字母
                    c++;
                }
                if (62 <= rand) {//特殊符号
                    d++;
                }
            }
            // 是否是合法的密码
            flag = (a!=0 && b!=0 && c!=0 &&d!=0);
        }
        return randomPassword;
    }

    boolean isGoodsIDExists(String newGoodsID) throws SQLException {
        String selectQueryID = "SELECT * FROM Store WHERE goodsID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryID);
        preparedStatement.setString(1, newGoodsID);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }

    boolean isShoppingCartGoodsExists(String goodsID) throws SQLException {
        String selectQueryID = "SELECT * FROM shoppingCart WHERE userID = ? and goodsID = ?";//where有两个条件时用and不能用，
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryID);
        preparedStatement.setString(1,String.valueOf(userID));
        preparedStatement.setString(2,goodsID);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }

    void ShoppingCartController() throws SQLException {
        System.out.println("****欢迎来到购物车！****");
        boolean judgeShoppingCart = false;
        while (!judgeShoppingCart){
            System.out.print("请选择（1、添加商品 2、移除商品 3、修改购买数量 4、付款 5、查看购物历史 0、退出购物车）");
            int shoppingCartChoose = scanner.nextInt();
            switch (shoppingCartChoose){
                case 0:{
                    judgeShoppingCart = true;
                    break;
                }
                case 1:{
                    AddGoods();
                    break;
                }
                case 2:{
                    RemoveGoods();
                    break;
                }
                case 3:{
                    ChangeNumber();
                    break;
                }
                case 4:{
                    Pay();
                    break;
                }
                case 5:{
                    ShowShoppingRecord();
                    break;
                }
                default:
                    System.out.println("输入错误！请重新输入！");
            }
        }
    }

    void AddGoods() throws SQLException {
        //添加时需要比较商品数量，如果商品数量不足则添加失败并提示剩余数量
        String goodsID;
        String goodsName;
        String manufacturer;
        String produceDate;
        String modelSize;
        float purchasePrice;
        int purchaseQuantity;
        int remainNumber;
        while (true) {
            System.out.print("请输入商品编号:");
            goodsID = scanner.next();
            if(isGoodsIDExists(goodsID)){
                //从mysql取值取值?
                String selectGoodsIDSql = "SELECT * FROM Store where goodsID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectGoodsIDSql);
                preparedStatement.setString(1,goodsID);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                goodsName = resultSet.getString("goodsName");
                manufacturer = resultSet.getString("manufacturer");
                produceDate = resultSet.getString("produceDate");
                modelSize = resultSet.getString("modelSize");
                purchasePrice = resultSet.getFloat("sellPrice");
                remainNumber = resultSet.getInt("remainNumber");
                preparedStatement.close();
                break;
            }else{
                System.out.println("输入商品编号不存在！是否重新输入？（y/n）");
                String next = scanner.next();
                if(next.equals("n")){
                    System.out.println("添加商品失败！");
                    return;
                }
            }
        }
        //到这里说明goodsID存在
        boolean validInput = false; //输入的数量是否为整数
        while (!validInput) {
            try {
                System.out.print("请输入商品数量：");
                purchaseQuantity = scanner.nextInt();
                if (purchaseQuantity > 0) {
                    //将购物车中已有商品再次添加进购物车时，只将两次数量相加，若超过库存则按最大购买数量设置
                    if(isShoppingCartGoodsExists(goodsID)){
                        System.out.println("已添加过该商品至购物车！");
                        String addSameGoods = "SELECT * FROM shoppingCart where userID = ? and goodsID = ?";
                        PreparedStatement preparedStatementSameGoods = connection.prepareStatement(addSameGoods);
                        preparedStatementSameGoods.setString(1, String.valueOf(userID));
                        preparedStatementSameGoods.setString(2,goodsID);
                        ResultSet resultSetSameGoods = preparedStatementSameGoods.executeQuery();
                        resultSetSameGoods.next();
                        int sameGoodsPurchaseQuantity = resultSetSameGoods.getInt("purchaseQuantity");
                        int newPurchaseQuantity = sameGoodsPurchaseQuantity+purchaseQuantity;
                        if (remainNumber>=newPurchaseQuantity){
                            String updateSameGoodsPurchaseQuantity = "UPDATE shoppingCart set purchaseQuantity = ? " +
                                    "where userID = ? and goodsID = ?";
                            PreparedStatement preparedStatementAddSameGoods = connection.prepareStatement(updateSameGoodsPurchaseQuantity);
                            preparedStatementAddSameGoods.setInt(1,newPurchaseQuantity);
                            preparedStatementAddSameGoods.setString(2, String.valueOf(userID));
                            preparedStatementAddSameGoods.setString(3,goodsID);
                            if (preparedStatementAddSameGoods.executeUpdate() > 0){
                                System.out.println("再次添加该商品成功！");
                            }else{
                                System.out.println("再次添加该商品失败！");
                            }
                            validInput = true;
                        }else {
                            System.out.println("本次添加后商品总数量超过库存数量！添加失败！");
                            System.out.print("是否重新输入？(y/n)");
                            String next = scanner.next();
                            if(next.equals("n")){
                                validInput = true;
                            }
                        }
                    }else{//该用户没有添加过相同商品
                        if (remainNumber > purchaseQuantity){
                            //将各种信息存入购物车表中
                            String insertShoppingCartSql = "INSERT INTO shoppingCart (userID, goodsID, goodsName,manufacturer" +
                                    ",produceDate,modelSize" + ",purchasePrice, purchaseQuantity) VALUES (?,?,?,?,?,?,?,?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(insertShoppingCartSql);
                            preparedStatement.setString(1, String.valueOf(userID));
                            preparedStatement.setString(2, goodsID);
                            preparedStatement.setString(3, goodsName);
                            preparedStatement.setString(4, manufacturer);
                            preparedStatement.setString(5, produceDate);
                            preparedStatement.setString(6, modelSize);
                            preparedStatement.setFloat(7, purchasePrice);
                            preparedStatement.setInt(8, purchaseQuantity);
                            int  resultSet = preparedStatement.executeUpdate();
                            if (resultSet > 0){
                                System.out.println("添加商品成功！");
                                System.out.println(goodsName+"添加成功，数量："+purchaseQuantity);
                            }
                            validInput = true;
                        } else {
                            System.out.println("库存数量不足！请重新输入数量！");
                        }
                    }
                } else {
                    System.out.println("输入数量错误！添加失败！");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入错误！请确保你输入了一个整数。");
                scanner.nextLine(); // 清空输入缓冲区
            }
        }
    }

    void RemoveGoods() throws SQLException {
        String selectShoppingCartInfo = "SELECT * from shoppingCart where userID = ?";
        String COUNT = "SELECT COUNT(*) AS count from shoppingCart where userID = ?";
        PreparedStatement statement = connection.prepareStatement(COUNT);
        statement.setString(1, String.valueOf(userID));
        ResultSet resultSetCount = statement.executeQuery();
        resultSetCount.next();
        int count = resultSetCount.getInt("count");
        resultSetCount.close();
        statement.close();
        if(count <= 0){
            System.out.println("购物车为空！");
        }else{
            PreparedStatement preparedStatementDisplay = connection.prepareStatement(selectShoppingCartInfo);
            preparedStatementDisplay.setString(1, String.valueOf(userID));
            ResultSet resultSet = preparedStatementDisplay.executeQuery();
            System.out.println("用户："+userID+"的购物车：");
            while (resultSet.next()){
                System.out.print("商品编号："+resultSet.getString("goodsID"));
                System.out.print("\t商品名称："+resultSet.getString("GoodsName"));
                System.out.print("\t生产厂家："+resultSet.getString("manufacturer"));
                System.out.print("\t生产日期："+resultSet.getString("produceDate"));
                System.out.print("\t型号："+resultSet.getString("modelSize"));
                System.out.print("\t购买价格："+resultSet.getFloat("purchasePrice"));
                System.out.println("\t购买数量："+resultSet.getInt("purchaseQuantity"));
            }
            resultSet.close();
            preparedStatementDisplay.close();
            //展示购物车里面的内容
            boolean validID = false;
            while (!validID){
                System.out.print("请选择需要移除的商品编号：");
                String removeGoodsID = scanner.next();
                if(isShoppingCartGoodsExists(removeGoodsID)){
                    System.out.println("***警告！是否移除购物车？(y/n)***");
                    String remove = scanner.next();
                    if(remove.equals("y")){
                        String deleteGoodsSql = "DELETE FROM shoppingCart where userID = ? AND goodsID = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(deleteGoodsSql);
                        preparedStatement.setString(1, String.valueOf(userID));
                        preparedStatement.setString(2,removeGoodsID);
                        if (preparedStatement.executeUpdate()>0){
                            System.out.println("移除成功！");
                        }else {
                            System.out.println("移除失败！");
                        }
                        validID = true;
                    }else{
                        System.out.println("取消移除购物车操作。");
                        break;
                    }
                }else {
                    System.out.println("选择失败！请检查商品ID是否正确！");
                }
            }
        }
    }

    void ChangeNumber() throws SQLException {
        //修改后数量小于0则移除改商品
        String selectShoppingCartInfo = "SELECT * from shoppingCart where userID = ?";
        String COUNT = "SELECT COUNT(*) AS count from shoppingCart where userID = ?";
        PreparedStatement statementCount = connection.prepareStatement(COUNT);
        statementCount.setString(1, String.valueOf(userID));
        ResultSet resultSetCount = statementCount.executeQuery();
        resultSetCount.next();
        int count = resultSetCount.getInt("count");
        resultSetCount.close();
        statementCount.close();
        if(count <= 0){
            System.out.println("购物车为空！");
        }else {
            PreparedStatement preparedStatementDisplay = connection.prepareStatement(selectShoppingCartInfo);
            preparedStatementDisplay.setString(1, String.valueOf(userID));
            ResultSet resultSet = preparedStatementDisplay.executeQuery();
            System.out.println("用户："+userID+"的购物车：");
            while (resultSet.next()){
                System.out.println("商品编号："+resultSet.getString("goodsID")+"\t"+"商品名称："+resultSet.getString("goodsName")+"\t"+"购买数量："+resultSet.getInt("purchaseQuantity"));
            }
            resultSet.close();
            preparedStatementDisplay.close();
            boolean validID = false;
            while (!validID){
                System.out.print("请输入需要修改数量的商品编号：");
                String changeQuantityGoodsID = scanner.next();
                if(isShoppingCartGoodsExists(changeQuantityGoodsID)){
                    System.out.print("将数量修改为:");
                    int changeNumber = scanner.nextInt();
                    if (changeNumber>0){
                        String changePurchaseQuantity = "update shoppingCart set purchaseQuantity = ? where userID = ? AND goodsID = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(changePurchaseQuantity);
                        preparedStatement.setInt(1,changeNumber);
                        preparedStatement.setString(2, String.valueOf(userID));
                        preparedStatement.setString(3,changeQuantityGoodsID);
                        if (preparedStatement.executeUpdate()>0){
                            System.out.println("修改购买数量成功！");
                        }else {
                            System.out.println("修改购买数量失败！");
                        }
                        validID = true;
                    }else {
                        //删除该商品
                        String deleteGoodsSql = "DELETE FROM shoppingCart where userID = ? AND goodsID = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(deleteGoodsSql);
                        preparedStatement.setString(1, String.valueOf(userID));
                        preparedStatement.setString(2,changeQuantityGoodsID);
                        if (preparedStatement.executeUpdate()>0){
                            System.out.println("购买数量小于0，删除该商品成功！");
                        }else {
                            System.out.println("购买数量小于0，删除该商品失败！");
                        }
                        validID = true;
                    }

                }else {
                    System.out.println("商品ID不存在！是否重新输入？(y/n)");
                    String next = scanner.next();
                    if(next.equals("n")){
                        validID = true;
                    }
                }
            }
        }
    }

    void Pay() throws SQLException {
        //支付后需要清空支付的商品
        //支付后更新购物记录
        //支付后改变商品库存
        String selectShoppingCartInfo = "SELECT * from shoppingCart where userID = ?";
        String COUNT = "SELECT COUNT(*) AS count from shoppingCart where userID = ?";
        PreparedStatement statementCount = connection.prepareStatement(COUNT);
        statementCount.setString(1, String.valueOf(userID));
        ResultSet resultSetCount = statementCount.executeQuery();
        resultSetCount.next();
        int count = resultSetCount.getInt("count");
        resultSetCount.close();
        statementCount.close();
        if(count <= 0){
            System.out.println("购物车为空！");
        }else{
            PreparedStatement preparedStatementDisplay = connection.prepareStatement(selectShoppingCartInfo);
            preparedStatementDisplay.setString(1, String.valueOf(userID));
            ResultSet resultSet = preparedStatementDisplay.executeQuery();
            System.out.println("用户："+userID+"的购物车：");
            while (resultSet.next()){
                System.out.println("商品编号："+resultSet.getString("goodsID")+"\t"+"商品名称："+resultSet.getString("goodsName")+"\t"+"购买数量："+resultSet.getInt("purchaseQuantity"));
            }
            resultSet.close();
            preparedStatementDisplay.close();
            System.out.print("是否付款？（y/n）");
            String pay = scanner.next();
            if(pay.equals("y")) {
                PreparedStatement preparedStatementPay = connection.prepareStatement(selectShoppingCartInfo);
                preparedStatementPay.setString(1, String.valueOf(userID));
                ResultSet resultSetPay = preparedStatementPay.executeQuery();
                String insertShoppingRecord = "INSERT INTO shoppingRecord (userID, goodsID, goodsName,manufacturer" +
                        ",produceDate,modelSize" + ",purchasePrice, purchaseQuantity,totalValue) VALUES (?,?,?,?,?,?,?,?,?)";
                while (resultSetPay.next()){
                    String goodsID = resultSetPay.getString("goodsID");
                    String selectRemainNumber = "SELECT * FROM Store where goodsID = ?";
                    PreparedStatement preparedStatementRemainNumber = connection.prepareStatement(selectRemainNumber);
                    preparedStatementRemainNumber.setString(1,goodsID);
                    ResultSet resultSetRemainNumber = preparedStatementRemainNumber.executeQuery();
                    resultSetRemainNumber.next();
                    int remainNumber = resultSetRemainNumber.getInt("remainNumber");
                    preparedStatementRemainNumber.close();
                    int purchaseQuantity = resultSetPay.getInt("purchaseQuantity");
                    if (purchaseQuantity <= remainNumber){
                        //库存够，成功购买
                        float consume = resultSetPay.getFloat("purchasePrice") * purchaseQuantity;
                        userTotalConsume += consume;
                        //更新user、store库存,shoppingRecord表,删除购物车对应列
                        PreparedStatement preparedStatementInsert = connection.prepareStatement(insertShoppingRecord);
                        preparedStatementInsert.setString(1, String.valueOf(userID));
                        preparedStatementInsert.setString(2, goodsID);
                        preparedStatementInsert.setString(3, resultSetPay.getString("goodsName"));
                        preparedStatementInsert.setString(4, resultSetPay.getString("manufacturer"));
                        preparedStatementInsert.setString(5, resultSetPay.getString("produceDate"));
                        preparedStatementInsert.setString(6, resultSetPay.getString("modelSize"));
                        preparedStatementInsert.setFloat(7, resultSetPay.getFloat("purchasePrice"));
                        preparedStatementInsert.setInt(8, purchaseQuantity);
                        preparedStatementInsert.setFloat(9, consume);
                        if(preparedStatementInsert.executeUpdate() > 0){
                            System.out.println("购买"+resultSetPay.getString("goodsName")+",售价"+resultSetPay.getFloat("purchasePrice")+",购买数量"+purchaseQuantity+"成功！花费"+consume+"元。");
                        }else{
                            System.out.println("购买"+resultSetPay.getString("goodsName")+"失败！");
                        }
                        remainNumber = remainNumber-purchaseQuantity;
                        String updateRemainNumber = "UPDATE Store set remainNumber = ? where goodsID = ?";
                        PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateRemainNumber);
                        preparedStatementUpdate.setInt(1,remainNumber);
                        preparedStatementUpdate.setString(2,goodsID);
                        preparedStatementUpdate.executeUpdate();
                        String deleteSql = "DELETE FROM shoppingCart where userID = ? AND goodsID = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
                        preparedStatement.setString(1, String.valueOf(userID));
                        preparedStatement.setString(2, goodsID);
                        preparedStatement.executeUpdate();
                    }else{//库存数量不足
                        System.out.println("商品"+resultSetPay.getString("goodsName")+"库存不足！购买失败！");
                        System.out.print("是否更改购买数量（y/n）:");
                        String changeGoodsNumber = scanner.next();
                        if (changeGoodsNumber.equals("y")){
                            while (true){
                                System.out.print("将数量修改为:");
                                int changeNumber = scanner.nextInt();
                                if(changeNumber < remainNumber){
                                    purchaseQuantity = changeNumber;
                                    System.out.println("数量修改成功！");
                                    break;
                                }else{
                                    System.out.println("库存不足！数量修改失败,请重新输入！");
                                }
                            }
                            float consume = resultSetPay.getFloat("purchasePrice") * purchaseQuantity;
                            userTotalConsume += consume;
                            //更新user、store库存,shoppingRecord表,删除购物车对应列
                            PreparedStatement preparedStatementInsert = connection.prepareStatement(insertShoppingRecord);
                            preparedStatementInsert.setString(1, String.valueOf(userID));
                            preparedStatementInsert.setString(2, goodsID);
                            preparedStatementInsert.setString(3, resultSetPay.getString("goodsName"));
                            preparedStatementInsert.setString(4, resultSetPay.getString("manufacturer"));
                            preparedStatementInsert.setString(5, resultSetPay.getString("produceDate"));
                            preparedStatementInsert.setString(6, resultSetPay.getString("modelSize"));
                            preparedStatementInsert.setFloat(7, resultSetPay.getFloat("purchasePrice"));
                            preparedStatementInsert.setInt(8, purchaseQuantity);
                            preparedStatementInsert.setFloat(9, consume);
                            if(preparedStatementInsert.executeUpdate() > 0){
                                System.out.println("购买"+resultSetPay.getString("goodsName")+",售价"+resultSetPay.getFloat("purchasePrice")+",购买数量"+purchaseQuantity+"成功！花费"+consume+"元。");
                            }else{
                                System.out.println("购买"+resultSetPay.getString("goodsName")+"失败！");
                            }
                            remainNumber = remainNumber-purchaseQuantity;
                            String updateRemainNumber = "UPDATE Store set remainNumber = ? where goodsID = ?";
                            PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateRemainNumber);
                            preparedStatementUpdate.setInt(1,remainNumber);
                            preparedStatementUpdate.setString(2,goodsID);
                            preparedStatementUpdate.executeUpdate();
                            String deleteSql = "DELETE FROM shoppingCart where userID = ? AND goodsID = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
                            preparedStatement.setString(1, String.valueOf(userID));
                            preparedStatement.setString(2, goodsID);
                            preparedStatement.executeUpdate();
                        }
                    }
                }
            }else{
                System.out.println("取消付款");
            }
            String updateTotalConsumeSql="update User set  userTotalConsume = ? where userID= ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(updateTotalConsumeSql);
            preparedStatement.setFloat(1,userTotalConsume);
            preparedStatement.setString(2, String.valueOf(userID));
            preparedStatement.executeUpdate();
            SetUserLevel();
        }
    }

    void ShowShoppingRecord(){
        //只有成功付款后，才会更新mysql记录
        try {
            String shoppingRecordDisplay = "SELECT * from shoppingRecord where userID = ?";
            String COUNT = "SELECT COUNT(*) AS count from shoppingRecord where userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT);
            preparedStatement.setString(1, String.valueOf(userID));
            ResultSet resultSetCount = preparedStatement.executeQuery();
            resultSetCount.next();
            int count = resultSetCount.getInt("count");
            resultSetCount.close();
            if(count <= 0){
                System.out.println("您还未进行过购物！");
            } else {
                preparedStatement = connection.prepareStatement(shoppingRecordDisplay);
                preparedStatement.setString(1, String.valueOf(userID));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    System.out.print("商品名称："+resultSet.getString("GoodsName"));
                    System.out.print("\t生产厂家："+resultSet.getString("manufacturer"));
                    System.out.print("\t生产日期："+resultSet.getString("produceDate"));
                    System.out.print("\t型号："+resultSet.getString("modelSize"));
                    System.out.print("\t购买价："+resultSet.getFloat("purchasePrice"));
                    System.out.print("\t购买数量："+resultSet.getInt("purchaseQuantity"));
                    System.out.println("\t消费："+resultSet.getInt("totalValue"));
                }
                resultSet.close();
            }
            preparedStatement.close();
            resultSetCount.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("消费总金额："+userTotalConsume);
    }

    void SetUserLevel() throws SQLException {
        String level = userLevel;
        if(500<=userTotalConsume && userTotalConsume<1000){
            userLevel = "铜牌用户";
        }
        if(1000<=userTotalConsume && userTotalConsume<3000){
            userLevel = "银牌用户";
        }
        if(3000<=userTotalConsume){
            userLevel = "金牌用户";
        }
        if(!level.equals(userLevel)){
            String updateLevelSql="update User set userLevel = ? , userTotalConsume = ? where userID= ? ";
            //预处理SQL语句
            PreparedStatement preparedStatement = connection.prepareStatement(updateLevelSql);
            preparedStatement.setString(1, userLevel);
            preparedStatement.setFloat(2, userTotalConsume);
            preparedStatement.setString(3, String.valueOf(userID));
            int execute = preparedStatement.executeUpdate();
            if(execute>0){
                System.out.println("恭喜你！升至"+userLevel+"!");
            }else {
                System.out.println("升级失败!");
            }
        }
    }

}

