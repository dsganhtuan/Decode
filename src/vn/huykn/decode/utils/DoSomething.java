package vn.huykn.decode.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import vn.huykn.decode.model.DataSource;
import vn.huykn.decode.model.Public;

public class DoSomething {

    private static Map<String, Public> map;

    static {
        map = new HashMap<String, Public>();
    }

    public static void init() throws FileNotFoundException {
        DataSource readData = readData("resource.json");
        processingData(readData);
    }

    private static void processingData(DataSource readData) {
        List<Public> public1 = readData.getResources().getPublic();
        for (Public pl : public1) {
            map.put(pl.getId(), pl);
        }
    }

    public static DataSource readData(String path) throws FileNotFoundException {
//		FileInputStream inputStream = new FileInputStream(path);
        Gson gson = new Gson();
//		String json = gson.toJson(inputStream, Object.class);
//		gson.fromJson(json, Object.class);
        FileReader reader = new FileReader(path);
        return gson.fromJson(reader, DataSource.class);
    }

    public static void copy2Clipboard(Public copy) {
//		String myString = "This text will be copied into clipboard when running this code!";
        StringSelection stringSelection = new StringSelection("R." + copy.getType() + "." + copy.getName());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);

    }

    public static String formatResponse(Public copy) {
        if (copy == null) {
            return null;
        }
//		String myString = "This text will be copied into clipboard when running this code!";
        return "R." + copy.getType() + "." + copy.getName();

    }

    public static Public find(String key) {
        return map.get(key);
    }

    public static String findString(String key) {
        return formatResponse(find(key));
    }

}
