import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Reducer solves the following problem: given a set of sorted input files (each
 * containing the same type of data), merge them into one sorted file. 
 *
 */
public class Reducer {
    // list of files for stocking the PQ
    private List<FileIterator> fileList;
    private String type,dirName,outFile;

    public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java Reducer <weather|thesaurus> <dir_name> <output_file>");
			System.exit(1);
		}

		String type = args[0];
		String dirName = args[1];
		String outFile = args[2];

		Reducer r = new Reducer(type, dirName, outFile);
		r.run();
	
    }

	/**
	 * Constructs a new instance of Reducer with the given type (a string indicating which type of data is being merged),
	 * the directory which contains the files to be merged, and the name of the output file.
	 */
    public Reducer(String type, String dirName, String outFile) {
		this.type = type;
		this.dirName = dirName;
		this.outFile = outFile;
    }

	/**
	 * Carries out the file merging algorithm described in the assignment description. 
	 */
    public void run() {
		File dir = new File(dirName);
		File[] files = dir.listFiles();
		Arrays.sort(files);

		Record r = null;

		// list of files for stocking the PQ
		fileList = new ArrayList<FileIterator>();

		for(int i = 0; i < files.length; i++) {
			File f = files[i];
			if(f.isFile() && f.getName().endsWith(".txt")) {
				//fileList.add(fif.makeFileIterator(f.getAbsolutePath()));
				fileList.add(new FileIterator(f.getAbsolutePath(), i));
			}
		}

		switch (type) {
		case "weather":
			r = new WeatherRecord(fileList.size());
			break;
		case "thesaurus":
			r = new ThesaurusRecord(fileList.size());
			break;
		default:
			System.out.println("Invalid type of data! " + type);
			System.exit(1);
		}

		// TODO
		FileLinePriorityQueue queue= new FileLinePriorityQueue(4, r.getComparator());
		Comparator<FileLine> cmp = r.getComparator();
		try{
		    for(int i =0; i<fileList.size();i++){
		     	if(fileList.get(i).hasNext()){
				queue.insert(fileList.get(i).next());
			    }
		    }
		    while(!queue.isEmpty()){
			    FileLine fileLine = queue.removeMin();
		    if(cmp.compare(new FileLine(r.toString(),null), fileLine) == 0){
		    	r.join(fileLine);
		    }else{
		    	System.out.println(r.toString());
		    	r.clear();
     	    	r.join(fileLine);
		    }
		    
		    if (fileList.get(fileLine.getFileIterator().getIndex()).hasNext()){
		    	queue.insert(fileList.get(fileLine.getFileIterator().getIndex()).next());	
		    }else {
		    	//if this file has been exhausted, do nothing
		    }
		    }
		    System.out.println(r.toString());
		}
		catch(PriorityQueueFullException ex){
			System.out.println(ex);
		}
		catch(PriorityQueueEmptyException e){
			System.out.println(e);
		}
    }
}
