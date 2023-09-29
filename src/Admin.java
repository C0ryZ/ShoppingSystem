import java.sql.*;
import java.util.Scanner;
public class Admin {
    String admin;
    String adminPassword;
    Scanner scanner = new Scanner(System.in);
    String url = "jdbc:mysql://localhost:3306/shoppingSystem";
    String mysqlUsername = "root";
    String mysqlPassword = "hsp";
    Connection connection = DriverManager.getConnection(url, mysqlUsername, mysqlPassword);
    Admin() throws SQLException {
        //可以不要Admin(),每次需要admin及password的时候就查询mysql，保证实时更新，避免Admin.adminPassword确定之后，手动
        //更改数据库之后出现Admin.adminPassword于数据库中数据不一致的问题。
        try {
            String queryAdmin = "SELECT * FROM Admin WHERE admin='admin'";
            PreparedStatement preparedStatementAdmin = connection.prepareStatement(queryAdmin);
            ResultSet resultSetAdmin = preparedStatementAdmin.executeQuery();
            // 提取结果集中的列值
            if (resultSetAdmin.next()) {
                admin = resultSetAdmin.getString("admin");
                adminPassword = resultSetAdmin.getString("adminPassword");
            } else {
                System.out.println("未找到匹配的数据。");
            }
            // 关闭连接和相关资源
            resultSetAdmin.close();
            preparedStatementAdmin.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void UpdateAdminPassword() throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM Admin WHERE admin='admin'");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        adminPassword = resultSet1.getString("adminPassword");
        int judgePassword = 0;//用于判断是否成功修改密码或退出修改密码
        while (judgePassword!=1){
            System.out.print("请输入原密码：");
            String password = scanner.next();
            if(password.equals(adminPassword)){
                int judgeNewPassword = 0;//用于判断两次密码输入是否一致，一致则结束循环
                while (judgeNewPassword != 1){
                    System.out.print("请输入新密码：");
                    String newPassword1 = scanner.next();
                    System.out.print("请确认新密码：");
                    String newPassword2 = scanner.next();
                    if(newPassword1.equals(newPassword2)){
                        adminPassword=newPassword1;
                        System.out.println("新密码修改成功！新密码为："+adminPassword);
                        //修改mysql表
                        try {
                            String updatePasswordSql="update admin set adminPassword=? where admin='admin'";
                            //预处理SQL语句
                            PreparedStatement preparedStatement = connection.prepareStatement(updatePasswordSql);
                            preparedStatement.setString(1,adminPassword);
                            int n = preparedStatement.executeUpdate();
                            if(n>0){
                                System.out.println("修改成功");
                            }else {
                                System.out.println("修改失败");
                            }
                            } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        judgePassword=1;
                        judgeNewPassword=1;
                    }
                    else{
                        System.out.println("两次密码输入不一致！请重新输入!");
                    }
                }
            }else{
                System.out.print("原密码错误！是否继续？（y/n）");
                String wrongPassword = scanner.next();
                if(wrongPassword.equals("n"))//字符串必须是"",不能写成'n'
                    judgePassword=1;
            }
        }
    }

    void UpdateUserPassword() throws SQLException {
        String updateUserID;
        while (true){
            System.out.print("请输入用户ID：");
            updateUserID = scanner.next();
            if(QueryUserID((updateUserID))){
                // 找到用户
                int judgeNewPassword = 0;//用于判断两次密码输入是否一致，一致则结束循环
                while (judgeNewPassword != 1){
                    System.out.print("请输入新密码：");
                    String newPassword1 = scanner.next();
                    System.out.print("请确认新密码：");
                    String newPassword2 = scanner.next();
                    if(newPassword1.equals(newPassword2)){
                        String updateUserPassword = "UPDATE User SET userPassword = ? WHERE userID = ?";
                        PreparedStatement statementUpdate = connection.prepareStatement(updateUserPassword);
                        statementUpdate.setString(1, newPassword1);
                        statementUpdate.setString(2, updateUserID);
                        //检验是否更新成功
                        statementUpdate.executeUpdate();
                        statementUpdate.close();
                        System.out.println("用户 "+updateUserID+" 的新密码修改成功！新密码为："+newPassword1);
                        judgeNewPassword=1;
                        return;
                        //更新用户密码
                    }
                    else{
                        System.out.println("两次密码输入不一致！请重新输入!");
                    }
                }
            }else {
                System.out.println("该用户不存在！");
            }
            /*try {
                // 执行查询
                String queryUserID = "SELECT * FROM user WHERE userID = ?";
                PreparedStatement statement = connection.prepareStatement(queryUserID);
                statement.setString(1, String.valueOf(updateUserID));
                ResultSet resultSet = statement.executeQuery();
                // 判断查询结果
                if (resultSet.next()) {
                    // 找到用户
                    int judgeNewPassword = 0;//用于判断两次密码输入是否一致，一致则结束循环
                    while (judgeNewPassword != 1){
                        System.out.print("请输入新密码：");
                        String newPassword1 = scanner.next();
                        System.out.print("请确认新密码：");
                        String newPassword2 = scanner.next();
                        if(newPassword1.equals(newPassword2)){
                            String updateUserPassword = "UPDATE User SET userPassword = ? WHERE userID = ?";
                            PreparedStatement statementUpdate = connection.prepareStatement(updateUserPassword);
                            statementUpdate.setString(1, newPassword1);
                            statementUpdate.setString(2, updateUserID);
                            //检验是否更新成功
                            statementUpdate.executeUpdate();
                            statementUpdate.close();
                            System.out.println("用户 "+updateUserID+" 的新密码修改成功！新密码为："+newPassword1);
                            judgeNewPassword=1;
                            return;
                            //更新用户密码
                        }
                        else{
                            System.out.println("两次密码输入不一致！请重新输入!");
                        }
                    }
                    // 关闭连接
                    resultSet.close();
                    statement.close();
                } else {
                    System.out.println("该用户不存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
        }
        //System.out.println(user.GetUserPassword());
    }

    void DisplayUserInformation() throws SQLException {
        try {
            String selectUserInfo = "SELECT * from User";
            String COUNT = "SELECT COUNT(*) AS count from User";
            Statement statement = connection.createStatement();
            ResultSet resultSetCount = statement.executeQuery(COUNT);
            resultSetCount.next();
            int count = resultSetCount.getInt("count");
            if(count <= 0){
                System.out.println("无用户信息！");
            } else {
                ResultSet resultSet = statement.executeQuery(selectUserInfo);
                while (resultSet.next()){
                    System.out.print("用户ID："+resultSet.getString("userID"));
                    System.out.print("\t用户名："+resultSet.getString("username"));
                    System.out.print("\t用户级别："+resultSet.getString("userLevel"));
                    System.out.println("\t用户注册时间："+resultSet.getString("userRegisterDate"));
                    System.out.print("累计消费总金额："+resultSet.getFloat("userTotalConsume"));
                    System.out.print("\t用户手机号："+resultSet.getString("userPhoneNumber"));
                    System.out.println("\t用户邮箱："+resultSet.getString("userEmail"));
                }
                resultSet.close();
            }
            statement.close();
            resultSetCount.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void DeleteUser() throws SQLException {
        System.out.print("输入删除客户的用户ID：");
        String userID = scanner.next();
        if (QueryUserID(userID)){
            //说明ID存在
            System.out.println("********！警告！********");
            System.out.print("是否删除客户ID: "+userID+" 的信息？（y/n）:");
            String judgeDelete = scanner.next();
            if(judgeDelete.equals("y")){
                String DeleteUserSql = "DELETE FROM User where userID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(DeleteUserSql);
                preparedStatement.setString(1,userID);
                if (preparedStatement.executeUpdate()>0){
                    System.out.println("删除客户ID："+userID+" 用户的信息成功！");
                }else{
                    System.out.println("用户删除失败！");
                }
            } else {
                System.out.println("取消删除客户信息操作!");
            }
        }else{
            System.out.println("未找到该用户信息！请检查用户名是否错误！");
        }
    }

    void UnlockUser() throws SQLException {
        System.out.print("输入需要解锁的用户ID：");
        String userID = scanner.next();
        if (QueryUserID(userID)){
            String querySql = "SELECT * from User where userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(querySql);
            preparedStatement.setString(1,userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int lock = resultSet.getInt("userLock");
            if(lock == 0){
                System.out.println("该用户未被锁定！");
            }else{
                String updateSql = "UPDATE User SET userLock = 0 where userID = ?";
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateSql);
                preparedStatementUpdate.setString(1,userID);
                int updateJudge = preparedStatementUpdate.executeUpdate();
                if(updateJudge > 0){
                    System.out.println("解锁成功！");
                }else {
                    System.out.println("解锁失败！");
                }
            }
        }else{
            System.out.println("该用户不存在！");
        }
    }

    boolean QueryUserID(String userID) throws SQLException {
        try {
            String queryUserID = "SELECT * FROM user WHERE userID = ?";
            PreparedStatement statement = connection.prepareStatement(queryUserID);
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();
            // 判断查询结果
            if (resultSet.next()) {
                statement.close();
                resultSet.close();
                return true;
            }else {
                statement.close();
                resultSet.close();
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean QueryUserName(String userName){
        try {
            String queryUserName = "SELECT * FROM user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(queryUserName);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            // 判断查询结果
            if (resultSet.next()) {
                statement.close();
                resultSet.close();
                return true;
            }else {
                statement.close();
                resultSet.close();
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    void DisplayGoodsInformation(){
        try {
            String selectGoodsInfo = "SELECT * from Store";
            String COUNT = "SELECT COUNT(*) AS count from Store";
            Statement statement = connection.createStatement();
            ResultSet resultSetCount = statement.executeQuery(COUNT);
            resultSetCount.next();
            int count = resultSetCount.getInt("count");
            if(count <= 0){
                System.out.println("无商品信息！");
            } else {
                ResultSet resultSet = statement.executeQuery(selectGoodsInfo);
                while (resultSet.next()){
                    System.out.print("商品编号："+resultSet.getString("goodsID"));
                    System.out.print("\t商品名称："+resultSet.getString("GoodsName"));
                    System.out.print("\t生产厂家："+resultSet.getString("manufacturer"));
                    System.out.print("\t生产日期："+resultSet.getString("produceDate"));
                    System.out.print("\t型号："+resultSet.getString("modelSize"));
                    System.out.print("\t进货价："+resultSet.getFloat("purchasePrice"));
                    System.out.print("\t零售价格："+resultSet.getFloat("sellPrice"));
                    System.out.println("\t数量："+resultSet.getInt("remainNumber"));
                }
                resultSet.close();
            }
            statement.close();
            resultSetCount.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void AddGoods() throws SQLException {
        System.out.print("请输入商品ID(1-6位数字)：");
        String goodsID=scanner.next();
        while (isGoodsIDExists(goodsID)) {
            System.out.print("该商品ID已存在！请重新输入商品ID：");
            goodsID = scanner.next();
        }
        System.out.print("请输入商品名称：");
        String goodsName = scanner.next();
        while (isGoodsNameExists(goodsName)) {
            System.out.print("该商品名称已存在！请重新输入商品名称：");
            goodsName = scanner.next();
        }
        System.out.print("请输入生产厂家：");
        String manufacturer=scanner.next();
        System.out.print("请输入生产日期：");
        String produceDate=scanner.next();
        System.out.print("请输入商品型号：");
        String modelSize=scanner.next();
        System.out.print("请输入商品进货价：");
        float purchasePrice=scanner.nextFloat();
        System.out.print("请输入商品零售价格：");
        float sellPrice=scanner.nextFloat();
        System.out.print("请输入商品数量：");
        int remainNumber=scanner.nextInt();
        String insertGoodsSql = "INSERT INTO Store (goodsName, goodsID, manufacturer,produceDate,modelSize" +
                ",purchasePrice,sellPrice,remainNumber) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertGoodsSql);
        preparedStatement.setString(1, goodsName);
        preparedStatement.setString(2, goodsID);
        preparedStatement.setString(3, manufacturer);
        preparedStatement.setString(4, produceDate);
        preparedStatement.setString(5, modelSize);
        preparedStatement.setFloat(6, purchasePrice);
        preparedStatement.setFloat(7, sellPrice);
        preparedStatement.setInt(8, remainNumber);
        int  resultSet = preparedStatement.executeUpdate();
        if (resultSet > 0){
            System.out.println("添加商品成功！");
        }
        preparedStatement.close();
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
    boolean isGoodsNameExists(String newGoodsName) throws SQLException {
        String selectQueryName = "SELECT * FROM Store WHERE goodsName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryName);
        preparedStatement.setString(1, newGoodsName);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }
    void ModifyGoods() throws SQLException {
        int judgeModify=-1;
        String selectGoodsIDSql = "SELECT * FROM Store WHERE goodsID = ?";
        System.out.print("请选择修改商品的编号：");
        String goodsID = scanner.next();
        if  (isGoodsIDExists(goodsID)){
            PreparedStatement preparedStatement = connection.prepareStatement(selectGoodsIDSql);
            preparedStatement.setString(1,goodsID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String goodsName = resultSet.getString("goodsName");
            String manufacturer = resultSet.getString("manufacturer");
            String produceDate = resultSet.getString("produceDate");
            String modelSize = resultSet.getString("modelSize");
            float purchasePrice = resultSet.getFloat("purchasePrice");
            float sellPrice = resultSet.getFloat("sellPrice");
            int remainNumber = resultSet.getInt("remainNumber");
            System.out.print("商品编号："+goodsID);
            System.out.print("  商品名称："+goodsName);
            System.out.print("  生产厂家："+manufacturer);
            System.out.print("  生产日期："+produceDate);
            System.out.print("  型号："+modelSize);
            System.out.print("  进货价："+purchasePrice);
            System.out.print("  零售价格："+sellPrice);
            System.out.println("  数量："+remainNumber);
            while (judgeModify!=0){
                System.out.print("请选择修改内容:\n（ 1、商品名称 2、生产厂家" +
                        " 3、生产日期 4、型号 5、进货价 6、零售价 7、数量 0、退出）");
                judgeModify = scanner.nextInt();
                switch (judgeModify){
                    case 0:{
                        judgeModify = 0;
                        break;
                    }
                    case 1:{
                        System.out.print("请输入修改后的商品名称：");
                        goodsName=scanner.next();
                        while (isGoodsNameExists(goodsName)){
                            System.out.print("商品名称已被使用！请重新输入：");
                            goodsName=scanner.next();
                        }
                        break;
                    }
                    case 2:{
                        System.out.print("请输入修改后的生产厂家：");
                        manufacturer=scanner.next();
                        break;
                    }
                    case 3:{
                        System.out.print("请输入修改后的生产日期：");
                        produceDate=scanner.next();
                        break;
                    }
                    case 4:{
                        System.out.print("请输入修改后的商品型号：");
                        modelSize=scanner.next();
                        break;
                    }
                    case 5:{
                        System.out.print("请输入修改后的进货价：");
                        purchasePrice=scanner.nextFloat();
                        break;
                    }
                    case 6:{
                        System.out.print("请输入修改后的零售价：");
                        sellPrice=scanner.nextFloat();
                        break;
                    }
                    case 7:{
                        System.out.print("请输入修改后的商品数量：");
                        remainNumber=scanner.nextInt();
                        break;
                    }
                    default:{
                        System.out.println("选择错误！请重新选择！");
                        break;
                    }
                }
            }
            //对shoppingCard里面的内容也要同步修改
            String updateGoodsSql = "UPDATE Store SET goodsName = ?, manufacturer = ?, produceDate = ?, " +
                    "modelSize = ?, purchasePrice = ?, sellPrice = ?, remainNumber = ? where goodsID= ?";
            String updateShoppingCartSql = "UPDATE shoppingCart SET goodsName = ?, manufacturer = ?, produceDate = ?, " +
                    "modelSize = ?, purchasePrice = ? where goodsID= ?";
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateGoodsSql);
            PreparedStatement preparedStatementShoppingCart = connection.prepareStatement(updateShoppingCartSql);
            preparedStatementUpdate.setString(1, goodsName);
            preparedStatementUpdate.setString(2, manufacturer);
            preparedStatementUpdate.setString(3, produceDate);
            preparedStatementUpdate.setString(4, modelSize);
            preparedStatementUpdate.setFloat(5, purchasePrice);
            preparedStatementUpdate.setFloat(6, sellPrice);
            preparedStatementUpdate.setInt(7, remainNumber);
            preparedStatementUpdate.setString(8, goodsID);
            preparedStatementShoppingCart.setString(1,goodsName);
            preparedStatementShoppingCart.setString(2,manufacturer);
            preparedStatementShoppingCart.setString(3,produceDate);
            preparedStatementShoppingCart.setString(4,modelSize);
            preparedStatementShoppingCart.setFloat(5,sellPrice);
            preparedStatementShoppingCart.setString(6,goodsID);
            //检验是否更新成功
            int executeUpdate = preparedStatementUpdate.executeUpdate();
            int executeShoppingCartUpdate = preparedStatementShoppingCart.executeUpdate();
            if (executeUpdate  > 0 && executeShoppingCartUpdate > 0){
                System.out.println("商品信息更新成功");
            }else {
                System.out.println("商品信息更新失败");
            }
            preparedStatement.close();
            preparedStatementUpdate.close();
            preparedStatementShoppingCart.close();
            resultSet.close();
        } else {
            System.out.println("不存在该商品！请检查商品ID！");
        }
    }

    void DeleteGoods() throws SQLException {
        System.out.print("输入删除的商品编号：");
        String goodsID = scanner.next();
        if(isGoodsIDExists(goodsID)){
            System.out.println("********！警告！********");
            System.out.print("是否删除商品ID: "+goodsID+" 的信息？（y/n）:");
            String judgeDelete = scanner.next();
            if(judgeDelete.equals("y")){
                String deleteGoodsSql = "DELETE FROM Store where goodsID=?";
                String deleteShoppingCartSql = "DELETE FROM shoppingCart where goodsID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteGoodsSql);
                PreparedStatement preparedStatementShoppingCart = connection.prepareStatement(deleteShoppingCartSql);
                preparedStatement.setString(1,goodsID);
                preparedStatementShoppingCart.setString(1,goodsID);
                if (preparedStatement.executeUpdate() > 0 && preparedStatementShoppingCart.executeUpdate() > 0){
                    System.out.println("删除商品ID："+goodsID+" 成功！");
                }else{
                    System.out.println("商品删除失败！");
                }
            } else {
                System.out.println("取消删除商品操作!");
            }
        } else {
            System.out.println("商品未找到！请检查商品ID是否输入错误！");
        }

    }

}
