/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p3
// FILE:             FilePriority Queue
//
// TEAM:    Team 82 
// Authors: 
// Author1: Matt Marcouiller, mcmarcouille@wisc.edu, 9071489638, 003
// Author2: Zhiheng Wang, zwang759@wisc.edu, 9074796922 ,003
// 
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.Comparator;

/**
 * An implementation of the MinPriorityQueueADT interface. This implementation stores FileLine objects.
 * See MinPriorityQueueADT.java for a description of each method. 
 *
 */
public class FileLinePriorityQueue implements MinPriorityQueueADT<FileLine> {
	private Comparator<FileLine> cmp;
	private int maxSize;
	private FileLine[] queue;

	/**
	 * Constructor for a new FileLinePriorityQueue
	 * 
	 * 
	 */
	public FileLinePriorityQueue(int initialSize, Comparator<FileLine> cmp) {
		this.cmp = cmp;
		maxSize = initialSize;
		queue = new FileLine[initialSize + 1];
	}

	/**
	 * Removes the minimum element from the Priority Queue, and returns it.
	 *
	 * @return the minimum element in the queue, according to the compareTo()
	 * method of FileLine.
	 * @throws PriorityQueueEmptyException if the priority queue has no elements
	 * in it
	 */
	public FileLine removeMin() throws PriorityQueueEmptyException {

		
		if (isEmpty()) {
			throw new PriorityQueueEmptyException();
		}

		int k = 1; 
		FileLine min = queue[k];
		queue[k] = queue[maxSize + 1];
		queue[maxSize + 1] = null;
		maxSize--;

		while (k * 2 <= maxSize) {
			int child = k * 2;
			if ((cmp.compare(queue[child], queue[child + 1]) > 0)) {
				child++;
			}
			if ((cmp.compare(queue[k], queue[child]) <= 0)) {
				break;
			}
			else {
				FileLine temp = queue[k];
				queue[k] = queue[child];
				queue[child] = temp;
			}
			k = child;
		}
		return min;
	}

	/**
	 * Inserts a FileLine into the queue, making sure to keep the shape and
	 * order properties intact.
	 *
	 * @param fl the FileLine to insert
	 * @throws PriorityQueueFullException if the priority queue is full.
	 */
	public void insert(FileLine fl) throws PriorityQueueFullException {
		
		if (maxSize == queue.length - 1) {
			throw new PriorityQueueFullException();
		}

		
		int child = maxSize + 1;

		queue[child] = fl;

		maxSize++;

		while ((child / 2) >= 1) {
			if ((cmp.compare(queue[child], queue[child / 2]) < 0)) {
				FileLine temp = queue[child];
				queue[child] = queue[child / 2];
				queue[child / 2] = temp;
			}
			child = child / 2;
		}
	}

	/**
	 * Checks if the queue is empty.
	 * e.g. 
	 * 
	 * <pre>
	 * {@code
	 * m = new MinPriorityQueue(); 
	 * // m.isEmpty(): true
	 * m.insert(FileLine fl);
	 * // m.isEmpty(): false
	 * m.remove();
	 * // m.isEmpty(): true
	 * }
	 * </pre>
	 *
	 * @return true, if it is empty; false otherwise
	 */
	public boolean isEmpty() {
		return maxSize == 0;
	}
}
