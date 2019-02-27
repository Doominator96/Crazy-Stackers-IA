package game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import logic.Buco;
import logic.Coordinate;
import logic.ShapeIA;
import logic.ShapeIAFinale;
import utils.GameConfig;

public class GameManagerDLV {

	private static String encodingResourceCombinazioni ="../core/assets/combinazioni";
	private static String encodingResourceRisolutore ="../core/assets/risolutore";
	private static String encodingResourceBuchi ="../core/assets/buchi";
	//	private static String encodingResource ="../core/assets/encoding";
	private static Handler handler;
	InputProgram facts;
	InputProgram encoding;
	Output output;
//	Output output1;
	
	
	ArrayList<ArrayList<Coordinate>> coordinateFinali;
	public ArrayList<Integer> soluzioni = new ArrayList<Integer>();

	public GameManagerDLV(ArrayList<ArrayList<Coordinate>> coordinateFinali,boolean scrivi) {
		//IA
		if(scrivi)
			handler = new DesktopHandler(new DLV2DesktopService("../lib/dlv2"));
		else {
			handler = new DesktopHandler(new DLVDesktopService("../lib/dlv"));
			
		}
		facts = new ASPInputProgram();
		encoding = new ASPInputProgram();
		
		this.coordinateFinali = coordinateFinali;
	}



	public void generaCombinazioniASP() throws Exception {
		System.out.println("genera");
		facts.addObjectInput(new ShapeIA(0, 0, controlloFigura(0),1));
		facts.addObjectInput(new ShapeIA(0, 0, controlloFigura(1),2));
		facts.addObjectInput(new ShapeIA(0, 0, controlloFigura(2),3));
		facts.addObjectInput(new ShapeIAFinale());

		handler.addProgram(facts);
		encoding.addFilesPath(encodingResourceCombinazioni);
		handler.addProgram(encoding);
		OptionDescriptor option1 = new OptionDescriptor();
		OptionDescriptor option2 = new OptionDescriptor();
		option1.setOptions("-n=0 ");
		option2.setOptions("--filter fina/4 ");
		handler.addOption(option1);
		handler.addOption(option2);
		output = handler.startSync();


	}


	public void stampaCombinazioniASP() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		AnswerSets answers = (AnswerSets) output;
		int contatore = 0;
		ShapeIAFinale shape1 = new ShapeIAFinale();
		ShapeIAFinale shape2 = new ShapeIAFinale();
		ShapeIAFinale shape3 = new ShapeIAFinale();

		List<AnswerSet> answersets = answers.getAnswersets();
		for (int i = 0; i < answersets.size(); i++) {
			System.out.println(answersets.get(i).toString());
			contatore = 0;
			for(Object obj:answersets.get(i).getAtoms()) {
				if(!(obj instanceof ShapeIAFinale))continue;
				ShapeIAFinale shape = (ShapeIAFinale)obj;
				if( shape.getY()== 55)
					shape1 = shape;
				else if(shape.getY()==95)
					shape2 = shape;
				else if(shape.getY()==135) {
					shape3 =shape;
				}
				if(contatore == 3) {
					aggiungiCombinazione(shape1, shape2, shape3);
				}
				contatore++;

			}
		}
	}

	public void aggiungiCombinazione(ShapeIAFinale s1,ShapeIAFinale s2,ShapeIAFinale s3) {
		Coordinate figura1 = new Coordinate(s1.getX(),s1.getY(),0,0,s1.getType());
		Coordinate figura2 = new Coordinate(s2.getX(),s2.getY(),0,0,s2.getType());
		Coordinate figura3 = new Coordinate(s3.getX(),s3.getY(),0,0,s3.getType());
		ArrayList<Coordinate> coordinate = new ArrayList<Coordinate>();
		coordinate.add(figura1);
		coordinate.add(figura2);
		coordinate.add(figura3);

		coordinateFinali.add(coordinate);
	}


	public void calcolaAnswerSet(Buco[] buchi) throws Exception {
		facts.clearAll();
		encoding.clearAll();
		handler.removeAll();
		System.out.println("rios");
		
		int contatore =0;
		while(contatore < coordinateFinali.size()) {
			ArrayList<Coordinate> remove = coordinateFinali.get(contatore);
			facts.addObjectInput(new ShapeIA(remove.get(0).getxFinale(), remove.get(0).getyFinale(), remove.get(0).getType(),contatore));
			facts.addObjectInput(new ShapeIA(remove.get(1).getxFinale(), remove.get(1).getyFinale(), remove.get(1).getType(),contatore));
			facts.addObjectInput(new ShapeIA(remove.get(2).getxFinale(), remove.get(2).getyFinale(), remove.get(2).getType(),contatore));
			contatore++;
		}
		facts.addObjectInput(buchi[0]);
		facts.addObjectInput(buchi[1]);
		facts.addObjectInput(buchi[2]);
		facts.addObjectInput(new ShapeIAFinale(0,0,0,0));

		handler.addProgram(facts);
		encoding.addFilesPath(encodingResourceRisolutore);
		handler.addProgram(encoding);

		OptionDescriptor option1 = new OptionDescriptor();
		OptionDescriptor option2 = new OptionDescriptor();
		OptionDescriptor option3 = new OptionDescriptor();
		option1.setOptions("-n=1 ");
//		option2.setOptions("--filter=shapeFinale/10 ");
//		option2.setOptions("--filter buco/3 ");
//		handler.addOption(option2);
		handler.addOption(option1);

		output = handler.startSync();

		AnswerSets answers1 = (AnswerSets) output;
		for(AnswerSet a1:answers1.getAnswersets()) {
			System.out.println(a1.toString());
			for (Object obj1:a1.getAtoms()) {
				if(!(obj1 instanceof ShapeIAFinale)) continue;
				ShapeIAFinale shape = (ShapeIAFinale)obj1;
				if(shape.getX() != 0 && !soluzioni.contains(shape.getX())) {
					System.out.println("soluzione: +" + shape.getX());
					soluzioni.add(shape.getX());
				}
				}
		}
	}

	public void stampaAnswerSet() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		AnswerSets answers = (AnswerSets) output;
		for(AnswerSet a:answers.getAnswersets()) {
			for (Object obj:a.getAtoms()) {
				if(!(obj instanceof ShapeIAFinale)) continue;
				ShapeIAFinale shape = (ShapeIAFinale)obj;
				if(shape.getX() != 0)
					System.out.println(shape);
			}
		}
	}

	public int controlloFigura(int identificativo) {
		if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("square"))
			return 0;
		else if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("rectangle"))
			return 1;
		else if(GameConfig.levels[GameConfig.currentLevel][identificativo].equals("circle"))
			return 2;
		return 3;
	}


	public void generaBuchiASP() throws Exception {
		System.out.println("genera buchi");
		int contatore =0;
		while(contatore < coordinateFinali.size()) {
			System.out.println("taac");
			ArrayList<Coordinate> remove = coordinateFinali.get(contatore);
			facts.addObjectInput(new ShapeIA(remove.get(0).getxFinale(), remove.get(0).getyFinale(), remove.get(0).getType(),contatore));
			facts.addObjectInput(new ShapeIA(remove.get(1).getxFinale(), remove.get(1).getyFinale(), remove.get(1).getType(),contatore));
			facts.addObjectInput(new ShapeIA(remove.get(2).getxFinale(), remove.get(2).getyFinale(), remove.get(2).getType(),contatore));
			contatore++;
		}
//		facts.addObjectInput(new Buco(0,0,0,0));

		handler.addProgram(facts);
		encoding.addFilesPath(encodingResourceBuchi);
		handler.addProgram(encoding);

		OptionDescriptor option1 = new OptionDescriptor();
		OptionDescriptor option2 = new OptionDescriptor();
		option1.setOptions("-n=0 ");
		option2.setOptions("--filter posiziona/4 ");
		handler.addOption(option1);
//		handler.addOption(option2);

		output = handler.startSync();

		AnswerSets answers = (AnswerSets) output;
		for(AnswerSet a:answers.getAnswersets()) {
//			System.out.println(a.toString());
			for (Object obj:a.getAtoms()) {
				if(!(obj instanceof Buco)) continue;
				Buco shape = (Buco)obj;
				if(shape.getX() != 0)
					System.out.println(shape.getX());
				}
		}
	}
}
