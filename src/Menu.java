import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu  {
    String url = "jdbc:mysql://localhost:3306/shoppingSystem";
    String mysqlUsername = "root";//your_username
    String mysqlPassword = "hsp";//your_password
    Connection connection;
    Scanner scanner = new Scanner(System.in);
    Admin admin = new Admin();
    User user = new User();
    Menu() throws SQLException {
        try {
            connection = DriverManager.getConnection(url, mysqlUsername, mysqlPassword);//链接到数据库
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("************************");
        System.out.println("***欢迎来到网上购物系统！***");
        System.out.println("************************");
        boolean validSystem = false;
        while (!validSystem){
            //1.主界面
            System.out.println("==========================");
            System.out.println("========1.管理员登录========");
            System.out.println("========2.用户登录==========");
            System.out.println("========3.用户注册==========");
            System.out.println("========4.忘记密码==========");
            System.out.println("========0.退出=============");
            System.out.println("==========================");
            System.out.print("请选择：");
            int choose1 = scanner.nextInt();
            switch (choose1){
                case 0:{
                    validSystem = true;
                    break;
                }
                case 1:{
                    boolean validAdmin = false;
                    String queryAdmin = "SELECT * FROM Admin WHERE admin='admin'";
                    // 准备查询语句并执行查询
                    PreparedStatement preparedStatementAdmin = connection.prepareStatement(queryAdmin);
                    ResultSet resultSetAdmin = preparedStatementAdmin.executeQuery();
                    String mysqlAdmin;
                    String mysqlAdminPassword;
                    if (resultSetAdmin.next()) {
                        mysqlAdmin = resultSetAdmin.getString("admin");
                        mysqlAdminPassword = resultSetAdmin.getString("adminPassword");
                    } else {
                        System.out.println("未找到匹配的数据。");
                        break;
                    }
                    while (!validAdmin){
                        System.out.print("请输入帐号：");
                        String adminAccount = scanner.next();
                        System.out.print("请输入密码：");
                        String adminPassword = scanner.next();
                        if (adminAccount.equals(mysqlAdmin) && adminPassword.equals(mysqlAdminPassword)){
                            boolean validAdminSystem = false;
                            System.out.println("欢迎来到管理员系统！");
                            while (!validAdminSystem){
                                //2.1管理员界面
                                System.out.println("==============================");
                                System.out.println("=======1.修改管理员密码==========");
                                System.out.println("=======2.重置用户密码============");
                                System.out.println("=======3.用户信息===============");
                                System.out.println("=======4.删除用户===============");
                                System.out.println("=======5.解除登录锁==============");
                                System.out.println("=======6.根据用户名查找用户=======");
                                System.out.println("=======7.根据用户ID查找用户=======");
                                System.out.println("=======8.商品信息================");
                                System.out.println("=======9.添加商品================");
                                System.out.println("=======10.修改商品================");
                                System.out.println("=======11.删除商品===============");
                                System.out.println("=======0.退出===================");
                                System.out.println("===============================");
                                System.out.print("请选择：");
                                int choose2 = scanner.nextInt();
                                switch (choose2){
                                    case 0:{
                                        validAdminSystem = true;//退出管理员界面
                                        validAdmin = true;//返回到系统页面
                                        break;
                                    }
                                    case 1:{
                                        admin.UpdateAdminPassword();
                                        break;
                                    }
                                    case 2:{
                                        admin.UpdateUserPassword();
                                        break;
                                    }
                                    case 3:{
                                        admin.DisplayUserInformation();
                                        break;
                                    }
                                    case 4:{
                                        admin.DeleteUser();
                                        break;
                                    }
                                    case 5:{
                                        admin.UnlockUser();
                                        break;
                                    }
                                    case 6:{
                                        System.out.print("请输入用户名:");
                                        String userName = scanner.next();
                                        if(!admin.QueryUserName(userName)){
                                            System.out.println("该用户不存在！");
                                        }else{
                                            //用户存在
                                            String queryUserByName = "SELECT * FROM User where username = ?";
                                            PreparedStatement preparedStatement = connection.prepareStatement(queryUserByName);
                                            preparedStatement.setString(1,userName);
                                            ResultSet resultSet = preparedStatement.executeQuery();
                                            while (resultSet.next()){
                                                System.out.print("用户ID："+resultSet.getString("userID"));
                                                System.out.print("\t用户名："+resultSet.getString("username"));
                                                System.out.print("\t用户级别："+resultSet.getString("userLevel"));
                                                System.out.print("\t用户注册时间："+resultSet.getString("userRegisterDate"));
                                                System.out.print("\t累计消费总金额："+resultSet.getFloat("userTotalConsume"));
                                                System.out.print("\t用户手机号："+resultSet.getString("userPhoneNumber"));
                                                System.out.println("\t用户邮箱："+resultSet.getString("userEmail"));
                                            }
                                            resultSet.close();
                                            preparedStatement.close();
                                        }
                                        break;
                                    }
                                    case 7:{
                                        System.out.print("请输入用户ID:");
                                        String userID = scanner.next();
                                        if(!admin.QueryUserID(userID)){
                                            System.out.println("该用户不存在！");
                                        }else{
                                            //用户存在
                                            String queryUserById = "SELECT * FROM User where userID = ?";
                                            PreparedStatement preparedStatement = connection.prepareStatement(queryUserById);
                                            preparedStatement.setString(1,userID);
                                            ResultSet resultSet = preparedStatement.executeQuery();
                                            if (resultSet.next()){
                                                System.out.print("用户ID："+resultSet.getString("userID"));
                                                System.out.print("\t用户名："+resultSet.getString("username"));
                                                System.out.print("\t用户级别："+resultSet.getString("userLevel"));
                                                System.out.print("\t用户注册时间："+resultSet.getString("userRegisterDate"));
                                                System.out.print("\t累计消费总金额："+resultSet.getFloat("userTotalConsume"));
                                                System.out.print("\t用户手机号："+resultSet.getString("userPhoneNumber"));
                                                System.out.println("\t用户邮箱："+resultSet.getString("userEmail"));
                                            }
                                            resultSet.close();
                                            preparedStatement.close();
                                        }
                                        break;
                                    }
                                    case 8:{
                                        admin.DisplayGoodsInformation();
                                        break;
                                    }
                                    case 9:{
                                        admin.AddGoods();
                                        break;
                                    }
                                    case 10:{
                                        admin.ModifyGoods();
                                        break;
                                    }
                                    case 11:{
                                        admin.DeleteGoods();
                                        break;
                                    }
                                    default:{
                                        System.out.println("选择错误！请重新选择！");
                                    }
                                }
                            }
                        }else{
                            System.out.print("管理员帐号或密码错误！是否继续？（y/n）");
                            String next = scanner.next();
                            if(next.equals("n")){
                                validAdmin = true;
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    if (user.Login()) {
                        boolean validUserSystem = false;
                        while (!validUserSystem) {
                            //2.2用户界面
                            System.out.println("===============================");
                            System.out.println("=======1.密码管理================");
                            System.out.println("=======2.购物车管理==============");
                            System.out.println("=======3.个人信息================");
                            System.out.println("=======0.退出登录================");
                            System.out.println("================================");
                            System.out.print("请选择：");
                            int choose2 = scanner.nextInt();
                            switch (choose2) {
                                case 0: {
                                    validUserSystem = true;
                                    user.judgeLogin = 0;//退出登录时将判断是否登录的变量置0
                                    //更新mysql
                                    String updateSql = "UPDATE User SET judgeLogin = ? where userID = ?";
                                    PreparedStatement statementUpdate = connection.prepareStatement(updateSql);
                                    statementUpdate.setInt(1, user.judgeLogin);
                                    statementUpdate.setInt(2, user.userID);
                                    statementUpdate.executeUpdate();
                                    // 关闭连接
                                    statementUpdate.close();
                                    break;
                                }
                                case 1: {
                                    user.UserPassword();
                                    if (user.changePassword == 1) {//重置、修改密码后退出登录
                                        validUserSystem = true;
                                    }
                                    break;
                                }
                                case 2: {
                                    System.out.println("所有商品如下：");
                                    admin.DisplayGoodsInformation();
                                    user.ShoppingCartController();
                                    break;
                                }
                                case 3: {
                                    user.UserInformation();
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("登录失败！");
                    }
                    break;
                }
                case 3:{
                    user.Register();
                    break;
                }
                case 4:{
                    System.out.println("****！忘记密码系统！****");
                    boolean invalid = false;
                    String selectQueryID = "select * from User where userID = ?";
                    while (!invalid){
                        System.out.print("请输入用户ID：");
                        String userID = scanner.next();
                        PreparedStatement preparedStatement = connection.prepareStatement(selectQueryID);
                        preparedStatement.setString(1, userID);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        boolean exists = resultSet.next();
                        if(exists){
                            //更新user中的信息
                            user = new User(userID);
                            user.ForgetUserPassword();
                            resultSet.close();
                            preparedStatement.close();
                            break;
                        }else{
                            System.out.print("未找到改用户！是否继续？（y/n）: ");
                            String continueForget = scanner.next();
                            if (continueForget.equals("n")){
                                invalid = true;
                            }
                        }

                    }
                    break;
                }
                default:{
                    System.out.println("选择错误！请重新选择！");
                }
            }
        }
    }

}