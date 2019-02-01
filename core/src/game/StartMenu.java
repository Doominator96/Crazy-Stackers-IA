package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import graphic.LoadTexture;

public class StartMenu {

	public static Button newGameButton;
	public static Button exitButton;
	public static Button startMenu;
	public static Button resume;
	public static Button restart;
	
	static long firstClick = 0;
	public static boolean menu;
	public static boolean pausedMenu;



	
	public StartMenu() {
		newGameButton = new Button("newgame", LoadTexture.newGame, 75, 100, 20, 16);
		exitButton = new Button("exit", LoadTexture.exit, 105, 100, 20, 16);
		resume = new Button("resume", LoadTexture.resumeT, 50, 90, 20, 16);
		restart = new Button("restart", LoadTexture.restartT, 90, 90, 20, 16);
		startMenu = new Button("startMenu", LoadTexture.startMenuT, 130, 90, 20, 16);
		
		menu=true;
	}
	public static void mouseClickMenu(float mouseX, float mouseY) {
		//System.out.println(mouseX + "   " + mouseY);
		long secondClick = 0;
		if(menu) {
			if((mouseX >= newGameButton.x && mouseX <= (newGameButton.width + newGameButton.x)) && (mouseY >= newGameButton.y && mouseY <= (newGameButton.height +newGameButton.y))) {
				newGameButton.setX(72);
				newGameButton.setY(98);
				newGameButton.setWidth(26);
				newGameButton.setHeight(20);
				newGameButton.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						menu = false;
				}
			}
			else {
				newGameButton.setX(75);
				newGameButton.setY(100);
				newGameButton.setWidth(20);
				newGameButton.setHeight(16);
				newGameButton.setSelected(false);
			}

			if((mouseX >= exitButton.x && mouseX <= (exitButton.width + exitButton.x)) && (mouseY >= exitButton.y && mouseY <= (exitButton.height +exitButton.y))) {
				exitButton.setX(102);
				exitButton.setY(98);
				exitButton.setWidth(26);
				exitButton.setHeight(20);
				exitButton.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						Gdx.app.exit();
				}
			}
			else {
				exitButton.setX(105);
				exitButton.setY(100);
				exitButton.setWidth(20);
				exitButton.setHeight(16);
				exitButton.setSelected(false);
			}
		}

		if(pausedMenu) {
			if((mouseX >= resume.x && mouseX <= (resume.width + resume.x)) && (mouseY >= resume.y && mouseY <= (resume.height +resume.y))) {
				resume.setX(48);
				resume.setY(88);
				resume.setWidth(26);
				resume.setHeight(20);
				resume.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) 
						pausedMenu = false;

				}
			}
			else {
				resume.setX(50);
				resume.setY(90);
				resume.setWidth(20);
				resume.setHeight(16);
				resume.setSelected(false);
			}

			if((mouseX >= restart.x && mouseX <= (restart.width + restart.x)) && (mouseY >= restart.y && mouseY <= (restart.height +restart.y))) {
				restart.setX(88);
				restart.setY(88);
				restart.setWidth(26);
				restart.setHeight(20);
				restart.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					//implementare restart livello
					if(secondClick - firstClick > 60) { 
						GameManager.restartLevel();
						pausedMenu=false;
					}
				}
			}
			else {
				restart.setX(90);
				restart.setY(90);
				restart.setWidth(20);
				restart.setHeight(16);
				restart.setSelected(false);
			}

			if((mouseX >= startMenu.x && mouseX <= (startMenu.width + startMenu.x)) && (mouseY >= startMenu.y && mouseY <= (startMenu.height +startMenu.y))) {
				startMenu.setX(128);
				startMenu.setY(88);
				startMenu.setWidth(26);
				startMenu.setHeight(20);
				startMenu.setSelected(true);
				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					secondClick = System.currentTimeMillis();
					if(secondClick - firstClick > 60) {
						pausedMenu = false;
						menu = true;
					}
				}

			}
			else {
				startMenu.setX(130);
				startMenu.setY(90);
				startMenu.setWidth(20);
				startMenu.setHeight(16);
				startMenu.setSelected(false);
			}
		}
		firstClick = secondClick;
		//		System.out.println(firstClick +"    " + secondClick);
	}
}
