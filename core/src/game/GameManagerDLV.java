package game;

import java.util.ArrayList;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import logic.ShapeIA;
import utils.GameConfig;

/*
 * cerchio = 0
 * quadrato = 1
 * rettangolo = 2
 * triangolo =3
 */
public class GameManagerDLV {

	private Handler handler;
	private InputProgram facts;
	private InputProgram encoding;
	private String encodingFile;

	public GameManagerDLV() {
		encodingFile = "";
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		facts= new ASPInputProgram();
		encoding= new ASPInputProgram();
	}

	public void posiziona() throws Exception {
		int level = GameConfig.currentLevel;
		ArrayList<String>  figure = new ArrayList<String>();
		
		for (int i = 0; i < GameConfig.COUNT; i++) {
			figure.add(GameConfig.levels[level][i]);
		}	
		System.out.println(figure);
		
		for (int i = 0; i < GameConfig.COUNT; i++) {
			if(GameConfig.levels[level][i] == "circle")
				facts.addObjectInput(new ShapeIA(0, 0, 0, i));
			else if(GameConfig.levels[level][i] == "square")
					facts.addObjectInput(new ShapeIA(0, 0, 1, i));
			else if(GameConfig.levels[level][i] == "rectangle")
				facts.addObjectInput(new ShapeIA(0, 0, 2, i));
			else if(GameConfig.levels[level][i] == "triangle")
				facts.addObjectInput(new ShapeIA(0, 0, 3, i));
		}
	}
}
