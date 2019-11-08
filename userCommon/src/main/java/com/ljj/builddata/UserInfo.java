package com.ljj.builddata;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class UserInfo {
    private static Random rand;
    private static  String[] email_suffix;
    private static String base;
    private static String[] telFirst;
    private static String[] terminalType;
    private static  Connection con = null; //定义一个MYSQL链接对象
    private static PreparedStatement pstmt =null; //创建声明
    static{
        rand=new Random();
        email_suffix="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
        base = "abcdefghijklmnopqrstuvwxyz0123456789";
        telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        terminalType="pc端,移动端,小程序端".split(",");
        try {
            Class.forName("com.mysql.jdbc.Driver"); //MYSQL驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private UserInfo(){};
    /**
     * 生成一批用户id
     * <br>生成规则：
     * 字母加数字的固定5位，前三位为小写字母，后两位为数字
     * @param oldUserIds 系统中原有的用户id列表，避免重复
     * @param num 要获取多少个用户编号
     * @return
     */
    public static List<String> getUserIds(List<String> oldUserIds,int num){
        List<String> ids=new ArrayList<String>();
        while(ids.size()<num){
            StringBuffer sb=new StringBuffer();
            for(int j=1;j<=5;j++){
                if(j<=3){//前三位获取字母
                    sb.append(getLetter());
                }else{//后两位用数字
                    sb.append(getNum());
                }
            }
            String userName=sb.toString();
            if(oldUserIds.contains(userName)||ids.contains(userName)){
                //已存在，重新生成一个
            }else{
                ids.add(userName);
            }
        }
        return ids;
    }

    /**
     * 生成一批密码
     * <br>生成规则：
     * 大写字母+小写字母+数字
     * @param num 要生成多少个密码
     * @param wordNum 要生成的密码长度是多少
     * @return
     */
    public static List<String> getPasswords(int num,Integer wordNum){
        int total=wordNum;//密码总位数
        List<String> passwords=new ArrayList<String>();
        while(passwords.size()<num){
            StringBuffer sb=new StringBuffer();
            int upperNum=getRadomInt(1,total-2);//大写字母位数，保留至少两位，用来放小写和数字
            int lowerNum=getRadomInt(1, total-upperNum-1);//小写字母位数，为总数减去大写字母占用的数量，再为数字区域保留至少1
            int nnum=total-upperNum-lowerNum;//最后剩余数字的位数，为总数减去大写和小写字母位数之后剩余的位数
            //随机获取到每个类型的位置index
            Map<Integer,String> indexMap=new HashMap<Integer,String>();
            while(indexMap.size()<upperNum){
                //确定大写字母的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "upper");
                }
            }
            while(indexMap.size()<upperNum+lowerNum){
                //确定小写字母的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "lower");
                }
            }
            while(indexMap.size()<total){
                //确定数字的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "nnum");
                }
            }
            //组装密码
            for(int i=0;i<total;i++){
                if("upper".equals(indexMap.get(i))){
                    sb.append(getUpper());
                }else if("lower".equals(indexMap.get(i))){
                    sb.append(getLetter());
                }else{
                    sb.append(getNum());
                }
            }
            passwords.add(sb.toString());
        }
        return passwords;
    }
    /**
     * 随机获取一个小写字母
     */
    public static char getLetter(){
        char c=(char)getRadomInt(97, 122);
        return c;
    }

    /**
     * 随机获取一个大写字母
     */
    public static char getUpper(){
        char c=(char)getRadomInt(65, 90);
        return c;
    }


    /**
     * 随机获取一个0-9的数字
     * @return
     */
    public static int getNum(){
        return getRadomInt(0, 9);
    }

    /**
     * 获取一个范围内的随机数字
     * @return
     */
    public static int getRadomInt(int min,int max){
        return rand.nextInt(max-min+1)+min;
    }
    /**
     * 获取一个范围内的随机数字
     * @return
     */
    private static long getRrandomLong(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return getRrandomLong(begin,end);
        }
        return rtn;
    }
    /**
     * 生成一批名字
     * @param num 要生成多少个名字
     * @return
     */
    public static List<String> getChineseNames(int num){
        List<String> names=new ArrayList<String>();
        while(names.size()<num) {
            String name = "";
            int chineseNameNum = (int) (Math.random() * 2 + 2);
            for (int i = 1; i <= chineseNameNum; i++) {
                name += getChinese();
            }
            names.add(name);
        }
        return names;
    }

    //获得单个汉字
    public static String getChinese(){
        String chinese = "";
        int i = (int)(Math.random()*40 + 16);
        int j = (int)(Math.random()*94 + 1);
        if(i == 55){
            j = (int)(Math.random()*89 + 1);
        }
        byte[] bytes = {(byte) (i+160),(byte) (j+160)};
        try {
            chinese =  new String(bytes, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return chinese;
    }

    /**
     * 随机生成手机号码
     * @param num 生成手机号码的个数
     * @return
     */
    public static List<String> getTelephone(int num) {
        List<String> telephones=new ArrayList<String>();
        while(telephones.size()<num) {
            String telephone ="";
            int index = getRadomInt(0, telFirst.length - 1);
            String first = telFirst[index];
            String second = String.valueOf(getRadomInt(1, 888) + 10000).substring(1);
            String thrid = String.valueOf(getRadomInt(1, 9100) + 10000).substring(1);
            telephone=first+second+thrid;
            telephones.add(telephone);
        }
        return telephones;
    }

    /**
     *  生成一批邮箱
     * @param lMin 最小邮箱长度
     * @param lMax 最大邮箱长度
     * @param num  生成的邮箱个数
     * @return
     */
    public static List<String> getEmail(int lMin,int lMax,int num) {
        int length=getRadomInt(lMin,lMax);
        List<String> emails=new ArrayList<String>();
        while(emails.size()<num) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = (int) (Math.random() * base.length());
                sb.append(base.charAt(number));
            }
            sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
            emails.add(sb.toString());
        }
        return emails;
    }

    /**
     * 生成一批性别
     * @param num
     * @return
     */
    public static List<String> getSex(int num) {
        List<String> sexs = new ArrayList<String>();
        while (sexs.size() < num)
            if (getRadomInt(0,num) % 2 == 0) {
                sexs.add("男");
            }else{
                sexs.add("女");
            }
        return sexs;
    }
    /**
     * 生成一批年龄
     * @param num 生成多少个
     * @return
     */
    public static List<Integer> getAge(int num) {
        List<Integer> ages = new ArrayList<Integer>();
        while (ages.size() < num){
            ages.add(getRadomInt(10,80));
        }

        return ages;
    }
    /**
     * 生成一批终端类型
     * @param num 生成多少个
     * @return
     */
    public static List<String> getTerminalType(int num) {
        List<String> terminalTypes = new ArrayList<String>();
        while (terminalTypes.size() < num){
            terminalTypes.add(terminalType[getRadomInt(0,2)]);
        }

        return terminalTypes;
    }

    /**
     * 生成一批时间
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param num 生成的个数
     * @return
     */
    private static List<String> getDate(String beginDate, String endDate,int num){
        //时间格式：2019-01-01
        try {
            List<String> dates=new ArrayList<String>();
            while(dates.size()<num) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date start = format.parse(beginDate);
                Date end = format.parse(endDate);

                if (start.getTime() >= end.getTime()) {
                    return null;
                }
                long date = getRrandomLong(start.getTime(), end.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                dates.add(sdf.format(new Date(date)));
            }
            return dates;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {

        int  num=100 ;
        List<String> userids=getUserIds(new ArrayList(), num);
		List<String> passwords=getPasswords(num,8);
		List<String> names=getChineseNames(num);
		List<String> telephones=getTelephone(num);
		List<String> emails=getEmail(5,8,num);
		List<String> sexs=getSex(num);
		List<Integer> ages=getAge(num);
		List<String> dates=getDate("2018-01-10","2019-11-10",num);
		List<String> terminaltypes=getTerminalType(num);

		for (int i =0;i<num;i++) {
            String userid =  userids.get(i);
            String password =  passwords.get(i);
            String name =  names.get(i);
            String telephone =  telephones.get(i);
            String email =  emails.get(i);
            String sex =  sexs.get(i);
            int age =  ages.get(i);
            String date =  dates.get(i);
            String terminaltype =  terminaltypes.get(i);
            try {

                con = DriverManager.getConnection("jdbc:mysql://192.168.2.101:3306/user_portrait", "root", "123456"); //链接本地MYSQL
                String sql = "INSERT INTO user_info (user_id,password,user_name, tel_phone,email,sex,age,register_time,user_type) " +
                        "VALUES (?,?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(sql);
                //新增一条数据
                pstmt.setString(1,userid);
                pstmt.setString(2,password);
                pstmt.setString(3,name);
                pstmt.setString(4,telephone);
                pstmt.setString(5,email);
                pstmt.setString(6,sex);
                pstmt.setInt(7,age);
                pstmt.setString(8,date);
                pstmt.setString(9,terminaltype);
                pstmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }
}