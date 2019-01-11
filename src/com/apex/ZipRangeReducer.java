package com.apex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * 
 * @author jortiz
 *
 */
public class ZipRangeReducer {
	
	private static final String DEFAULT_INPUT_FILE_NAME = "src/inputRanges";

	
	/**
	 * MAIN APP RUNNER
	 * User can give the path to the file or use the default one the src folder
	 * @param args Path to File that have the ranges 
	 */
	public static void main(String args []) {
		
		String fileName = (args != null && args.length > 0 && args[0] != null) ? args[0] : DEFAULT_INPUT_FILE_NAME;
		
		//(1) Read ranges from file  O(n)
		List<ZipRange> ranges = readFileRanges(fileName);
		
		//(2) Sort ranges asc order to help find the overlapping in the next elements, O(log(n))
		sortZipRanges(ranges);
		
		//(3) Find the overlapping and merge the ranges, return the new ranges O(n-1)
		ranges = reduceZipRanges(ranges);

		//(4) Print result O(n)
		for(ZipRange zipRange : ranges) {
			System.out.println(zipRange);
		}
	}
	
	/**
	 * Read the file and return the array of ranges
	 * @param fileName
	 * @return
	 */
	public static List<ZipRange> readFileRanges(String fileName){
		List<ZipRange> ranges = new ArrayList<>();
		
		//(1) read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(range -> {
				try {
					ranges.add(new ZipRange(range));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Invalid Range :: " + e.getMessage());
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ranges;
	}
	
	/**
	 * Sort a given list of Zip Ranges ASC mode
	 * @param listZipRangeToSort
	 */
    public static void sortZipRanges(List<ZipRange> listZipRangeToSort){
        Collections.sort(listZipRangeToSort, new Comparator<ZipRange>() {
            @Override
            public int compare(ZipRange range1, ZipRange range2) {

                if (range1.getLower() > range2.getLower())
                    return 1;

                if (range1.getLower() < range2.getLower())
                    return -1;

                return 0;
            }
        });
    }
    
    /**
     * Find overlapping Zip Ranges and merge them to produce new one
     * @param listZipRangeToReduce
     */
    public static List<ZipRange> reduceZipRanges(List<ZipRange> listZipRangeToReduce){
    	List<ZipRange> listNewZipRange = new ArrayList<>();
        int i = 0;
        
        while(i < listZipRangeToReduce.size()) {
        	ZipRange zipRange = listZipRangeToReduce.get(i);

        	//INCLUDE LAST RANGE
		    if (i == (listZipRangeToReduce.size() - 1) ){
		    	listNewZipRange.add(zipRange);
		         break;
		    }
		    
		    //FIND OVERLAPPINGS
		    ZipRange nextZipRange = listZipRangeToReduce.get(i+1);
		    if(zipRange.getUpper() >= nextZipRange.getLower()) {
		    	 //UPDATE NEXT Zip with the new overlapping range and keep comparing
		         if (zipRange.getUpper() >= nextZipRange.getUpper()) {
		         	// i contains i+1
		         	listZipRangeToReduce.set(i+1, new ZipRange(zipRange.getLower(),zipRange.getUpper()));
		         }else {
		         	// i+1 has the upper value
		         	listZipRangeToReduce.set(i+1, new ZipRange(zipRange.getLower(),nextZipRange.getUpper()));
		         }
		    }else {
		    	//if there is no overlapping
		    	listNewZipRange.add(zipRange);
		    }
        	i++;
        }
        return listNewZipRange;
    }
}
