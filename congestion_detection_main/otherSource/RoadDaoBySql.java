package trafficinfo.gps.speed.road;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import trafficinfo.gps.speed.commutil.DBUtil;
import trafficinfo.gps.speed.commutil.LogUtil;
import trafficinfo.gps.speed.coord.Wgs84Coord;
import trafficinfo.gps.speed.paramconfig.Parameter;
import trafficinfo.gps.speed.path.RoadEncoder;

/**
 * 透過 RoadDao 將道路資料從資料庫撈出
 * 
 * @author Clay
 * 
 */
public class RoadDaoBySql {

	private static Log log = LogUtil.getLog(RoadDaoBySql.class);
	private static final String InterSectionTable = Parameter.InterSectionTable.toString();
	
	public List<Road> loadRoad() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<Road> roadList = new ArrayList<Road>();
		
		Map<Integer, Byte> stopRoadMap = loadStopRoadMap();
		
		try {

			conn = DBUtil.getConnection("roadmap");

			//String sql = "SELECT A.ID, A.LENGTH, A.S , A.KPH,  B.X1, B.Y1 , B.X2 , B.Y2 FROM ALLCROAD AS A JOIN ALLEDGECOORDS_WGS84 AS B ON (A.ID = B.ID)";
			//String sql = "SELECT A.ID, A.LENGTH, A.S , A.KPH, A.DIRECTION, A.FULLNAME, B.X1, B.Y1 , B.X2 , B.Y2 FROM ALLCROAD AS A JOIN ALLEDGECOORDS_WGS84 AS B ON (A.ID = B.ID) WHERE A.ROUTING = 'Y'";
			//String sql = "SELECT C.ENCODE_ID AS ID, A.LENGTH, A.S , A.KPH, A.DIRECTION, A.FULLNAME, B.X1, B.Y1 , B.X2 , B.Y2 FROM ALLCROAD AS A JOIN ALLEDGECOORDS_WGS84 AS B ON (A.ID = B.ID) JOIN ALLROADID_ENCODE AS C ON (A.ID = C.ID) WHERE A.ROUTING = 'Y' ";
			String sql = "SELECT C.ENCODE_ID AS ID, A.LENGTH, A.S, A.KPH, A.DIRECTION, A.FULLNAME, A.KIND, A.CLASS, B.X1, B.Y1 , B.X2 , B.Y2 FROM ALLCROAD AS A JOIN ALLEDGECOORDS_WGS84 AS B ON (A.ID = B.ID) JOIN ALLROADID_ENCODE AS C ON (A.ID = C.ID) WHERE A.ROUTING = 'Y' ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				Wgs84Coord sCoord = new Wgs84Coord(rs.getDouble("X1"), rs.getDouble("Y1"));
				Wgs84Coord eCoord = new Wgs84Coord(rs.getDouble("X2"), rs.getDouble("Y2"));

				Road road = new Road();
				road.setId(rs.getInt("ID"));
				road.setLen(rs.getFloat("LENGTH"));
				road.setS(rs.getInt("S"));
				road.setSpeedlimit(rs.getInt("KPH"));
				road.setRoadName(rs.getString("FULLNAME"));
				road.setDirection(rs.getString("DIRECTION"));
				road.setScoord(sCoord);
				road.setEcoord(eCoord);
				
				String kind = rs.getString("KIND");
				if(kind.equals("國道")) {
					road.setKind((byte)0);
				} else if (kind.equals("快速道路")) {
					road.setKind((byte)1);
				} else if (kind.equals("交流道")) {
					road.setKind((byte)2);
				} else if (kind.equals("省道")) {
					road.setKind((byte)3);
				} else if (kind.equals("縣道")) {
					road.setKind((byte)4);
				} else if (kind.equals("鄉道")) {
					road.setKind((byte)5);
				} else if (kind.equals("重要道路")) {
					road.setKind((byte)6);
				} else if (kind.equals("一般道路")) {
					road.setKind((byte)7);
				} else if (kind.equals("巷弄")) {
					road.setKind((byte)8);
				} else if (kind.equals("無名道路")) {
					road.setKind((byte)12);
				} else if (kind.equals("計劃道路")) {
					road.setKind((byte)14);
				} else {
					log.info("unknown road kind : " + kind);
				}
				
				road.setRoadClass(rs.getByte("CLASS"));
				
				Byte type = stopRoadMap.get( rs.getInt("ID") );
				if(type != null)
					road.setStopType( type );
				else
					road.setStopType( (byte)0 );
				
				roadList.add(road);

			}

			return roadList;
			
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return null;

	}
	
	public static Map<Integer, Byte> loadStopRoadMap() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		RoadEncoder.init();
		
		Map<Integer, Byte> stopRoadMap = new HashMap<Integer, Byte>();
		Map<Integer, int[]> routeGraph = loadRouteGraph();
		
		try {
			conn = DBUtil.getConnection("roadmap");
			String sql = "SELECT ID_1, ID_2 FROM " + InterSectionTable + " WHERE STATUS = 'Y'" +
					" AND ((KIND1, KIND2) <> ('國道','交流道') AND (KIND1, KIND2) <> ('交流道','國道') AND (KIND1, KIND2) <> ('交流道','快速道路') AND (KIND1, KIND2) <> ('快速道路','交流道'))";
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				/* 加入該grid中包含停等區的路段  */
				Integer id1 = RoadEncoder.idEncode( rs.getString("ID_1") );
				Integer id2 = RoadEncoder.idEncode( rs.getString("ID_2") );
				
				//System.out.println("id1 : " + id1);
				//System.out.println("id2 : " + id2);
				
				int[] fromto1 = routeGraph.get(id1);
				int[] fromto2 = routeGraph.get(id2);
				
//				if(fromto1 == null || fromto2 == null)
//					continue;
				
				//System.out.println(fromto1[0] + "," + fromto1[1]);
				//System.out.println(fromto2[0] + "," + fromto2[1]);
				
				Byte type1 = stopRoadMap.get(id1);
				Byte type2 = stopRoadMap.get(id2);
				
				if(type1 == null)
					type1 = 0;
				if(type2 == null)
					type2 = 0;
				
				if( fromto1[0] == fromto2[0] ) {
					stopRoadMap.put(id1, (byte) (type1 | 1));
					stopRoadMap.put(id2, (byte) (type2 | 1));
				} else if (fromto1[1] == fromto2[0]) {
					stopRoadMap.put(id1, (byte) (type1 | 2));
					stopRoadMap.put(id2, (byte) (type2 | 1));
				} else if (fromto1[0] == fromto2[1]) {
					stopRoadMap.put(id1, (byte) (type1 | 1));
					stopRoadMap.put(id2, (byte) (type2 | 2));
				} else if ( fromto1[1] == fromto2[1] ) {
					stopRoadMap.put(id1, (byte) (type1 | 2));
					stopRoadMap.put(id2, (byte) (type2 | 2));
				} else {
					log.info("roads have no intersection : " + id1 + "," +  id2);
				}
				
				//System.out.println(stopRoadMap.get(id1));
				//System.out.println(stopRoadMap.get(id2));
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return stopRoadMap;
	}
	
	public static Map<Integer, int[]> loadRouteGraph() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Map<Integer, int[]> routeGraph = new HashMap<Integer, int[]>();
		
		try {
			conn = DBUtil.getConnection("roadmap");
			String sql = "SELECT ID, INT_FROM, INT_TO FROM `ALLCROAD` WHERE ROUTING='Y'";
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int roadId = RoadEncoder.idEncode( rs.getString("ID") );
				int intForm = rs.getInt("INT_FROM");
				int intTo = rs.getInt("INT_TO");
				
				routeGraph.put(roadId, new int[]{intForm, intTo} );
			} 
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return routeGraph;
	}
	
	public static void main(String[] argv) {
		loadStopRoadMap();
		
	}
	
}
