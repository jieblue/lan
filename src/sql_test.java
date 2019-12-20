public class sql_test {
    //对mysql的操作测试
    private static operation_to_mysql my_sql;//数据库操作对象

    public static void main(String[] args) {
        //operation_to_mysql
        my_sql=new operation_to_mysql();//new 一个 对象

       my_sql.write_to_sql();//把天气写入数据据//写入数据库
        my_sql.myquery("北京市","2019-12-10");//查询事例
        my_sql.mydelete("北京市","2019-12-09");//删除事例
      //  更新事例
        my_sql.myupdate("北京市","2019-12-10","阴","晴","8","3","西北","西北","5","5");

        //////几天之后，再次写入数据，即更新数据
        my_sql.write_to_sql();;
        my_sql.myquery("北京市","2019-12-10");//查询事例
        my_sql.myquery("北京市","9102-12-10");//查询事例

    }
}
