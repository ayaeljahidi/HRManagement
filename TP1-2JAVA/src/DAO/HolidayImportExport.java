package DAO;

import java.sql.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Model.Holiday;
import java.sql.Date;
import java.time.LocalDate;
public class HolidayImportExport implements DataImportExport<Holiday>{
	
	private Connection conn;
	public HolidayImportExport() {
	    conn = DBConnection.getConnection();
	}
	
	@Override
	public void importData(String filePath) throws IOException {
	    String query = "INSERT INTO Holiday(date_debut, date_fin, type, nom) values(?, ?, ?, ?)";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        String line = reader.readLine(); 
	        while ((line = reader.readLine()) != null) {
	            String[] data = line.split(",");
	            if (data.length == 4) { 
	            	   // Cast date strings to java.sql.Date
	                Date dateDebut = Date.valueOf(LocalDate.parse(data[0].trim()));
	                Date dateFin = Date.valueOf(LocalDate.parse(data[1].trim()));

	                pstmt.setDate(1, dateDebut);
	                pstmt.setDate(2, dateFin);
	                pstmt.setString(3, data[2].trim());
	                pstmt.setString(4, data[3].trim());
	                pstmt.addBatch();
	            }
	        }
	        pstmt.executeBatch();
	        System.out.println("Holiday imported successfully!");
	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	        throw new IOException("Erreur lors de l'importation des données : " + e.getMessage(), e);
	    }
	}

	@Override
	public void exportData(String fileName, List<Holiday> data) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
			writer.write("nom,type,dateDebut,dateFin");
			writer.newLine();
			for (Holiday holi : data) {
				String line = String.format("%s,%s,%s,%s",
					holi.getNom(),
					holi.getType(),
					holi.getDateDebut(),
					holi.getDateFin()
						);
				writer.write(line);
				writer.newLine();
			}
		}
		
	}

}
