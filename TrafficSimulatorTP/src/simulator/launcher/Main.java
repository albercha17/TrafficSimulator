package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private boolean consola= false;
	private static Integer _timeLimit = null; // n�merode pasos
	private static String _inFile = null;
	private static String _outFile = null;
	private static String time = null;
	private static boolean mode=false;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseMode(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);

			parseTime(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc(" Ticks to the simulator�s main loop (default value is 10).").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			System.out.printf("An events file is missing");
		}
	}
	private static void parseMode(CommandLine line) throws ParseException {
		String x = line.getOptionValue("m");
		if (x.equals("gui")) mode=true;
		else if(x=="console") mode =false; 
		//else throw new ParseException("The mode is not valid");
		
		
	}
	private static void parseTime(CommandLine line) throws ParseException {
		time = line.getOptionValue("t");
		if (time == null) {
			_timeLimit=_timeLimitDefaultValue;
		}
		else {
			_timeLimit=Integer.parseInt(time);
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void initFactories() {
		// se creanlas estrategiasde cambiode sem�foro 
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory= new BuilderBasedFactory<>(lsbs);
		
		// se creanlas estrategiasde extracci�nde la cola 
		ArrayList<Builder<DequeuingStrategy>> dqbs = new ArrayList<>(); 
		dqbs.add(new MoveFirstStrategyBuilder()); 
		dqbs.add(new MoveAllStrategyBuilder()); 
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		// se creala listade builders 
		List<Builder<Event>> eventBuilders= new ArrayList<>(); 
		eventBuilders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory)); 
		eventBuilders.add(new NewCityRoadEventBuilder(lssFactory, dqsFactory)); 
		eventBuilders.add(new NewVehicleEventBuilder ()); 
		eventBuilders.add(new NewInterCityRoadEventBuilder(lssFactory, dqsFactory)); 
		eventBuilders.add(new SetContClassEventBuilder()); 
		eventBuilders.add(new SetWeatherEventBuilder()); 
		//...
		 _eventsFactory = new BuilderBasedFactory<>(eventBuilders);
        	}
 
	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out= _outFile == null ? 
		System.out : new FileOutputStream(new File(_outFile)); 
		TrafficSimulator sim = new TrafficSimulator(); 
		Controller ctrl= new Controller(sim, _eventsFactory); 
		
		ctrl.loadEvents(in); 
		ctrl.run(_timeLimit, out); 
		in.close();
		System.out.println("Done!");

	
	}
	private static void startGUIMode () throws IOException {
		InputStream in =null;
		if(_inFile!=null) {
			in = new FileInputStream(new File(_inFile));
			OutputStream out= _outFile == null ? 
			System.out : new FileOutputStream(new File(_outFile));
		} 
		
		TrafficSimulator sim = new TrafficSimulator(); 
		Controller ctrl= new Controller(sim, _eventsFactory); 
		ctrl.loadEvents(in); 
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
		
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(mode) startGUIMode();
		else startBatchMode();
		
	}

	// example command lines:
	//-m gui -i resources/other/json-example-1.json
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/other/json-example-1.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
