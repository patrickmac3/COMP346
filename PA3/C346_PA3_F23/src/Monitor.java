
import java.util.PriorityQueue;

/** 
 * Class Monitor 
 * To synchronize dining philosophers. 
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */

/* 
 * TASK 2
 */
public class Monitor   
{
	/*
	 * ------------    
	 * Data members 
	 * ------------
	 */

	// possible states of a philosopher
	private enum State {THINKING, HUNGRY, EATING}

	// array with the state of the philosophers
	private State[] states;

	// number of  philosophers
	private int numberOfPhilosophers;

	// boolean if any philosopher is talking
	private boolean isTalking;
	
	// queue containing the hungry philosophers
	private PriorityQueue<Integer> hungryPhilosophers;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{

		numberOfPhilosophers = piNumberOfPhilosophers;

		
		hungryPhilosophers = new PriorityQueue<>();
		isTalking = false;

		states = new State[numberOfPhilosophers];
		for (int i = 0; i < numberOfPhilosophers; i++) {
			states[i] = State.THINKING;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	 /* 
	  * check if philosophers to left and right are eating 
	  * - if neither are, then the current philosopher can start eating
	  * - if at least one is eating, the philosopher will wait until ressources are available
	  */
	public synchronized void test(int index)
	{
		try 
		{
			while (true) {

				if (
					states[ ( (index+1) % numberOfPhilosophers) ] != State.EATING &&
					states[ (index + numberOfPhilosophers - 1) % numberOfPhilosophers ] != State.EATING &&
					states[ index] == State.HUNGRY 
				)
				{
					states[index] = State.EATING;
					break;
				}
				else
				{
					wait();
				}
			}	
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		/* 
		 * index of philosopher in states array
		 * - array index start at 0
		 * - piTID starts at 1
		 */
		int philopherIndex = piTID - 1;

		// change state to hungry and add to queue of hungry philosophers
		states[philopherIndex] = State.HUNGRY;
		hungryPhilosophers.add(piTID);
		// test if philospher can eat, until then he will wait
		test(philopherIndex);
		// no longer hungry
		hungryPhilosophers.remove();

	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int philopherIndex = piTID - 1;
		states[philopherIndex] = State.THINKING;
		// let others know the ressources are available
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// if someone is talking request is you must wait
		if(isTalking)
		{
			try 
			{
				wait(); 
				requestTalk();
			} 
			catch (InterruptedException e) 
			{
			e.printStackTrace();
			}

			isTalking = true;
		}

	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// no one is talking anymore
		isTalking = false;
		// notify others that the someone can talk 
		notifyAll();
	}
}

// EOF
