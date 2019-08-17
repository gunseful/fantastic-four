import app.entities.products.Order;
import app.entities.user.User;
import app.model.controller.Repository;

public class Main {
    public static void main(String[] args) {
        Repository ur = new Repository();
        User user = new User();
        user.setId(2);
//        ur.getOrdersNEW(user);

        for(Order order :         ur.getOrders(user)){
            System.out.println(order);
        }
//        ResultSet rsObj = null;
//        Connection connObj = null;
//        Connection connObj2 = null;
//
//        PreparedStatement pstmtObj = null;
//        ConnectionPool jdbcObj = new ConnectionPool();
//
//
//        try {
//            DataSource data = ConnectionPool.getDataSource();
//
//            // Performing Database Operation!
//            System.out.println("\n=====Making A New Connection Object For Db Transaction=====\n");
//            connObj = data.getConnection();
//            connObj2 = data.getConnection();
//            jdbcObj.printDbStatus();
//
//            pstmtObj = connObj.prepareStatement("SELECT * FROM USERS");
//            rsObj = pstmtObj.executeQuery();
//            while (rsObj.next()) {
//                System.out.println("Username: " + rsObj.getString("NICKNAME"));
//            }
//            System.out.println("\n=====Releasing Connection Object To Pool=====\n");
//        } catch(Exception sqlException) {
//            sqlException.printStackTrace();
//        } finally {
//            try {
//                // Closing ResultSet Object
//                if(rsObj != null) {
//                    rsObj.close();
//                }
//                // Closing PreparedStatement Object
//                if(pstmtObj != null) {
//                    pstmtObj.close();
//                }
//                // Closing Connection Object
//                if(connObj != null) {
//                    connObj.close();
//                }
//            } catch(Exception sqlException) {
//                sqlException.printStackTrace();
//            }
//        }
//        jdbcObj.printDbStatus();
//        try {
//            connObj2.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        jdbcObj.printDbStatus();
//
    }
}
