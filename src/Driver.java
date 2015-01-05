//Tyler Brown
//12/26/14
//CureTheVirusDriver
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Driver {

	public static ArrayList<String> citizen = new ArrayList<String>();
	public static int numCitizens;
	public static int lenCitizenDNA;
	public static int lenVirusDNA;
	public static String virusDNA;
	
	public static void main(String[] args) 
	{
		System.out.println("Please enter the path to the input file: ");
		Scanner userInput = new Scanner(System.in);
		String filePath = userInput.next();
		userInput.close();
		
		int fileSuccess = fileReader(filePath);
		
		if(fileSuccess == 1)
		{
			Set<String> virusDNASet = new LinkedHashSet<String>();
			virusDNAMaker(virusDNA, lenVirusDNA, virusDNASet);
			
			List<String> sortedDNAList = new ArrayList<String>(virusDNASet);
			sortedDNAList.remove("");
			Collections.sort(sortedDNAList, new SortingComparator());
			
			float probability[] = new float[numCitizens];
			float batch[] = new float[lenVirusDNA + 1];
			probabilityCalculator(probability, sortedDNAList);
			printResults(batch, probability);
		}
	}
	
	
	public static int fileReader(String filePath)
	{
		File inputFile = new File(filePath);
		try
		{
			Scanner reader = new Scanner(inputFile);
			
			String s = reader.nextLine();
			String data[] = s.split(" ");
			numCitizens = Integer.parseInt(data[0]);
			lenCitizenDNA = Integer.parseInt(data[1]);
			lenVirusDNA = Integer.parseInt(data[2]);
			
			for(int i = 0; i < numCitizens; ++i)
			{
				String DNA = reader.nextLine();
				citizen.add(DNA);
			}
			
			virusDNA = reader.nextLine();		
			reader.close();
			return 1;
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("File could not be opened.");
			return 0;
		}	
	}
	
	
	public static void virusDNAMaker(String virusDNA, int lenVirusDNA, Set<String> virusDNASet)
	{
		if (!virusDNASet.contains(virusDNA))
		{
			virusDNASet.add(virusDNA);
			
			for(int i = 1; i <= lenVirusDNA; ++i)
			{
				String newString1 = virusDNA.substring(0, lenVirusDNA-i) + virusDNA.substring(lenVirusDNA-i+1);
				virusDNAMaker(newString1, newString1.length(), virusDNASet);
			}
		}
	}
	
	public static void probabilityCalculator(float probability[], List<String> sortedDNAList)
	{
		for(int i=0; i < numCitizens; ++i)
		{
			for(int j=0; j < sortedDNAList.size(); ++j)
			{
				if(stringChecker(sortedDNAList.get(j), citizen.get(i)))
				{
					float ratio = ((float)sortedDNAList.get(j).length()) / virusDNA.length();
					probability[i] = ratio;
					break;
				}
			}
		}
	}
	
	
	public static boolean stringChecker(String virusDNA, String citizenDNA)
	{
		int lenVirusDNA = virusDNA.length();
		int lenCitizenDNA = citizenDNA.length();
		
		for(int i = 0; i < lenCitizenDNA; ++i)
		{
			if(virusDNA.charAt(0) == citizenDNA.charAt(i))
			{
				int j = 1;
				if(lenVirusDNA == 1)
					return true;
					while((i+j) < (lenCitizenDNA-1) && virusDNA.charAt(j) == citizenDNA.charAt(i+j))
					{
						j++;
						if(j == lenVirusDNA)
							return true;
					}
			}
		}
		
		return false;
	}
	
	
	public static void printResults(float batch[], float probability[])
	{
		batch[0] = 2;
		for(int k = 1; k <= lenVirusDNA; k++)
		{
			float max = 0;
			for(int l = 0; l < numCitizens; l++)
			{
				if(probability[l] > max && probability[l] < batch[k-1])
					max = probability[l];
			}
			
			if (max == 0)
				break;
			batch[k] = max;
		}
		
		System.out.println("Cure the Virus");
		System.out.println("Each citizen is printed out with their batch number.");
		System.out.println("Citizens with batch 1 get vaccinated first and so forth.\n");
		for(int m = 0; m < numCitizens; m++)
		{
			for(int n = 1; n < batch.length; n++)
			{
				if(probability[m] == batch[n])
				{
					System.out.println("Citizen " + (m+1) + ": " + n);
				}
			}
		}
	}
}
