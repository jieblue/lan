public class sql_test {
    //��mysql�Ĳ�������
    private static operation_to_mysql my_sql;//���ݿ��������

    public static void main(String[] args) {
        //operation_to_mysql
        my_sql=new operation_to_mysql();//new һ�� ����

       my_sql.write_to_sql();//������д�����ݾ�//д�����ݿ�
        my_sql.myquery("������","2019-12-10");//��ѯ����
        my_sql.mydelete("������","2019-12-09");//ɾ������
      //  ��������
        my_sql.myupdate("������","2019-12-10","��","��","8","3","����","����","5","5");

        //////����֮���ٴ�д�����ݣ�����������
        my_sql.write_to_sql();;
        my_sql.myquery("������","2019-12-10");//��ѯ����
        my_sql.myquery("������","9102-12-10");//��ѯ����

    }
}
