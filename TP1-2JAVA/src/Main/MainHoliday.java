package Main;
import View.HolidayView;
import DAO.HolidayDAOimpl;
import Model.HolidayModel;
import Controller.HolidayController;

public class MainHoliday {
	
	 public static void start() {
			HolidayView view = new HolidayView();
			HolidayDAOimpl dao = new HolidayDAOimpl();
			HolidayModel model= new HolidayModel(dao);
			new HolidayController(view,model);
	 }
	public static void main(String[] args) {

}

}