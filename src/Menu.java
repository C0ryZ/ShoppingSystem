












import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class Menu {
    Scanner scanner = new Scanner(System.in);
    Admin admin = new Admin();
    Store[] stores = new Store[100];
    User[] users = new User[100];
    Set<Integer> idSet = new HashSet<>();//用于生成唯一ID
    int usersPoint = 0;
    int storesPoint = 0;
    Menu(){
        for (int i = 0; i < users.length; i++) {
            users[i] = new User();
        }
        for (int i = 0; i < stores.length; i++) {
            stores[i] = new Store();
        }
        users[usersPoint].userName = "zhouhejun";
        users[usersPoint].SetUserPassword("Zhj123456.");
        users[usersPoint].userRegisterDate = "2023-7-14";
        users[usersPoint].userLevel = "普通用户";
        users[usersPoint].userID = 106001;
        idSet.add(106001);
        users[usersPoint].userEmail = "2428725172@qq.com";
        users[usersPoint].userPhoneNumber = "19113547971";
        users[usersPoint].userTotalConsume = 0;
        usersPoint++;
        users[usersPoint].userName = "lll";
        users[usersPoint].SetUserPassword("Zhj12345678.");
        users[usersPoint].userRegisterDate = "2023-7-14";
        users[usersPoint].userLevel = "普通用户";
        users[usersPoint].userID = 106002;
        idSet.add(106002);
        users[usersPoint].userEmail = "2428725172@qq.com";
        users[usersPoint].userPhoneNumber = "13208846858";
        users[usersPoint].userTotalConsume = 0;
        usersPoint++;
        stores[storesPoint].goodsID = "001";
        stores[storesPoint].goodsName = "洗衣液";
        stores[storesPoint].manufacturer = "蓝月亮";
        stores[storesPoint].produceDate= "2023-7-9";
        stores[storesPoint].modelSize = "1L";
        stores[storesPoint].purchasePrice = 1;
        stores[storesPoint].sellPrice = 10;
        stores[storesPoint].remainNumber = 50;
        storesPoint++;
        stores[storesPoint].goodsID = "002";
        stores[storesPoint].goodsName = "洗面奶";
        stores[storesPoint].manufacturer = "稀物集";
        stores[storesPoint].produceDate= "2023-7-13";
        stores[storesPoint].modelSize = "100ml";
        stores[storesPoint].purchasePrice = 39;
        stores[storesPoint].sellPrice = 20;
        stores[storesPoint].remainNumber = 30;
        storesPoint++;
        stores[storesPoint].goodsID = "003";
        stores[storesPoint].goodsName = "奶茶";
        stores[storesPoint].manufacturer = "云南大学";
        stores[storesPoint].produceDate= "2023-7-13";
        stores[storesPoint].modelSize = "450ml";
        stores[storesPoint].purchasePrice = 9;
        stores[storesPoint].sellPrice = 30;
        stores[storesPoint].remainNumber = 200;
        storesPoint++;
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
                    while (!validAdmin){
                        System.out.print("请输入帐号：");
                        String account = scanner.next();
                        System.out.print("请输入密码：");
                        String accountPassword = scanner.next();
                        if (account.equals(admin.admin) && accountPassword.equals(admin.adminPassword)){
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
                                        validAdminSystem = true;
                                        validAdmin = true;
                                        break;
                                    }
                                    case 1:{
                                        admin.UpdateAdminPassword();
                                        break;
                                    }
                                    case 2:{
                                        System.out.print("请输入用户ID：");
                                        int userID = scanner.nextInt();
                                        int location = SearchUserID(userID);
                                        if(location>=0){
                                            admin.UpdateUserPassword(users[location]);
                                        }else{
                                            System.out.println("用户ID不存在！");
                                        }
                                        break;
                                    }
                                    case 3:{
                                        admin.DisplayUserInformation(users,usersPoint);
                                        break;
                                    }
                                    case 4:{
                                        if(admin.DeleteUser(users,usersPoint))
                                            usersPoint--;//删除用户成功后需要将指针减一
                                        break;
                                    }
                                    case 5:{
                                        System.out.print("请输入用户ID：");
                                        int userID = scanner.nextInt();
                                        int location = SearchUserID(userID);
                                        if(location>=0){
                                            if(users[location].userLock != 1){
                                                System.out.println("该用户未被锁定！");
                                            }else{
                                                admin.UnlockUser(users[location]);
                                            }
                                        }else{
                                            System.out.println("用户ID不存在！");
                                        }
                                        break;
                                    }
                                    case 6:{
                                        admin.SearchUserName(users,usersPoint);
                                        break;
                                    }
                                    case 7:{
                                        admin.SearchUserID(users,usersPoint);
                                        break;
                                    }
                                    case 8:{
                                        admin.DisplayGoodsInformation(stores,storesPoint);
                                        break;
                                    }
                                    case 9:{
                                        admin.AddGoods(stores,storesPoint);
                                        storesPoint++;
                                        break;
                                    }
                                    case 10:{
                                        admin.ModifyGoods(stores);
                                        break;
                                    }
                                    case 11:{
                                        if(admin.DeleteGoods(stores,storesPoint))
                                            storesPoint--;//删除商品成功后需要将指针减一
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
                case 2:{
                    boolean validUser = false;
                    int userAccount = 0;
                    while (!validUser){
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
                        int location = SearchUserAccount(userAccount);
                        if(location>=0){
                            users[location].Login();
                            if(users[location].judgeLogin==1){//登录成功了
                                boolean validUserSystem = false;
                                while (!validUserSystem){
                                    //2.2用户界面
                                    System.out.println("==============================");
                                    System.out.println("=======1.密码管理==========");
                                    System.out.println("=======2.购物车管理============");
                                    System.out.println("=======3.个人信息===============");
                                    System.out.println("=======0.退出登录===================");
                                    System.out.println("===============================");
                                    System.out.print("请选择：");
                                    int choose2 = scanner.nextInt();
                                    switch (choose2){
                                        case 0:{
                                            validUserSystem = true;
                                            validUser = true;
                                            users[location].judgeLogin = 0;//退出登录时将判断是否登录的变量置0
                                            break;
                                        }
                                        case 1:{
                                            users[location].UserPassword();
                                            if(users[location].changePassword == 1){//重置、修改密码后退出登录
                                                validUserSystem = true;
                                                validUser = true;
                                            }
                                            break;
                                        }
                                        case 2:{
                                            admin.DisplayGoodsInformation(stores,storesPoint);
                                            users[location].ShoppingCartController(stores,storesPoint);
                                            break;
                                        }
                                        case 3:{
                                            users[location].UserInformation();
                                            break;
                                        }
                                    }
                                }
                            }else{
                                System.out.println("登录失败！");
                                validUser = true;
                            }
                        }else{
                            System.out.println("用户名错误！是否请重新输入？(y/n)");
                            String next = scanner.next();
                            if(next.equals("n")){
                                validUser = true;
                            }
                        }
                    }
                    break;
                }
                case 3:{
                    boolean register = users[usersPoint].Register(idSet);
                    if(register){
                        usersPoint++;
                    }
                    break;
                }
                case 4:{
                    System.out.println("****！忘记密码系统！****");
                    boolean invalid = false;
                    while (!invalid){
                        System.out.print("请输入用户ID：");
                        int userID = scanner.nextInt();
                        int search = SearchUserID(userID);
                        if(search != -1){
                            users[search].ForgetUserPassword();
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

    int SearchUserAccount(int userName){
        for (int i = 0;i < usersPoint ;i++){
            if(users[i].userID == userName){
                return i;
            }
        }
        return -1;
    }

    int SearchUserID(int userID){
        for (int i = 0;i < usersPoint ;i++){
            if(users[i].userID == userID){
                return i;
            }
        }
        return -1;
    }

}
