package com.risk.mapeditor;

import com.risk.common.STATE;
import com.risk.exception.InvalidMapException;
import com.risk.model.Model;
import com.risk.validate.MapValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class is to generate standard map file
 */
public class Writer {

    private ArrayList<Country> countries;
    private HashMap<String,Integer> continents;
    private Path filePath;
    public String invalidReason;

    public Writer(ArrayList<Country> countries, Path filePath) {
        this.countries = countries;
        this.filePath = filePath;
        continents = new HashMap<>();
    }

    /**
     * this method is to write all the information to a specific file
     * @throws IOException IOException
     * @return true if the map to be written is valid; otherwise return false
     */
    public void write() throws IOException, InvalidMapException {

        String headContent = "[Map]\nauthor=SOEN6441Team11\nwarn=yes\nimage=unavailable.bmp\nwrap=no\nscroll=none\n\n";

        String continentsContent = "[Continents]\n";
        String territoriesContent = "[Territories]\n";
        //[map] and author
        File tmp = File.createTempFile("RiskMap",".tmp");
        tmp.deleteOnExit();
        FileWriter fileWriter= new FileWriter(tmp.getName(),true);

        //[Territories]
        for (Country eachCountry: this.countries) {

            String continentName = eachCountry.getContinent();

            //put or update continents HashMap information
            if(!continents.containsKey(continentName)){
                continents.put(continentName,1);
            } else {
                continents.put(continentName,continents.get(continentName) + 1);
            }

            //update territories information
            territoriesContent += eachCountry.getName() + "," + eachCountry.getX() + "," + eachCountry.getY() + "," + eachCountry.getContinent();
            for (Country countryInList: eachCountry.getAdjList()) {
                territoriesContent += "," + countryInList.getName();
            }
            territoriesContent += "\n";
        }

        //update continents information
        for (String key : continents.keySet()) {
            continentsContent += key + "=" + continents.get(key) + "\n";
        }
        continentsContent += "\n";

        fileWriter.write(headContent + continentsContent + territoriesContent);
        fileWriter.close();

        MapValidator mapValidator = new MapValidator();
        Model model = new Model();

        mapValidator.validateMap(model);
        model.editorReadFile(tmp.getCanonicalPath());

        Files.copy(tmp.toPath(),filePath);

    }

//    /**
//     * valid the correctness of the map information for the specific file
//     * @return true if the map is valid; otherwise return false
//     * @throws IOException IOException
//     */
//    private boolean isValidMap() throws IOException, InvalidMapException {
//
//        MapValidator mapValidator = new MapValidator();
//        Model model = new Model();
//
//        try {
//            mapValidator.validateMap(model);
//            model.editorReadFile(filePath);
//        } catch (InvalidMapException ex){
//            invalidReason = ex.getMessage();
//            return false;
//        }
//        return true;
//    }
}
