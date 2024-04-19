package DAOs;

import Interfaces.DAOs.IStockDAO;
import Models.Stock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockDAO implements IStockDAO {

    private static Connection conn = null;

    public StockDAO() {
        conn = DB.DBContext.getConnection();
    }

    public enum Table {
        Product_ID,
        Price,
        Quantity
    }

    @Override
    public int addStock(Stock stock) {
        int result = 0;
        try {
            String sql = "INSERT INTO Stock VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, stock.getProductID());
            ps.setInt(2, stock.getPrice());
            ps.setInt(3, stock.getQuantity());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Stock getStock(int productID) {
        ResultSet rs;
        try {
            String sql = "SELECT * FROM Stock WHERE [Product_ID] = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, productID);
            rs = ps.executeQuery();
            Stock stock = new Stock();
            if (rs.next()) {
                stock.setProductID(rs.getInt(Table.Product_ID.toString()));
                stock.setPrice(rs.getInt(Table.Price.toString()));
                stock.setQuantity(rs.getInt(Table.Quantity.toString()));
            }
            return stock;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateStock(Stock stock) {
        int result = 0;
        try {
            String sql = "UPDATE Stock SET "
                    + "Price=?, Quantity=? "
                    + "WHERE Product_ID=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, stock.getPrice());
            ps.setInt(2, stock.getQuantity());
            ps.setInt(3, stock.getProductID());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}