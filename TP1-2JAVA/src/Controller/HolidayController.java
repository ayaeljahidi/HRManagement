package Controller;
import View.HolidayView;

import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Main.MainEMployee;
import Model.Employee;
import Model.Holiday;
import Model.HolidayModel;
import Model.HolidayImpoExpo;

public class HolidayController {
	private HolidayView view;
	private HolidayModel model;
	private HolidayImpoExpo impexp = new HolidayImpoExpo();
	public HolidayController(HolidayView view ,HolidayModel model) {
		this.view=view;
		this.model=model;
        initComboBoxData();
        initDateComboBoxData();  // Charger les dates dans les ComboBox de dateDebut et dateFin
        
        afficher();
		this.view.ajouter.addActionListener(e->ajouter());
		this.view.supprimer.addActionListener(e->drop());
		this.view.modifier.addActionListener(e->modifier());
		this.view.Employes.addActionListener(e->back());
		this.view.JT.getSelectionModel().addListSelectionListener(e->fillOutFields());
		this.view.refrecher.addActionListener(e->refrecher());
		this.view.importer.addActionListener(e->handleImport());
		this.view.exporter.addActionListener(e->handleExport());
	}
	

	private void handleImport() {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "csv"));

	    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
	        try {
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	            impexp.importData(filePath);
	            view.afficherMessageSuccess("Importation réussie !");
	        } catch (Exception ex) {
	            view.afficherMessageError("Erreur lors de l'importation : " + ex.getMessage());
	        }
	    }
	}

	private void handleExport() {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "csv"));

	    if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
	        try {
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	            if (!filePath.toLowerCase().endsWith(".csv")) {
	                filePath += ".csv";
	            }
	            List<Holiday> employees = model.afficher();
	            impexp.exportData(filePath, employees);
	            view.afficherMessageSuccess("Exportation réussie !");
	        } catch (Exception ex) {
	            view.afficherMessageError("Erreur lors de l'exportation : " + ex.getMessage());
	        }
	    }
	}
	
	public void refrecher() {
		view.nom.setEnabled(true);
		 view.nom.setSelectedItem(null);
         view.type.setSelectedItem(null);
         view.dateDebut.setSelectedItem(null);
         view.dateFin.setSelectedItem(null);
	}
	public void fillOutFields() {
	    int row = view.JT.getSelectedRow();
	    if (row != -1) {
	        try {
	            String nom = view.tableModel.getValueAt(row, 1).toString();
	            String dateDebut = view.tableModel.getValueAt(row, 2).toString();
	            String dateFin = view.tableModel.getValueAt(row, 3).toString();
	            String type = view.tableModel.getValueAt(row, 4).toString();

	            LocalDate debut = LocalDate.parse(dateDebut);
	            LocalDate fin = LocalDate.parse(dateFin);

	            view.nom.setSelectedItem(nom);
	    		view.nom.setEnabled(false);
	            view.type.setSelectedItem(type);
	            view.dateDebut.setSelectedItem(debut.toString());
	            view.dateFin.setSelectedItem(fin.toString());
	        } catch (Exception e) {
	            view.afficherMessageError("Erreur lors du remplissage des champs : " + e.getMessage());
	        }
	    }
	}

	private void back() {
        view.dispose();
        MainEMployee.start(); 
	}
	

    private void initDateComboBoxData() {
        LocalDate dateDebut = LocalDate.now();
        LocalDate dateFin = LocalDate.of(2025,2,21); 
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        List<String> dates = new ArrayList<>();
        LocalDate currentDate = dateDebut;
        while (!currentDate.isAfter(dateFin)) {
            dates.add(currentDate.format(formatter)); 
            currentDate = currentDate.plusDays(1); 
        }
        
        HolidayView.remplirComboBox(view.DateDebut(), dates);
        HolidayView.remplirComboBox(view.DateFin(), dates);
    }
    
	
	private void initComboBoxData() {
        HolidayView.remplirCombo(view.getNomComboBox(), model.getEmployesName());
    }
	
	public void ajouter() {
		try {
		String nom= view.getNom();
		String dateDebut = view.getDateDebut();
		String dateFin= view.getDateFin();
		String type = view.getSelectedType();
        int solde = view.getSolde();
		if(nom.isEmpty() || dateDebut.isEmpty() || dateFin.isEmpty() || type == null) {
			view.afficherMessageError("Tous les champs doivent être remplis.");
			return;
		}
		
		  if (solde > model.getSolde(nom)) {
              view.afficherMessageError("Solde insuffisant, votre solde est : " + model.getSolde(nom));
              return;
          }
		boolean succes= model.ajouter(dateDebut,dateFin,Holiday.Type.valueOf(type),nom);
		if(succes) {
			view.afficherMessageSuccess("Holiday ajouter");
			view.clearFields();
			 List<Holiday> updatedHolidays = model.afficher();
			 model.updateSolde(nom,model.getSolde(nom)-solde);
	            Object[][] data = new Object[updatedHolidays.size()][5]; // 5 colonnes pour id, date_debut, date_fin, type, nom

	            for (int i = 0; i < updatedHolidays.size(); i++) {
	                Holiday holiday = updatedHolidays.get(i);
	                data[i][0] = holiday.getId();
	                data[i][1] = holiday.getDateDebut();
	                data[i][2] = holiday.getDateFin();
	                data[i][3] = holiday.getType();
	                data[i][4] = holiday.getNom();
	            }
	            view.updateTable(data);

		}else {
			view.afficherMessageError("Erreur lors de l'ajout");
		}
		}catch(Exception ex) {
            view.afficherMessageError("Erreur : " + ex.getMessage());
		}
	}
	
	public void afficher() {
		try {
			view.tableModel.setRowCount(0);
			List<Holiday>holidays= model.afficher();
			for(Holiday e:holidays) {
				Object[]row={
					e.getId(),
					e.getNom(),
					e.getDateDebut(),
					e.getDateFin(),
					e.getType()
					
				};
				view.tableModel.addRow(row);
				view.clearFields();

			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void drop() {
		 view.JT.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) {
	             int row = view.JT.getSelectedRow();
	             if (row != -1) {
	                 int Id = (int) view.tableModel.getValueAt(row, 0); 
	                 String nom = (String) view.tableModel.getValueAt(row, 1);
	                 String dateDebut = (String) view.tableModel.getValueAt(row, 2);
	                 String dateFin = (String) view.tableModel.getValueAt(row, 3);
	                 LocalDate debut = LocalDate.parse(dateDebut);
	                 LocalDate fin = LocalDate.parse(dateFin);
	                 int solde= (int) java.time.temporal.ChronoUnit.DAYS.between(debut, fin);
	                 int reponse = JOptionPane.showConfirmDialog(null, 
	                         "Êtes-vous sûr de vouloir supprimer ce congé ?", 
	                         "Confirmation", 
	                         JOptionPane.YES_NO_OPTION);
	                 if(reponse  == JOptionPane.YES_OPTION) {
	                     boolean success = model.supprimer(Id);
	                     model.updateSolde(nom,model.getSolde(nom)+solde);
	                     if(success) {
	                    	    view.tableModel.removeRow(row);
	                            view.afficherMessageSuccess("congé supprimer avec succès.");
	                     }else {
	                    	 view.afficherMessageError("Erreur lors de la suppression");
	                     }
	                     
	                	 
	                 }}
	 }
	 });
	 }
	 
	
	 
	private void modifier() {

	    // Ajout d'un ActionListener au bouton "Modifier"
	    view.modifier.addActionListener(e -> {
	        try {
	            // Récupérer les informations depuis les champs remplis
	            String nom = view.getNom();
	            String dateDebut = view.getDateDebut();
	            String dateFin = view.getDateFin();
	            String type = view.getSelectedType();

	            // Vérification des champs
	            if (nom == null || nom.isEmpty() || dateDebut == null || dateDebut.isEmpty() 
	                || dateFin == null || dateFin.isEmpty() || type == null) {
	                view.afficherMessageError("Tous les champs doivent être remplis.");
	                return;
	            }
	            
	            // Calcul du solde
	            int solde = view.getSolde();

	            // Vérification de la sélection dans la table
	            int row = view.JT.getSelectedRow();
	            if (row == -1) {
	                view.afficherMessageError("Veuillez sélectionner un congé à modifier.");
	                return;
	            }

	            // Récupération de l'ID du congé à partir de la table
	            int id = (int) view.tableModel.getValueAt(row, 0);
	            String ancienNom = (String) view.tableModel.getValueAt(row, 1);
	            String ancienneDateDebut = (String) view.tableModel.getValueAt(row, 2);
	            String ancienneDateFin = (String) view.tableModel.getValueAt(row, 3);
	            
	            // Calcul de l'ancien solde
	            LocalDate ancienneDebut = LocalDate.parse(ancienneDateDebut);
	            LocalDate ancienneFin = LocalDate.parse(ancienneDateFin);
	            int ancienSolde = (int) java.time.temporal.ChronoUnit.DAYS.between(ancienneDebut, ancienneFin);

	            // Confirmation de modification
	            int reponse = JOptionPane.showConfirmDialog(null, 
	                    "Êtes-vous sûr de vouloir modifier ce congé ?", 
	                    "Confirmation", 
	                    JOptionPane.YES_NO_OPTION);
	            if (reponse == JOptionPane.YES_OPTION) {
	                // Appel au modèle pour modifier les données
	                boolean succes = model.modifier(id, dateDebut, dateFin, Holiday.Type.valueOf(type), ancienNom);

	                // Mise à jour des soldes
	                model.updateSolde(ancienNom, model.getSolde(ancienNom) + ancienSolde);
	                model.updateSolde(ancienNom, model.getSolde(nom) - solde);

	                // Rafraîchissement de la table
	                List<Holiday> updatedHolidays = model.afficher();
	                Object[][] data = new Object[updatedHolidays.size()][5];

	                for (int i = 0; i < updatedHolidays.size(); i++) {
	                    Holiday holiday = updatedHolidays.get(i);
	                    data[i][0] = holiday.getId();
	                    data[i][1] = holiday.getNom();
	                    data[i][2] = holiday.getDateDebut();
	                    data[i][3] = holiday.getDateFin();
	                    data[i][4] = holiday.getType();
	                }
	                view.updateTable(data);

	                if (succes) {
	                    view.afficherMessageSuccess("Congé modifié avec succès.");
	                } else {
	                    view.afficherMessageError("Échec de la modification du congé.");
	                }

	                // Réinitialisation des champs
	                view.clearFields();
	            }
	        } catch (Exception ex) {
	            view.afficherMessageError("Erreur lors de la modification : " + ex.getMessage());
	        }
	    });}


}
