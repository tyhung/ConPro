package trafficinfo.gps.speed.commutil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {
	private static Log log = LogUtil.getLog( DBUtil.class );
	
	public static ComboPooledDataSource cpds = new ComboPooledDataSource("gfvd");
	
	public static Connection getConnection() throws SQLException {		
		return cpds.getConnection();
	}
	//// add for gpsgetter//////////////////////////////
	private static Map<String,ComboPooledDataSource> map;
	
	static {				
		map = new HashMap<String,ComboPooledDataSource>();
	}
	
	public static Connection getConnection(String source) throws SQLException{
	
		if(map.containsKey(source)) {
			return map.get(source).getConnection();
		} else {
			map.put(source, new ComboPooledDataSource(source));
			return map.get(source).getConnection();
			
		}		
	}
	//////////////////////////////////////////////////////
	public static void init( ) {
		log.debug( "c3p0-url:"+cpds.getJdbcUrl() );
		//cpds.setJdbcUrl( connectionUrl );
		Connection conn = null;
		try {
			long t = System.currentTimeMillis();
			long mem = Runtime.getRuntime().freeMemory();

			conn = DBUtil.getConnection("gfvd");
			
			log.info( "c3p0，花費: " + (System.currentTimeMillis() - t) + " ms" );
			log.info( "c3p0，花費: " + (mem - Runtime.getRuntime().freeMemory() ) / 1024 / 1024 + "M 記憶體" );
			log.info( "c3p0，" + mem + " " + Runtime.getRuntime().freeMemory() );
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				conn.close();
			}catch(SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void getData() {
		String sql = "select * from D_FLOATING_CAR_DATA limit 100";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
			Thread.sleep(100000);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getSqlString(String path, String fname) {
		FileReader fr;
		String sql = "";
		try {
			// fr = new
			// FileReader(".//Config//generate_rawgps_table.sqlstring");
			fr = new FileReader(".//" + path + "//" + fname);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				sql += s;
			}
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public static void main(String argv[]) {
		DBUtil db = new DBUtil();
		db.getData();
	}
}
