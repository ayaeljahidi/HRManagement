package Model;
import java.io.File;
import java.io.IOException;
import java.util.List;

import DAO.DataDAOimpl;
public class ImportExportModel {
	private DataDAOimpl dataimp;
	
	
	  // Constructeur pour initialiser dataimp
    public ImportExportModel() {
        this.dataimp = new DataDAOimpl();
    }
	
	private boolean checkFileExists(File file) {
		if(!file.exists()) {
			throw new IllegalArgumentException("Le fichier n'existe pas: "+ file.getPath());
		}
		return true;
	}
	
	private boolean checkIsFile(File file) {
		if(!file.isFile()) {
			throw new IllegalArgumentException("Le chemin spécifié n'est pas un fichier: "+ file.getPath());

		}
		return true;
	}
	
	private boolean checkIsReadable(File file) {
		if(!file.canRead()) {
			throw new IllegalArgumentException("Le fichier n'est pas lisible: "+ file.getPath());

		}
		return true;
	}
	
	public void importData(String filePath) {
	    File file = new File(filePath);
	    if (checkFileExists(file) && checkIsFile(file) && checkIsReadable(file)) {
	        try {
	            dataimp.importData(filePath);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            throw new RuntimeException("Erreur lors de l'importation des données : " + ex.getMessage());
	        }
	    }
	}

	public void exportData(String fileName, List<Employee> data) {
	    if (data == null || data.isEmpty()) {
	        throw new IllegalArgumentException("La liste des employés est vide.");
	    }
	    try {
	        dataimp.exportData(fileName, data);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        throw new RuntimeException("Erreur lors de l'exportation des données : " + ex.getMessage());
	    }
	}

}
