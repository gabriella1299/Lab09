package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries (Map<Integer,Country> map) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!map.containsKey(rs.getInt("ccode"))) {
					Country c=new Country(rs.getInt("ccode"),rs.getString("StateAbb"),rs.getString("StateNme"));
					map.put(rs.getInt("ccode"), c);
				}
				
			}
			st.close();
			rs.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno) {
		
		String sql="SELECT state1no,state2no,conttype "
				+ "FROM contiguity "
				+ "WHERE year<=? AND state1no>state2no and conttype=1";
		List<Border> result=new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
					Border b=new Border(rs.getInt("state1no"), rs.getInt("state2no"),anno,rs.getInt("conttype"));
					result.add(b);
				
				
			}
			st.close();
			rs.close();
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
}
