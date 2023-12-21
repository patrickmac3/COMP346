/**
 * Class DiningPhilosophers    
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca     
 */
public class DiningPhilosophers 
{
	/*
	 * ------------
	 * Data members   
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */

			int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			/* 
			 * Task 3
			 */
			// Take and validate command line arguments
			if (argv.length > 0)
			{
				String COMMAND_LINE_ERROR_MESSAGE = "Invalid command line argument: ";
				try 
				{
					// check if string
					if (! argv[0].matches("-?\\d+(\\.\\d+)?"))
					{
						throw new Exception(COMMAND_LINE_ERROR_MESSAGE+ "argument must be numeric");
					}
					// check is decimal number
					else if( argv[0].contains("."))
					{
						throw new Exception(COMMAND_LINE_ERROR_MESSAGE + "argument must be an Integer. Current type: decimal number");
					}
					// check if it is a null or negative value
					else if (Integer.parseInt(argv[0]) == 0 || Integer.parseInt(argv[0]) < 0)
					{
						throw new Exception(COMMAND_LINE_ERROR_MESSAGE + "argument must be positve or non null");
					}
					// assign new valuee to number of philosophers
					else
					{
						iPhilosophers = Integer.parseInt(argv[0]);
					}
				} 
				// handle exceptions 
				catch (Exception e) 
				{
					System.out.println(e);
					System.exit(0);
				}
			}

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
