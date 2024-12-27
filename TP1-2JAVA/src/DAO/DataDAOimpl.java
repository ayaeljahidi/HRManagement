package DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import Model.Employee;

public class DataDAOimpl implements DataImportExport<Employee> {
	private Connection conn;
	public DataDAOimpl() {
	    conn = DBConnection.getConnection();
	}
	
	@Override
	public void importData(String filePath) throws IOException {
	    String query = "INSERT INTO employe(nom, prenom, email, telephone, salaire, poste, role) values(?, ?, ?, ?, ?, ?, ?)";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        String line = reader.readLine(); // Skip header
	        while ((line = reader.readLine()) != null) {
	            String[] data = line.split(",");
	            if (data.length == 7) { // Fix: 7 columns expected
	                pstmt.setString(1, data[0].trim());
	                pstmt.setString(2, data[1].trim());
	                pstmt.setString(3, data[2].trim());
	                pstmt.setString(4, data[3].trim());
	                pstmt.setDouble(5, Double.parseDouble(data[4].trim()));
	                pstmt.setString(6, data[5].trim());
	                pstmt.setString(7, data[6].trim());
	                pstmt.addBatch();
	            }
	        }
	        pstmt.executeBatch();
	        System.out.println("Employees imported successfully!");
	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	        throw new IOException("Erreur lors de l'importation des données : " + e.getMessage(), e);
	    }
	}

   @Override
	public void exportData(String fileName, List<Employee> data) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
			writer.write("nom,prenom,email,telephone,salaire,poste,role");
			writer.newLine();
			for (Employee employee : data) {
				String line = String.format("%s,%s,%s,%s,%.2f,%s,%s",
						employee.getNom(),
				        employee.getPrenom(),
				        employee.getEmail(),
				        employee.getTelephone(),
				        employee.getSalaire(),
						employee.getPoste(),
						employee.getRole()
						);
				writer.write(line);
				writer.newLine();
			}
		}
		
	}

}
