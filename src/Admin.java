import java.util.Scanner;
public class Admin {
    String admin = "admin";
    String adminPassword = "ynuadmin";
    Scanner scanner = new Scanner(System.in);
    void UpdateAdminPassword(){
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

    void UpdateUserPassword(User user){
        System.out.println(user.GetUserPassword());
        int judgeNewPassword = 0;//用于判断两次密码输入是否一致，一致则结束循环
        while (judgeNewPassword != 1){
            System.out.print("请输入新密码：");
            String newPassword1 = scanner.next();
            System.out.print("请确认新密码：");
            String newPassword2 = scanner.next();
            if(newPassword1.equals(newPassword2)){
                user.SetUserPassword(newPassword1);
                //System.out.println("用户"+UserName.userName+"的新密码修改成功！新密码为："+User.getUserPassword());
                System.out.println("用户 "+user.userName+" 的新密码修改成功！新密码为："+newPassword1);
                judgeNewPassword=1;
            }
            else{
                System.out.println("两次密码输入不一致！请重新输入!");
            }
        }
    }

    void DisplayUserInformation(User[] users,int length){
        if(length==0){
            System.out.println("无用户信息！");
        }else {
            for(int i = 0; i<length; i++){
                System.out.println("用户ID："+users[i].userID);
                System.out.println("用户名："+users[i].userName);
                System.out.println("用户级别："+users[i].userLevel);
                System.out.println("用户注册时间："+users[i].userRegisterDate);
                System.out.println("累计消费总金额："+users[i].userTotalConsume);
                System.out.println("用户手机号："+users[i].userPhoneNumber);
                System.out.println("用户邮箱："+users[i].userEmail);
                System.out.println("");
            }
        }
    }

    boolean DeleteUser(User[] users,int length){
        System.out.print("输入删除客户的用户ID：");
        int userID = scanner.nextInt();
        for(int i = 0; i<length; i++){
            if(users[i].userID == userID){
                System.out.println("********！警告！********");
                System.out.print("是否删除客户 "+users[i].userName+" 的信息？（y/n）:");
                String judgeDelete = scanner.next();
                if(judgeDelete.equals("y")){
                    while (i<length){
                        users[i]=users[i+1];
                        i++;
                    }
                    System.out.println("删除客户ID为："+userID+" 用户的信息成功！");
                    return true;
                }else{
                    System.out.println("取消删除客户信息操作");
                    return false;
                }
            }
        }
        System.out.println("未找到该用户信息！请检查用户名是否错误！");
        return false;
    }

    void UnlockUser(User user){
        user.userLock = 0;
        System.out.println("解锁成功！");
    }

    int SearchUserName(User[] users,int length){
        System.out.print("输入用户名：");
        String userName = scanner.next();
        for(int i = 0; i<length; i++){
            if(users[i].userName.equals(userName)){
                System.out.println("用户名"+userName+"已找到！");
                System.out.println("用户ID："+users[i].userID);
                System.out.println("用户级别："+users[i].userLevel);
                System.out.println("用户注册时间："+users[i].userRegisterDate);
                System.out.println("累计消费总金额："+users[i].userTotalConsume);
                System.out.println("用户手机号："+users[i].userPhoneNumber);
                System.out.println("用户邮箱："+users[i].userEmail);
                return i;
            }
        }
        System.out.println("未找到该用户信息！请检查用户名是否错误！");
        return -1;
    }

    int SearchUserID(User[] users,int length){
        System.out.print("输入用户ID：");
        int userID = scanner.nextInt();
        for(int i = 0; i<length; i++){
            if(users[i].userID==(userID)){
                System.out.println("用户ID"+userID+"已找到！");
                System.out.println("用户名："+users[i].userName);
                System.out.println("用户级别："+users[i].userLevel);
                System.out.println("用户注册时间："+users[i].userRegisterDate);
                System.out.println("累计消费总金额："+users[i].userTotalConsume);
                System.out.println("用户手机号："+users[i].userPhoneNumber);
                System.out.println("用户邮箱："+users[i].userEmail);
                return i;
            }
        }
        System.out.println("未找到该用户ID的信息！请检查用户ID是否错误！");
        return -1;
    }

    void DisplayGoodsInformation(Store[] stores,int length){
        if(length==0){
            System.out.println("无商品信息！");
        }else {
            for (int i = 0; i<length; i++){
                System.out.print("商品编号："+stores[i].goodsID);
                System.out.print("  商品名称："+stores[i].goodsName);
                System.out.print("  生产厂家："+stores[i].manufacturer);
                System.out.println("  生产日期："+stores[i].produceDate);
                System.out.print("  型号："+stores[i].modelSize);
                System.out.print("  进货价："+stores[i].purchasePrice);
                System.out.print("  零售价格："+stores[i].sellPrice);
                System.out.println("  数量："+stores[i].remainNumber);
            }
        }
    }

    void AddGoods(Store[] stores,int i){
        System.out.print("请输入商品编号：");
        stores[i].goodsID=scanner.next();
        System.out.print("请输入商品名称：");
        stores[i].goodsName=scanner.next();
        System.out.print("请输入生产厂家：");
        stores[i].manufacturer=scanner.next();
        System.out.print("请输入生产日期：");
        stores[i].produceDate=scanner.next();
        System.out.print("请输入商品型号：");
        stores[i].modelSize=scanner.next();
        System.out.print("请输入商品进货价：");
        stores[i].purchasePrice=scanner.nextFloat();
        System.out.print("请输入商品零售价格：");
        stores[i].sellPrice=scanner.nextFloat();
        System.out.print("请输入商品数量：");
        stores[i].remainNumber=scanner.nextInt();
    }

    void ModifyGoods(Store[] stores){
        int judgeModify=-1;
        System.out.print("请选择修改商品的编号：");
        String goodsId = scanner.next();
        int i;
        for (i = 0; i<stores.length; i++){
            if (goodsId.equals(stores[i].goodsID)){
                System.out.print("商品编号："+stores[i].goodsID);
                System.out.print("  商品名称："+stores[i].goodsName);
                System.out.print("  生产厂家："+stores[i].manufacturer);
                System.out.println("  生产日期："+stores[i].produceDate);
                System.out.print("  型号："+stores[i].modelSize);
                System.out.print("  进货价："+stores[i].purchasePrice);
                System.out.print("  零售价格："+stores[i].sellPrice);
                System.out.println("  数量："+stores[i].remainNumber);
                while (judgeModify!=0){
                    System.out.print("请选择修改内容\n（1、商品编号 2、商品名称 3、生产厂家" +
                            "4、生产日期 5、型号 6、进货价 7、零售价 8、数量 0、退出）");
                    judgeModify = scanner.nextInt();
                    switch (judgeModify){
                        case 0:{
                            judgeModify = 0;
                            break;
                        }
                        case 1:{
                            System.out.print("请输入修改后的商品编号：");
                            stores[i].goodsID=scanner.next();
                            break;
                        }
                        case 2:{
                            System.out.print("请输入修改后的商品名称：");
                            stores[i].goodsName=scanner.next();
                            break;
                        }
                        case 3:{
                            System.out.print("请输入修改后的生产厂家：");
                            stores[i].manufacturer=scanner.next();
                            break;
                        }
                        case 4:{
                            System.out.print("请输入修改后的生产日期：");
                            stores[i].produceDate=scanner.next();
                            break;
                        }
                        case 5:{
                            System.out.print("请输入修改后的商品型号：");
                            stores[i].modelSize=scanner.next();
                            break;
                        }
                        case 6:{
                            System.out.print("请输入修改后的进货价：");
                            stores[i].purchasePrice=scanner.nextFloat();
                            break;
                        }
                        case 7:{
                            System.out.print("请输入修改后的零售价：");
                            stores[i].sellPrice=scanner.nextFloat();
                            break;
                        }
                        case 8:{
                            System.out.print("请输入修改后的商品数量：");
                            stores[i].remainNumber=scanner.nextInt();
                            break;
                        }
                        default:{
                            System.out.println("选择错误！请重新选择！");
                            break;
                        }
                    }
                }
                break;
            }
        }
        if(i == stores.length)
            System.out.println("商品未找到！请检查商品编号是否输入错误！");
    }

    boolean DeleteGoods(Store[] stores,int length){
        System.out.print("输入删除的商品编号：");
        String goodsID = scanner.next();
        for (int i = 0 ; i<length; i++){
            if(stores[i].goodsID.equals(goodsID)){
                System.out.println("********！警告！********");
                System.out.print("是否删除商品 "+stores[i].goodsName+" 信息？（y/n）:");
                String judgeDelete = scanner.next();
                if(judgeDelete.equals("y")){
                    System.out.println("商品 "+stores[i].goodsName+" 已删除！");
                    while (i<length){
                        stores[i]=stores[i+1];
                        i++;
                    }
                    return true;
                }else{
                    System.out.println("取消删除客户信息操作");
                    return false;
                }
            }
        }
        System.out.println("商品未找到！请检查商品编号是否输入错误！");
        return false;
    }

}
