import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
    String [][] shoppingCart = new String[100][2];//购物车容量为100 存储编号和商品名称
    int [] goodsLocation = new int[100];//记录购物车中的商品在store中的序号
    int [] goodsNumber = new int[100];//记录购物车某个商品的数量
    int shoppingCartPoint = 0;//类似栈的结构来储存购物车
    float consumeTotal = 0;//总消费
    String [][] shoppingRecord = new String[100][2];//订单历史容量为100
    Float [] sellPriceRecord = new Float[100];//
    int [] goodsNumberRecord = new int[100];//
    int shoppingRecordPoint = 0;//类似栈的结构来储存历史订单

    void SetUserPassword(String newPassword){
        userPassword=newPassword;
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

    boolean Register(Set idSet){
        //用户注册用户名可以一致但用户ID唯一
        while (true){
            System.out.print("请输入用户名（不少于5个字符）：");
            userName = scanner.next();
            if(userName.length()>=5){
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
                    System.out.print("请输入密码手机号码：");
                    userPhoneNumber = scanner.next();
                    String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                    boolean flag = false;
                    while (!flag){
                        System.out.print("请输入邮箱地址：");
                        userEmail = scanner.next();
                        Pattern p;
                        Matcher m;
                        p = Pattern.compile(regEx1);
                        m = p.matcher(userEmail);
                        if(m.matches())
                            flag = true;
                        else
                            System.out.println("输入邮箱格式错误!请重新输入！");
                    }
                    System.out.println("帐号注册成功！");
                    userID = generateUniqueId(idSet);
                    System.out.println("帐号ID为："+userID);
                    userLevel = "普通用户";
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    userRegisterDate = dateFormat.format(date);
                    System.out.println("注册日期："+ userRegisterDate);
                    userTotalConsume = 0;
                    return true;
                }else {
                    System.out.println("输入格式错误！请重新输入！");
                }
            } else
                System.out.println("用户名输入长度不合法！请重新输入！");
        }
    }

    int generateUniqueId(Set idSet){
        int id;
        Random random = new Random();
        do {
            // 生成6位随机数
            id = random.nextInt(900000) + 100000;
        } while (idSet.contains(id));  // 如果生成的ID已存在，重新生成
        idSet.add(id);  // 将生成的ID加入集合
        return id;
    }

    void Login(){
        if(userLock == 1){
            System.out.println("该用户已被锁定，无法登录！");
        }else{
            if(judgeLogin==1){
                System.out.println("该用户已登录！");
            }else{
                while (judgeLogin !=1 ) {
                    if(wrongLoginCount==5){
                        userLock=1;
                        wrongLoginCount=0;//归零
                        System.out.println("该用户已被锁定，无法登录！");
                        break;
                    }
                    System.out.print("请输入用户密码：");
                    String password = scanner.next();
                    if(password.equals(userPassword)){
                        wrongLoginCount=0;
                        userLock=0;
                        judgeLogin=1;
                        System.out.println("登录成功！");
                    }else{
                        wrongLoginCount++;
                        System.out.print("密码输入错误！是否请重新输入？（y/n）:");
                        String next = scanner.next();
                        if (next.equals("n")){
                            return;
                        }
                    }
                }
            }
        }
    }

    void UserPassword(){
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

    void UpdateUserPassword(){
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
                            changePassword = 1;
                            judgeLogin = 0;
                            break;
                        }else{
                            System.out.println("密码格式错误！请重新输入！");
                        }
                    }
                    else{
                        System.out.println("两次密码输入不一致！请重新输入!");
                    }
                }
            }
        }else{
            System.out.println("请先登录！");
        }
    }

    void ForgetUserPassword(){
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
                    System.out.println("密码已重置成功！将通过用户邮箱发送！即将退出登录！"+"!!该密码通过邮箱发送，但为了方便后续登录验证，在这里看一下重置的密码:"+GetUserPassword());
                }else{
                    System.out.println("密码已重置成功！将通过用户邮箱发送！"+"!!该密码通过邮箱发送，但为了方便后续登录验证，在这里看一下重置的密码:"+GetUserPassword());
                }
                changePassword = 1;
                judgeLogin = 0;
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
        /*int i = 0;
        if (randomPassword.matches(REG_NUMBER)) i++;
        if (randomPassword.matches(REG_LOWERCASE)) i++;
        if (randomPassword.matches(REG_UPPERCASE)) i++;
        if (randomPassword.matches(REG_SYMBOL)) i++;
        if (i > 3) {
            System.out.println(i+" "+len+" 新密码修改成功！新密码为："+randomPassword);
        }else{
            System.out.println("密码格式错误！请重新输入！");
        }*/
        return randomPassword;
    }

    void ShoppingCartController(Store[] stores,int length){
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
                    AddGoods(stores,length);
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
                    Pay(stores);
                    break;
                }
                case 5:{
                    ShoppingRecord();
                    break;
                }
                default:
                    System.out.println("输入错误！请重新输入！");
            }
        }
    }

    void AddGoods(Store[] stores,int length) {
        //添加时需要比较商品数量，如果商品数量不足则添加失败并提示剩余数量
        String goodsID = null;
        boolean validID = false;
        int location = 0;//商品的位置
        while (!validID) {
            System.out.print("请输入商品编号：");
            goodsID = scanner.next();
            for(location =0; location<length;location++){
                if(stores[location].goodsID.equals(goodsID)){
                    validID=true;
                    break;
                }
            }
            if(location == length){
                System.out.println("输入商品编号不存在！是否继续？（y/n）");
                String next = scanner.next();
                if(next.equals("n"))
                    return;
            }
        }
        int goodsNumbers;
        boolean validInput = false; //输入的数量是否为整数
        while (!validInput) {
            try {
                System.out.print("请输入商品数量：");
                goodsNumbers = scanner.nextInt();
                if (goodsNumbers > 0) {
                    if (stores[location].remainNumber > goodsNumbers){
                        shoppingCart[shoppingCartPoint][0] = goodsID;
                        shoppingCart[shoppingCartPoint][1] = stores[location].goodsName;
                        goodsNumber[shoppingCartPoint] = goodsNumbers;
                        goodsLocation[shoppingCartPoint] = location;
                        System.out.println(stores[location].goodsName+"添加成功，数量："+goodsNumbers);
                        shoppingCartPoint++;
                        validInput = true; // 输入为整数，跳出循环
                        //stores[location].remainNumber -= goodsNumber;付款时才需要减去
                    } else {
                        System.out.println("库存数量不足！请重新输入数量！");
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

    void RemoveGoods(){
        if(shoppingCartPoint == 0){
            System.out.println("购物车为空！");
        }else{
            for (int i=0 ; i<shoppingCartPoint ;i++){
                System.out.println("序号："+i+": "+"商品编号："+shoppingCart[i][0]+"  商品名称："+shoppingCart[i][1]+" 商品数量："+goodsNumber[i]);
            }//展示购物车里面的内容
            boolean validLocation = false;
            while (!validLocation){
                System.out.print("请选择需要移除的商品序号：");
                int location = scanner.nextInt();
                if(location>=0 && location < shoppingCartPoint){
                    System.out.println("***警告！是否将购物车中的 " + shoppingCart[location][1]+ " 移除购物车？(y/n)***");
                    String remove = scanner.next();
                    if(remove.equals("y")){
                        for (int i = location; i < shoppingCartPoint ; i++){
                            shoppingCart[i] = shoppingCart[i+1];
                            goodsNumber[i] = goodsNumber[i+1];
                            goodsLocation[i] = goodsLocation[i+1];
                        }
                        validLocation = true;
                        shoppingCartPoint--;
                    }else{
                        System.out.println("取消移除购物车操作。");
                        break;
                    }
                }else {
                    System.out.println("选择失败！请重新选择！");
                }
            }
        }
    }

    void ChangeNumber(){
        //修改后数量小于0则移除改商品
        if(shoppingCartPoint == 0){
            System.out.println("购物车为空！");
        }else {
            for (int i=0 ; i<shoppingCartPoint ;i++){
                System.out.println("序号："+i+": "+"商品编号："+shoppingCart[i][0]+"  商品名称："+shoppingCart[i][1]+" 商品数量："+goodsNumber[i]);
            }
            System.out.print("请选择需要修改数量的商品序号：");
            int location = scanner.nextInt();
            System.out.print("将数量修改为:");
            int changeNumber = scanner.nextInt();
            if(changeNumber>0){
                //shoppingCart[location][1]=Integer.toString(changeNumber);
                goodsNumber[location] = changeNumber;
            }else{
                //数量小于等于0，即移除该商品
                for (int i = location; i<shoppingCartPoint ; i++){
                    shoppingCart[i]=shoppingCart[i+1];
                    goodsNumber[i] = goodsNumber[i+1];
                    goodsLocation[i] = goodsLocation[i+1];
                }
                shoppingCartPoint--;
            }
        }
    }

    void Pay(Store[] stores){
        //支付后需要清空支付的商品
        //支付后更新购物记录
        //支付后改变商品库存
        if(shoppingCartPoint == 0){
            System.out.println("购物车为空！");
        }else{
            System.out.println("购物车为：");
            for (int i = 0; i < shoppingCartPoint; i++){
                //System.out.print("goodsLocation："+goodsLocation[i]);
                System.out.print("商品编号："+stores[goodsLocation[i]].goodsID);
                System.out.print("  商品名称："+stores[goodsLocation[i]].goodsName);
                System.out.print("  生产日期："+stores[goodsLocation[i]].produceDate);
                System.out.print("  零售价格："+stores[goodsLocation[i]].sellPrice);
                System.out.println("  数量："+goodsNumber[i]);
            }
            System.out.print("是否付款？（y/n）");
            String pay = scanner.next();
            if(pay.equals("y")) {
                for (int i = 0; i < shoppingCartPoint; i++){
                    if (stores[goodsLocation[i]].remainNumber > goodsNumber[i]){//库存数量足够，购买可以成功
                        float consume = goodsNumber[i]*stores[goodsLocation[i]].sellPrice;
                        stores[goodsLocation[i]].remainNumber -= goodsNumber[i];//修改商品库存
                        shoppingRecord[shoppingRecordPoint][0] = shoppingCart[i][0];
                        shoppingRecord[shoppingRecordPoint][1] = shoppingCart[i][1];
                        sellPriceRecord[shoppingRecordPoint] = stores[goodsLocation[i]].sellPrice;
                        goodsNumberRecord[shoppingRecordPoint] = goodsNumber[i];
                        shoppingRecordPoint++;//将购买记录保存在相应的变量中
                        consumeTotal += consume;
                        System.out.println("购买"+stores[goodsLocation[i]].goodsName+",售价"+stores[goodsLocation[i]].sellPrice+",购买数量"+goodsNumber[i]+"成功！花费"+consume+"元。");
                    }else{//库存数量不足
                        System.out.println("商品"+shoppingCart[i][1]+"库存不足！购买失败！");
                        System.out.print("是否更改购买数量（y/n）:");
                        String changeGoodsNumber = scanner.next();
                        if (changeGoodsNumber.equals("y")){
                            while (true){
                                System.out.print("将数量修改为:");
                                int changeNumber = scanner.nextInt();
                                if(changeNumber < stores[goodsLocation[i]].remainNumber){
                                    goodsNumber[i] = changeNumber;
                                    i--;
                                    System.out.println("数量修改成功！");
                                    break;
                                }else{
                                    System.out.println("库存不足！数量修改失败,请重新输入！");
                                }
                            }
                        } else {
                            System.out.println("商品"+shoppingCart[i][1]+"库存数量不足，购买失败！");
                        }
                    }
                }
                System.out.println("总花费:"+consumeTotal+"元。");
                setUserLevel();//
                shoppingCartPoint = 0;//清空购物车，指针回到栈底
            }else{
                System.out.println("取消付款");
            }
        }
    }

    void ShoppingRecord(){
        //只有付款后，才会更新记录
        for (int i = 0; i < shoppingRecordPoint; i++){
            System.out.println("商品编号："+shoppingRecord[i][0]+"  \t商品名称："+shoppingRecord[i][1]+"  \t购买数量："+goodsNumberRecord[i]+"\t售价:"+sellPriceRecord[i]);
        }
        System.out.println("消费总金额："+consumeTotal);
    }

    void setUserLevel(){
        String level = userLevel;
        if(500<=consumeTotal && consumeTotal<1000){
            userLevel = "铜牌用户";
        }
        if(1000<=consumeTotal && consumeTotal<3000){
            userLevel = "银牌用户";
        }
        if(3000<=consumeTotal){
            userLevel = "金牌用户";
        }
        if(!level.equals(userLevel)){
            System.out.println("恭喜你！升至"+userLevel);
        }
    }
}

