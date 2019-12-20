import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class operation_to_mysql {

    static Connection myconnection;//数据库连接
    private String url;
    private String [ ] addrs;//各省份地址
    public operation_to_mysql()
    {
        url="https://restapi.amap.com/v3/weather/weatherInfo?key=fe250d98db59f44ee94e0bd0b3648393&city=";//一半的http
        addrs= new String[]{"110000", "120000", "310000", "340100", "350100", "440100", "450100", "520100", "620100", "130100", "230100", "410100",//各省份地址
                "420100", "430100", "460100", "220100", "320100", "360100", "210100", "150100", "640100", "630100", "140100", "370100", "510100", "610100",
                "710000", "540100", "650100", "530100", "330100", "150000", "810000", "820000"};
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//注册驱动
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void mydelete(String city,String date)//删除操作 城市 和 时间
    {
        try{
            //连接mysql
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            //准备执行
            PreparedStatement preparedStatement=myconnection.prepareStatement("delete from weather where city like ? and date like ?");
            preparedStatement.setString(1,city);
            preparedStatement.setString(2,date);
            preparedStatement.execute();//执行
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (myconnection!=null)//操作完关闭链接
                    myconnection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public  String getjsonstr(String url1)//解析从端口获取的数据，返回的是string//传入url
    {
        StringBuffer buffer=new StringBuffer();//字符串缓冲
        try {
            URL url=new URL(url1);//url
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.connect();//链接端口
            if (connection.getResponseCode()==200)//访问数据存在
            {
                InputStream is=connection.getInputStream();//获取数据流
                InputStreamReader isr=new InputStreamReader(is,"utf-8");//读入
                BufferedReader br=new BufferedReader(isr);
                String line="";
                //  StringBuilder sb=new StringBuilder();

                while ((line=br.readLine())!=null)
                {
                    buffer.append(line);//加入buffer
                }
                br.close();//关闭
                connection.disconnect();//return
                //System.out.println(buffer.toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
         //   System.out.println("wrong");
        }return buffer.toString();//返回获得的string
    }
    public    boolean myquery(String city,String date)//查询根据城市 和 时间
    {
        boolean done=false;//是否成功
        try {//连接数据库
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            PreparedStatement preparedStatement=myconnection.prepareStatement("select week,dayweather,nightweather,daytemp,nighttemp,daywind,nightwind,daypower,nightpower from weather where city like ? and date like ?");
            preparedStatement.setString(1,city);
            preparedStatement.setString(2,date);

            ResultSet resultSet= preparedStatement.executeQuery();//得到执行查询的返回结果

            System.out.println("week  dayweather nightweather  daytemp  nighttemp  daywind  nightwind  daypower  nightpower");//输出天气类别

                while (resultSet.next())
                {
                    done=true;//成功
                    for (int i=1;i<=8;i++)
                    {
                        System.out.print(resultSet.getString(i)+"------");//输出信息
                    }
                    System.out.println();
                }
            if (done==false)//查询不到信息
            {
                System.out.println("-->!!!wrong information, check it!!!<--");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (myconnection!=null)//关闭连接
                    myconnection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return done;//返回是否成功
    }
    public   JSONObject getjsonobject(String jsonstr)//把获得的STRING 转换为 jsonobject 对象
    {

        JSONObject jsonObject=JSONObject.fromObject(jsonstr);
        JSONArray jsonArray= (JSONArray) jsonObject.get("forecasts");//获取天气预报的信息
        JSONObject jsonObject1= (JSONObject) jsonArray.get(0);
        return jsonObject1;//返回jsonobject 对象
    }
   //// 更新信息
    //轻量级更新，仅更新一个城市
    public  void myupdate(String city,String date,String dayweather,String nightweather,String daytemp,String nighttemp,String daywind, String nightwind,String daypower,String nightpower)
    {
        try{
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");

            PreparedStatement preparedStatement = myconnection.prepareStatement("update weather set dayweather=?,nightweather=?,daytemp=?,nighttemp=?,daywind=?,nightwind=?,daypower=?,nightpower=? where city=? and date=?");
//
                //对应的数据值

                preparedStatement.setString(1, dayweather);
                preparedStatement.setString(2, nightweather);
                preparedStatement.setString(3,daytemp);
                preparedStatement.setString(4,nighttemp);
                preparedStatement.setString(5, daywind);
                preparedStatement.setString(6, nightwind);
                preparedStatement.setString(7, daypower);
                preparedStatement.setString(8, nightpower);
                preparedStatement.setString(9, city);
                preparedStatement.setString(10, date);
                    preparedStatement.execute();//执行
                    //  preparedStatement.clearParameters();
            }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if (myconnection!=null)//关闭
                    myconnection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void write_to_sql()//把全部天气信息写入mysql
    {
        for (int i=0;i<34;i++)
        {
            String real_url=url+addrs[i]+"&extensions=all";//完整de url
            JSONObject jsonObject=getjsonobject(getjsonstr(real_url));
            myinsert(jsonObject);//写入信息
        }
    }
    public  void myinsert(JSONObject myobject)//写入单个城市的信息
    {
        try{
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            String city="";
            city=myobject.getString("city");//获取城市名

            JSONArray jsonArray=myobject.getJSONArray("casts");//获取天气预报的信息
            System.out.println("city -> "+city);//输出城市

            for (int i=0;i<jsonArray.size();i++) {

                JSONObject tmp1=(JSONObject)jsonArray.get(i);
                try {//写入数据库
                    //本地数据库以 城市和日期 为联合主键，确定一数据的唯一性，也方便重新从网页端写入数据
                    //长长的一句话，写入时，存在即更新；
                    PreparedStatement preparedStatement = myconnection.prepareStatement("insert into weather(date,week,dayweather,nightweather,daytemp,nighttemp,daywind,nightwind,daypower,nightpower,city) values(?,?,?,?,?,?,?,?,?,?,?) " +
                            "ON DUPLICATE KEY UPDATE  dayweather=?,nightweather=?,daytemp=?,nighttemp=?,daywind=?,nightwind=?,daypower=?,nightpower=?");
                    //对应的数据值
                    preparedStatement.setString(1, (String) tmp1.get("date"));
                    preparedStatement.setString(2, (String) tmp1.get("week"));
                    preparedStatement.setString(3, (String) tmp1.get("dayweather"));
                    preparedStatement.setString(4, (String) tmp1.get("nightweather"));
                    preparedStatement.setString(5, (String) tmp1.get("daytemp"));
                    preparedStatement.setString(6, (String) tmp1.get("nighttemp"));
                    preparedStatement.setString(7, (String) tmp1.get("daywind"));
                    preparedStatement.setString(8, (String) tmp1.get("nightwind"));
                    preparedStatement.setString(9, (String) tmp1.get("daypower"));
                    preparedStatement.setString(10, (String) tmp1.get("nightpower"));
                    preparedStatement.setString(11, city);
                    preparedStatement.setString(12, (String) tmp1.get("dayweather"));
                    preparedStatement.setString(13, (String) tmp1.get("nightweather"));
                    preparedStatement.setString(14, (String) tmp1.get("daytemp"));
                    preparedStatement.setString(15, (String) tmp1.get("nighttemp"));
                    preparedStatement.setString(16, (String) tmp1.get("daywind"));
                    preparedStatement.setString(17, (String) tmp1.get("nightwind"));
                    preparedStatement.setString(18, (String) tmp1.get("daypower"));
                    preparedStatement.setString(19, (String) tmp1.get("nightpower"));
                    preparedStatement.execute();//执行
                    //  preparedStatement.clearParameters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (myconnection!=null)//关闭
                    myconnection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
    }
    }
 }

