package con.example.myband.classes;

import java.util.Date;

import android.graphics.Bitmap;

public class Live {
	
	String live_name;
	String band_name;
	String place;
	String date; //questo include anche l'ora
	String notes;
	Bitmap img;
	
	public Live(){}
	
	public Live(String live_name, String band_name, String place, String date, String notes, Bitmap bitmap){
		this.live_name = live_name;
		this.band_name = band_name;
		this.place = place;
		this.date= date;
		this.notes = notes;
		this.img = bitmap;
	}
	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	//shift+alt+s
	public String getLive_name() {
		return live_name;
	}

	public void setLive_name(String live_name) {
		this.live_name = live_name;
	}

	public String getBand_name() {
		return band_name;
	}

	public void setBand_name(String band_name) {
		this.band_name = band_name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	
	

}
