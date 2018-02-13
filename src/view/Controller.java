package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import java.io.*;
import java.util.ArrayList;

public class Controller {

	// Buttons to Edit, Add and Delete Songs
	@FXML
	Button mEdit, mAdd, mDelete, mSave, mUndo;

	// Text that are supposed to display Song properties
	@FXML
	Text tName, tArtist, tAlbum, tYear;

	// TextFields that are supposed to edit Song properties
	@FXML
	TextField tfName, tfArtist, tfAlbum, tfYear;


	@FXML
	Label lName, lArtist, lAlbum, lYear;


	// List of All Songs
	@FXML ListView<Song> listview;
	public ObservableList<Song> songList;
	public ArrayList<Song> alSongList;


	public void start() throws IOException {

		mSave.setVisible(false);
		mSave.setDisable(true);
		mUndo.setVisible(false);
		mUndo.setDisable(true);
		loadSongs();

	}

	public ArrayList<Song> loadFiles(String file) throws IOException {
		File songfile = new File(file);
		alSongList = new ArrayList<Song>(1);
		if(!songfile.exists()) {
			songfile.createNewFile();
		}

		FileReader fr = new FileReader(songfile);
		BufferedReader br = new BufferedReader(fr);

		String line = "";
		while(line != null) {
			Song temp = new Song();
			line = br.readLine();
			if(line == null) {
				break;
			}
			temp.name = line;
			line = br.readLine();
			temp.artist = line;
			line = br.readLine();
			temp.album = line;
			line = br.readLine();
			temp.year = line;
			alSongList.add(temp);
			alSongList.sort(null);
		}

		br.close();

		return alSongList;

	}

	public void loadSongs() throws IOException {

		loadFiles("songs.txt");

		songList = FXCollections.observableArrayList(alSongList);
		if(songList.size() == 0) {
			tName.setText("");
			tArtist.setText("");
			tAlbum.setText("");
			tYear.setText("");
		}

        listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {

			@Override
			public void changed(ObservableValue<? extends Song> arg0, Song arg1, Song arg2) {
				try {
					tName.setText(arg2.getName());
					tArtist.setText(arg2.getArtist());
					tAlbum.setText(arg2.getAlbum());
					tYear.setText(arg2.getYear());
				}
				catch(NullPointerException e) {

				}

			}
            });

		listview.setItems(songList);
		listview.getSelectionModel().select(0);


	}
	public void addToFile(String filename) throws IOException
	{
		//		System.out.println("I got successfully called!");
		File songs = new File(filename);

		if (alSongList.size() == 0)
		{	// There were no items in the arraylist, nothing to save
			songs.delete();
			return;
		}
		if (!songs.exists())
		{
			songs.createNewFile();
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(filename));

		for(int i = 0; i < alSongList.size(); i++) {
			Song songToBeWritten = alSongList.get(i);
			out.write(songToBeWritten.getName());
			out.newLine();
			out.write(songToBeWritten.getArtist());	
			out.newLine();
			out.write(songToBeWritten.getAlbum());
			out.newLine();
			out.write(songToBeWritten.getYear());
			out.newLine();

		}

		// Reset number of songs to original value after, because this function call
		// results in either application close or just application continue after a song is added
		out.close();
		return;
	}
	
	public void update() {
		songList = FXCollections.observableArrayList(alSongList);
		listview.setItems(songList);
		listview.refresh();
		try {
			addToFile("songs.txt");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

//	public boolean isDuplicate(Song song) {
//		if(alSongList.contains(song)) {
//			return true;
//		}
//		return false;
//	}



	public void btnPress(ActionEvent e) {
		Button b = (Button) e.getSource();
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		Alert errorAlert = new Alert(AlertType.ERROR);
		if(b == mAdd) {
			if(tfName.getText().equalsIgnoreCase("") || tfArtist.getText().equalsIgnoreCase("") || tfName.getText().isEmpty() || tfArtist.getText().isEmpty()) {
				infoAlert.setHeaderText("Missing Fields");
				infoAlert.setContentText("Missing mandatory fields: Song Name and/or Song Artist");
				infoAlert.showAndWait();
				return;
			}
			Song temp = new Song(tfName.getText(), tfArtist.getText());
			
			if(tfAlbum.getText() != null || !tfAlbum.getText().isEmpty()) {
				temp.setAlbum(tfAlbum.getText());
			}
			
			boolean isNumber = true;
			if(tfYear.getText() != null || !tfYear.getText().isEmpty()) {
				try {
					int year = Integer.parseInt(tfYear.getText());
				}
				catch(NumberFormatException g) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Year");
					alert.setContentText("Please Enter in a valid Year");
					alert.showAndWait();
					isNumber = false;
					return;
				}
				temp.setYear(tfYear.getText());
			}
			
			for(int i = 0; i < alSongList.size(); i++) {
				if(alSongList.get(i).getName().equalsIgnoreCase(temp.getName()) && alSongList.get(i).getArtist().equalsIgnoreCase(temp.getArtist()) ) {
					errorAlert.setHeaderText("Duplicate Entries");
					errorAlert.setContentText("Duplicate Entries");
					errorAlert.showAndWait();
					return;
				}
			}
			if(isNumber) {
				alSongList.add(temp);
				alSongList.sort(null);
				tfName.clear();
				tfArtist.clear();
				tfAlbum.clear();
				tfYear.clear();
				mSave.setVisible(false);
				mSave.setDisable(true);
				mUndo.setVisible(false);
				mUndo.setDisable(true);
				update();
			}
		
			System.out.println("Clicked" + b.toString());
		}
		else if(b == mEdit) {
			mSave.setVisible(true);
			mSave.setDisable(false);
			mUndo.setVisible(true);
			mUndo.setDisable(false);
			
			if(alSongList.isEmpty()) {
				errorAlert.setTitle("Nothing to edit");
				errorAlert.setContentText("Nothing left to edit");
				errorAlert.showAndWait();
				return;
			}
			
			int index = listview.getSelectionModel().getSelectedIndex();
			tfName.setText(songList.get(index).getName());
			tfArtist.setText(songList.get(index).getArtist());
			tfAlbum.setText(songList.get(index).getAlbum());
			tfYear.setText(songList.get(index).getYear());
			
			//Remove song from array list and add it back with save
			alSongList.remove(index);
			
			System.out.println("Clicked" + b.toString());
		}
		else if(b == mSave) {
			
			int index = listview.getSelectionModel().getSelectedIndex();
			
			if(tfName.getText().equalsIgnoreCase("") || tfArtist.getText().equalsIgnoreCase("") || tfName.getText().isEmpty() || tfArtist.getText().isEmpty()) {
				infoAlert.setHeaderText("Missing Fields");
				infoAlert.setContentText("Missing mandatory fields: Song Name and/or Song Artist");
				infoAlert.showAndWait();
				return;
			}
			Song temp = new Song(tfName.getText(), tfArtist.getText());
			
			if(tfAlbum.getText() != null || !tfAlbum.getText().isEmpty()) {
				temp.setAlbum(tfAlbum.getText());
			}
			boolean isNumber = true;
			if(tfYear.getText() != null || !tfYear.getText().isEmpty()) {
				try {
					int year = Integer.parseInt(tfYear.getText());
				}
				catch(NumberFormatException g) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Year");
					alert.setContentText("Please Enter in a valid Year");
					alert.showAndWait();
					isNumber = false;
					return;
				}
				temp.setYear(tfYear.getText());
			}
			
			for(int i = 0; i < alSongList.size(); i++) {
				if(alSongList.get(i).getName().equalsIgnoreCase(temp.getName()) && alSongList.get(i).getArtist().equalsIgnoreCase(temp.getArtist()) ) {
					errorAlert.setHeaderText("Duplicate Entries");
					errorAlert.setContentText("Duplicate Entries");
					errorAlert.showAndWait();
					return;
				}
			}
			if(isNumber) {
				alSongList.add(temp);
				alSongList.sort(null);
				tfName.clear();
				tfArtist.clear();
				tfAlbum.clear();
				tfYear.clear();
				update();
			}

			
			System.out.println("Clicked" + b.toString());
		}
		else if(b == mDelete) {
			if(songList.size() == 1)listview.getSelectionModel().select(0);
			if(songList.isEmpty()) {
				tName.setText("");
				tArtist.setText("");
				tAlbum.setText("");
				tYear.setText("");

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nothing to delete");
				alert.setContentText("Nothing left to delete");
				alert.showAndWait();
				return;
			}
			int index = listview.getSelectionModel().getSelectedIndex();
			alSongList.remove(index);
			update();
			listview.refresh();
			if(!songList.isEmpty()) {
				if(index == 0) {
					tName.setText(songList.get(index).name);
					tArtist.setText(songList.get(index).artist);
					tAlbum.setText(songList.get(index).album);
					tYear.setText(songList.get(index).year);
				}else {
					listview.getSelectionModel().select(index-1);
					tName.setText(songList.get(index-1).name);
					tArtist.setText(songList.get(index-1).artist);
					tAlbum.setText(songList.get(index-1).album);
					tYear.setText(songList.get(index-1).year);
				}

			}
			else {
				tName.setText("");
				tArtist.setText("");
				tAlbum.setText("");
				tYear.setText("");
			}
		}
		else if(b == mUndo) {
			System.out.println("Clicked" + b.toString());
		}
	}





}