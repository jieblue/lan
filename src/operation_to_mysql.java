import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class operation_to_mysql {

    static Connection myconnection;//���ݿ�����
    private String url;
    private String [ ] addrs;//��ʡ�ݵ�ַ
    public operation_to_mysql()
    {
        url="https://restapi.amap.com/v3/weather/weatherInfo?key=fe250d98db59f44ee94e0bd0b3648393&city=";//һ���http
        addrs= new String[]{"110000", "120000", "310000", "340100", "350100", "440100", "450100", "520100", "620100", "130100", "230100", "410100",//��ʡ�ݵ�ַ
                "420100", "430100", "460100", "220100", "320100", "360100", "210100", "150100", "640100", "630100", "140100", "370100", "510100", "610100",
                "710000", "540100", "650100", "530100", "330100", "150000", "810000", "820000"};
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//ע������
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void mydelete(String city,String date)//ɾ������ ���� �� ʱ��
    {
        try{
            //����mysql
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            //׼��ִ��
            PreparedStatement preparedStatement=myconnection.prepareStatement("delete from weather where city like ? and date like ?");
            preparedStatement.setString(1,city);
            preparedStatement.setString(2,date);
            preparedStatement.execute();//ִ��
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (myconnection!=null)//������ر�����
                    myconnection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public  String getjsonstr(String url1)//�����Ӷ˿ڻ�ȡ�����ݣ����ص���string//����url
    {
        StringBuffer buffer=new StringBuffer();//�ַ�������
        try {
            URL url=new URL(url1);//url
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.connect();//���Ӷ˿�
            if (connection.getResponseCode()==200)//�������ݴ���
            {
                InputStream is=connection.getInputStream();//��ȡ������
                InputStreamReader isr=new InputStreamReader(is,"utf-8");//����
                BufferedReader br=new BufferedReader(isr);
                String line="";
                //  StringBuilder sb=new StringBuilder();

                while ((line=br.readLine())!=null)
                {
                    buffer.append(line);//����buffer
                }
                br.close();//�ر�
                connection.disconnect();//return
                //System.out.println(buffer.toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
         //   System.out.println("wrong");
        }return buffer.toString();//���ػ�õ�string
    }
    public    boolean myquery(String city,String date)//��ѯ���ݳ��� �� ʱ��
    {
        boolean done=false;//�Ƿ�ɹ�
        try {//�������ݿ�
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            PreparedStatement preparedStatement=myconnection.prepareStatement("select week,dayweather,nightweather,daytemp,nighttemp,daywind,nightwind,daypower,nightpower from weather where city like ? and date like ?");
            preparedStatement.setString(1,city);
            preparedStatement.setString(2,date);

            ResultSet resultSet= preparedStatement.executeQuery();//�õ�ִ�в�ѯ�ķ��ؽ��

            System.out.println("week  dayweather nightweather  daytemp  nighttemp  daywind  nightwind  daypower  nightpower");//����������

                while (resultSet.next())
                {
                    done=true;//�ɹ�
                    for (int i=1;i<=8;i++)
                    {
                        System.out.print(resultSet.getString(i)+"------");//�����Ϣ
                    }
                    System.out.println();
                }
            if (done==false)//��ѯ������Ϣ
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
                if (myconnection!=null)//�ر�����
                    myconnection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return done;//�����Ƿ�ɹ�
    }
    public   JSONObject getjsonobject(String jsonstr)//�ѻ�õ�STRING ת��Ϊ jsonobject ����
    {

        JSONObject jsonObject=JSONObject.fromObject(jsonstr);
        JSONArray jsonArray= (JSONArray) jsonObject.get("forecasts");//��ȡ����Ԥ������Ϣ
        JSONObject jsonObject1= (JSONObject) jsonArray.get(0);
        return jsonObject1;//����jsonobject ����
    }
   //// ������Ϣ
    //���������£�������һ������
    public  void myupdate(String city,String date,String dayweather,String nightweather,String daytemp,String nighttemp,String daywind, String nightwind,String daypower,String nightpower)
    {
        try{
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");

            PreparedStatement preparedStatement = myconnection.prepareStatement("update weather set dayweather=?,nightweather=?,daytemp=?,nighttemp=?,daywind=?,nightwind=?,daypower=?,nightpower=? where city=? and date=?");
//
                //��Ӧ������ֵ

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
                    preparedStatement.execute();//ִ��
                    //  preparedStatement.clearParameters();
            }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if (myconnection!=null)//�ر�
                    myconnection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void write_to_sql()//��ȫ��������Ϣд��mysql
    {
        for (int i=0;i<34;i++)
        {
            String real_url=url+addrs[i]+"&extensions=all";//����de url
            JSONObject jsonObject=getjsonobject(getjsonstr(real_url));
            myinsert(jsonObject);//д����Ϣ
        }
    }
    public  void myinsert(JSONObject myobject)//д�뵥�����е���Ϣ
    {
        try{
            myconnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/first_database?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8","root","2775856803");
            String city="";
            city=myobject.getString("city");//��ȡ������

            JSONArray jsonArray=myobject.getJSONArray("casts");//��ȡ����Ԥ������Ϣ
            System.out.println("city -> "+city);//�������

            for (int i=0;i<jsonArray.size();i++) {

                JSONObject tmp1=(JSONObject)jsonArray.get(i);
                try {//д�����ݿ�
                    //�������ݿ��� ���к����� Ϊ����������ȷ��һ���ݵ�Ψһ�ԣ�Ҳ�������´���ҳ��д������
                    //������һ�仰��д��ʱ�����ڼ����£�
                    PreparedStatement preparedStatement = myconnection.prepareStatement("insert into weather(date,week,dayweather,nightweather,daytemp,nighttemp,daywind,nightwind,daypower,nightpower,city) values(?,?,?,?,?,?,?,?,?,?,?) " +
                            "ON DUPLICATE KEY UPDATE  dayweather=?,nightweather=?,daytemp=?,nighttemp=?,daywind=?,nightwind=?,daypower=?,nightpower=?");
                    //��Ӧ������ֵ
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
                    preparedStatement.execute();//ִ��
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
                if (myconnection!=null)//�ر�
                    myconnection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
    }
    }
 }

