/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapbenchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class RandomObjects {
    static int random = 0;
    public static String randomString(Random randomizer, int length) {
        int[] rs = new int[length];
        for (int i=0; i<length; ++i) {
            int randomNumber = randomizer.nextInt(26);
            randomNumber += 65;
            rs[i] = randomNumber;
        }
        return new String (rs, 0, length);
    }
    public static int randomNumber(Random randomizer, int maxNumber) {
        return randomizer.nextInt(maxNumber);
    }
};

class Adresse {
    public Adresse (String vorname, String name, String strasse, int hausnummer,
                    int plz, String ort) {
        this._vorname = vorname;
        this._name = name;
        this._strasse = strasse;
        this._hausnummer = hausnummer;
        this._plz = plz;
        this._ort = ort;
    }
    public String getVorname() {
        return _vorname;
    }
    public String getName() {
        return _name;
    }
    public String getStrasse() {
        return _strasse;
    }
    public int getHausnummer() {
        return _hausnummer;
    }
    public int getPlz() {
        return _plz;
    }
    public String getOrt()  {
        return _ort;
    }
    @Override
    public String toString () {
        return "Name=" + _name + ", Vorname=" + _vorname
               + ", Strasse=" + _strasse + ' ' + _hausnummer + ", Ort="
               + _plz + ' ' + _ort;
    }
    public static Adresse randomAdresse(Random randomizer) {
        return new Adresse (RandomObjects.randomString (randomizer, 10),
                RandomObjects.randomString (randomizer, 15),
                RandomObjects.randomString (randomizer, 12),
                RandomObjects.randomNumber(randomizer, 100),
                RandomObjects.randomNumber(randomizer, 100000),
                RandomObjects.randomString (randomizer, 17));
    }
    private final String _vorname;
    private final String _name;
    private final String _strasse;
    private final int _hausnummer;
    private final int _plz;
    private final String _ort;
};

class VornameNameExact implements Comparable<VornameNameExact>{
    public VornameNameExact (String vorname, String name) {
        this._vorname = vorname;
        this._name = name;
    }
    @Override
    public int compareTo(VornameNameExact other) {
        _numberOfComparisons++;
        if (!_name.equals(other._name)) {
            return _name.compareTo(other._name);
        } else {
            return _vorname.compareTo(other._vorname);
        }
    }
    public String getVorname() {
        return _vorname;
    }
    public String getName() {
        return _name;
    }
    static VornameNameExact randomVornameNameExact(Random randomizer) {
        return new VornameNameExact (
                RandomObjects.randomString (randomizer, 10),
                RandomObjects.randomString (randomizer, 15));
    }
    static void resetNumberOfComparisons() {
        _numberOfComparisons = 0;
    }
    static int getNumberOfComparisons() {
        return _numberOfComparisons;
    }
    private final String _vorname;
    private final String _name;
    static private int _numberOfComparisons;
};

class HausnummerPlzExact implements Comparable<HausnummerPlzExact> {
    public HausnummerPlzExact (int hausnummer, int plz) {
        this._hausnummer = hausnummer;
        this._plz = plz;
    }
    @Override
    public int compareTo(HausnummerPlzExact other) {
        _numberOfComparisons++;
        if (_plz != other._plz) {
            return _plz - other._plz;
        } else {
            return _hausnummer - other._hausnummer;
        }
    }
    int getHausnummer() {
        return _hausnummer;
    }
    int getPlz() {
        return _plz;
    }
    static HausnummerPlzExact randomHausnummerPlzExact(Random randomizer) {
        return new HausnummerPlzExact (RandomObjects.randomNumber (randomizer, 100),
                                       RandomObjects.randomNumber (randomizer, 100000));
    }
    static void resetNumberOfComparisons() {
        _numberOfComparisons = 0;
    }
    static int getNumberOfComparisons() {
        return _numberOfComparisons;
    }
    private final int _hausnummer;
    private final int _plz;
    static private int _numberOfComparisons;
};

/**
 *
 * @author markus
 */
public class MapBenchmark {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        PrintStream out = System.out;
        long start = System.currentTimeMillis();
        List<Adresse> addresses = new ArrayList<>();
        Random randomizer = new Random();
        File file1 = new File("addresses.txt");
        if (file1.exists()) {
            BufferedReader infile1 = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file1)));
            out.println("Reading addresses.txt file...");
            String line;
            int i = 0;
            String vorname = "";
            String name = "";
            String strasse = "";
            int hausnummer = 0;
            int plz = 0;
            String ort = "";
            while ((line = infile1.readLine()) != null) {
                switch (i) {
                    case 0:
                        vorname = line;
                        break;
                    case 1:
                        name = line;
                        break;
                    case 2:
                        strasse = line;
                        break;
                    case 3:
                        hausnummer = Integer.parseInt(line);
                        break;
                    case 4:
                        plz = Integer.parseInt(line);
                        break;
                    case 5:
                        ort = line;
                        break;
                }
                i++;
                if (i>5) {
                    addresses.add(new Adresse(vorname, name, strasse,
                            hausnummer, plz, ort));
                    i=0;
                }
            }
            infile1.close();
        } else {
            out.println("Putting adresses to vector and file...");
            PrintWriter outfile = new PrintWriter(
                    new FileWriter("addresses.txt"));
            for (int i=0; i<3000000; i++) {
                addresses.add(Adresse.randomAdresse(randomizer));
                Adresse adresse = addresses.get(i); 
                outfile.println (adresse.getVorname());
                outfile.println (adresse.getName());
                outfile.println (adresse.getStrasse());
                outfile.println (adresse.getHausnummer());
                outfile.println (adresse.getPlz());
                outfile.println (adresse.getOrt());
            }
            outfile.close();
        }
        out.println("Adresses in vector:" + addresses.size());
        long startBuildingIndex1 = System.currentTimeMillis();
        long diff1 = startBuildingIndex1 - start;
        out.println("Time to put adresses to vector: " +
                diff1 + "ms.");
        out.println("Building index for Vorname Name...");
        Map<VornameNameExact, Adresse> addressMap1 = new TreeMap<>();
        for (Adresse adresse :  addresses) {
            addressMap1.put (new VornameNameExact(adresse.getVorname(),
                    adresse.getName()), adresse);
        }
        long startBuildingIndex2 = System.currentTimeMillis();
        long diff1_1 = startBuildingIndex2 - startBuildingIndex1;
        out.println("Time for indexing for Vorname Name: " +
                diff1_1 + "ms.");
        out.println("Building index for Hausnummer and PLZ ...");
        Map<HausnummerPlzExact, Adresse> addressMap2 = new TreeMap<>();
        for (Adresse adresse :  addresses) {
            addressMap2.put (new HausnummerPlzExact(adresse.getHausnummer(),
                    adresse.getPlz()), adresse);
        }
        long startCreatingSearchRequests1 = System.currentTimeMillis();
        long diff2_1 = startCreatingSearchRequests1 - startBuildingIndex2;
        out.println("Time for indexing for Hausnummer and PLZ: " +
                diff2_1 + "ms.");
        List<VornameNameExact> searchRequests1 = new ArrayList<>();
        File file2 = new File("searchRequests1.txt");
        if (file2.exists()) {
            BufferedReader infile1 = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file2)));
            out.println("Reading searchRequests1.txt file...");
            String line;
            int i = 0;
            String vorname = "";
            String name = "";
            while ((line = infile1.readLine()) != null) {
                switch (i) {
                    case 0:
                        vorname = line;
                        break;
                    case 1:
                        name = line;
                        break;
                }
                i++;
                if (i>1) {
                    searchRequests1.add(new VornameNameExact(vorname, name));
                    i=0;
                }
            }
            infile1.close();
        } else {
            out.println("Putting search requests 1 to vector and file ...");
            PrintWriter outfile = new PrintWriter(
                    new FileWriter("searchRequests1.txt"));
            for (int i=0; i<1000000; i++) {
                searchRequests1.add(VornameNameExact.
                        randomVornameNameExact(randomizer));
                VornameNameExact vne = searchRequests1.get(i);
                outfile.println (vne.getVorname());
                outfile.println (vne.getName());
            }
            outfile.close();
        }
        long startCreatingSearchRequests2 = System.currentTimeMillis();
        long diff2_2 = startCreatingSearchRequests2 - startCreatingSearchRequests1;
        out.println("Time for building search requests 1: " +
                diff2_2 + "ms.");
        List<HausnummerPlzExact> searchRequests2 = new ArrayList<>();
        File file3 = new File("searchRequests2.txt");
        if (file3.exists()) {
            BufferedReader infile1 = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file3)));
            out.println("Reading searchRequests2.txt file...");
            String line;
            int i = 0;
            int hausnummer = 0;
            int plz = 0;
            while ((line = infile1.readLine()) != null) {
                switch (i) {
                    case 0:
                        hausnummer = Integer.parseInt(line);
                        break;
                    case 1:
                        plz = Integer.parseInt(line);
                        break;
                }
                i++;
                if (i>1) {
                    searchRequests2.add(new HausnummerPlzExact(hausnummer, plz));
                    i=0;
                }
            }
            infile1.close();
        } else {
            out.println("Putting search requests 2 to vector and file ...");
            PrintWriter outfile = new PrintWriter(
                    new FileWriter("searchRequests2.txt"));
            for (int i=0; i<1000000; i++) {
                searchRequests2.add(HausnummerPlzExact.
                        randomHausnummerPlzExact(randomizer));
                HausnummerPlzExact hpe = searchRequests2.get(i);
                outfile.println (hpe.getHausnummer());
                outfile.println (hpe.getPlz());
            }
            outfile.close();
        }
        VornameNameExact.resetNumberOfComparisons();
        HausnummerPlzExact.resetNumberOfComparisons();
        long startSearch1 = System.currentTimeMillis();
        long diff3_1 = startSearch1 - startCreatingSearchRequests2;
        out.println("Time for building search requests 2: " +
                diff3_1 + "ms.");
        out.println("Performing 1x" + searchRequests1.size() + " search requests 1 ...");
        for (int i=0; i<1; i++) {
            for (VornameNameExact searchRequest : searchRequests1) {
                final Adresse foundAdresse = addressMap1.get(searchRequest);
                if (foundAdresse != null) {
                    out.println("Found adresse: " + foundAdresse);
                }
            }
        }
        long startSearch2 = System.currentTimeMillis();
        long diff3_2 = startSearch2 - startSearch1;
        out.println("Time for searching 1: " +
                diff3_2 + "ms.");
        out.println("Performing 1x" + searchRequests2.size() + " search requests 2 ...");
        Adresse lastFoundAdresse = null;
        for (int i=0; i<1; i++) {
            for (HausnummerPlzExact searchRequest : searchRequests2) {
                Adresse adresse = addressMap2.get(searchRequest);
                if (adresse != null) {
                    lastFoundAdresse = adresse;
                }
            }
        }
        if (lastFoundAdresse != null) {
            out.println("Last found adresse: " + lastFoundAdresse);
        }
        long end = System.currentTimeMillis();
        long diff4 = end - startSearch2;
        out.println("Time for searching 2: " +
                diff4 + "ms.");
        long diff5 = end - start;
        out.println("Overall time: " + diff5
             + "ms.");
        out.println("Number of Vorname/Name comparisons=" +
                VornameNameExact.getNumberOfComparisons());
        out.println("Number of hausnummer/PLZ comparisons=" +
                HausnummerPlzExact.getNumberOfComparisons());
    }
}
