package Model;
import java.io.File;
import java.io.IOException;
import java.util.List;

import DAO.HolidayImportExport;
public class HolidayImpoExpo {
	private HolidayImportExport holi;
	
	
	  // Constructeur pour initialiser dataimp
    public HolidayImpoExpo() {
        this.holi = new HolidayImportExport();
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
	            holi.importData(filePath);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            throw new RuntimeException("Erreur lors de l'importation des données : " + ex.getMessage());
	        }
	    }
	}

	public void exportData(String fileName, List<Holiday> data) {
	    if (data == null || data.isEmpty()) {
	        throw new IllegalArgumentException("La liste des Congés est vide.");
	    }
	    try {
	        holi.exportData(fileName, data);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        throw new RuntimeException("Erreur lors de l'exportation des données : " + ex.getMessage());
	    }
	}

}
