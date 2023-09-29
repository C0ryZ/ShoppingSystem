import java.sql.*;
import java.sql.SQLException;
public class Main {
    /**
     * @author 周荷钧
     * v2.0
     */
    // 创建表的SQL语句,并实现Admin表的初始数据插入。
    public static void main(String[] args) throws SQLException {
        CreateTable();
        Menu menu = new Menu();
    }
    public static void CreateTable() throws SQLException {
        // 注册MySQL驱动程序（可选的，新的驱动程序通过SPI自动注册）
        // Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = null;
        try {
            String jdbc = "jdbc:mysql://localhost:3306/shoppingSystem";
            connection = DriverManager.getConnection(jdbc, "root", "hsp");
            System.out.println("数据库shoppingSystem连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement statement = connection.createStatement();

        String isTableAdminExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'ShoppingSystem' AND table_name = 'Admin'";
        ResultSet tableAdminExistsResult = statement.executeQuery(isTableAdminExists);
        tableAdminExistsResult.next();
        int tableAdminExists = tableAdminExistsResult.getInt(1);
        if (tableAdminExists == 1) {
            System.out.println("表 Admin 已经存在!");
        } else {
            // 创建表
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Admin (admin char(5), adminPassword char(18))");
            statement.executeUpdate("INSERT INTO Admin (admin, adminPassword) VALUES ('admin', 'ynuadmin')");
            System.out.println("表 Admin 已成功创建!");
        }

        String isTableStoreExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'ShoppingSystem' AND table_name = 'Store'";
        ResultSet tableStoreExistsResult = statement.executeQuery(isTableStoreExists);
        tableStoreExistsResult.next();
        int tableStoreExists = tableStoreExistsResult.getInt(1);
        if (tableStoreExists == 1) {
            System.out.println("表 Store 已经存在!");
        } else {
            // 创建表
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Store (goodsName char(30) not null unique, goodsID char(6) not null unique, manufacturer char(30)," +
                    "produceDate Date, modelSize char(20), purchasePrice float, sellPrice float, remainNumber int)");
            System.out.println("表 Store 已成功创建!");
        }

        String isTableUserExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'ShoppingSystem' AND table_name = 'User'";
        ResultSet tableUserExistsResult = statement.executeQuery(isTableUserExists);
        tableUserExistsResult.next();
        int tableUserExists = tableUserExistsResult.getInt(1);
        if (tableUserExists == 1) {
            System.out.println("表 User 已经存在!");
        } else {
            // 创建表
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS User (username char(20) not null, userPassword char(18), userID char(6) not null unique, userLevel char(10)," +
                    "userRegisterDate Date, userTotalConsume float, userPhoneNumber char(11) not null unique, " +
                    "userEmail char(30) not null unique, userLock int, judgeLogin int, wrongLoginCount int, changePassword int)");
            System.out.println("表 User 已成功创建!");
        }
        // 检查表是否已创建
        String checkTableShoppingCartQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'ShoppingSystem' AND table_name = 'ShoppingCart'";
        ResultSet tableShoppingCartExistsResult = statement.executeQuery(checkTableShoppingCartQuery);
        tableShoppingCartExistsResult.next();
        int tableShoppingCartExists = tableShoppingCartExistsResult.getInt(1);
        // 创建表
        String createTableShoppingCart = "CREATE TABLE IF NOT EXISTS ShoppingCart (userID char(6), goodsID char(6), goodsName char(30),manufacturer char(30)," +
                "produceDate Date, modelSize char(20), purchasePrice float, purchaseQuantity int)";
        statement.executeUpdate(createTableShoppingCart);

        if (tableShoppingCartExists == 1) {
            System.out.println("表 ShoppingCart 已经存在!");
        } else {
            System.out.println("表 ShoppingCart 已成功创建!");
        }
        String checkTableShoppingRecordQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'ShoppingSystem' AND table_name = 'ShoppingRecord'";
        ResultSet tableShoppingRecordExistsResult = statement.executeQuery(checkTableShoppingRecordQuery);
        tableShoppingRecordExistsResult.next();
        int tableShoppingRecordExists = tableShoppingRecordExistsResult.getInt(1);
        String ShoppingRecord = "CREATE TABLE IF NOT EXISTS ShoppingRecord (userID char(6), goodsID char(6), goodsName char(30),manufacturer char(30)," +
                "produceDate Date, modelSize char(20), purchasePrice float, purchaseQuantity int ,totalValue float)";
        statement.executeUpdate(ShoppingRecord);
        if (tableShoppingRecordExists == 1) {
            System.out.println("表 ShoppingRecord 已经存在!");
        } else {
            System.out.println("表 ShoppingRecord 已成功创建!");
        }
    }
}