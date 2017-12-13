/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.huykn.decode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.huykn.decode.model.RuleConfig;
import vn.huykn.decode.utils.DoSomething;

/**
 *
 * @author huykn
 */
public class TestTools {

    private static final String PATH_APPEND = "huykn";
    private static final String PATH_CONFIG = "rule";
    private static final String PATH_RULE_CONFIG = "rule_attrs";
    private static final String PATH_RULE_FILE_CONFIG = "rule_files";

    private static List<String> listJava;
    private static List<String> listXml;
    private static List<String> listAttrs;
    private static List<String> listRuleFiles;
    private static RuleConfig[] listRuleConfig;

    private static boolean isDebug = false;

    public static void main(String[] args) throws IOException {
        listJava = new ArrayList<>();
        listXml = new ArrayList<>();
        listAttrs = new ArrayList<>();
        listRuleConfig = readRuleConfig();
        listAttrs = readAttrsConfig();
        readRuleFileConfig();
        printTree(0, new File("/Users/huykn/Documents/Gits/Firewall/app"));
        DoSomething.init();

        processingJavaFile();

    }

    static void printTree(int depth, File file) throws IOException {
        StringBuilder indent = new StringBuilder();
        String name = file.getAbsolutePath();

        for (int i = 0; i < depth; i++) {
            indent.append(".");
        }
        //Pretty print for directories
        int f = -1;
        if (file.isDirectory()) {
            System.out.println(indent.toString() + "|");
            if (isPrintName(name) > 0) {
                System.out.println(indent.toString() + "*" + file.getAbsolutePath());
            }
        } //Print file name
        else if ((f = isPrintName(name)) >= 0) {
            String path = file.getAbsolutePath();
            System.out.println(indent.toString() + path);
            if (f == 0) {
                listXml.add(path);
            } else if (f > 0) {
                System.out.println(indent.toString() + path);
                listJava.add(path);
            }
        }
        //Recurse children
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                printTree(depth + 4, files[i]);
            }
        }
    }

//Exclude some file names
    static int isPrintName(String name) {
//        if (name.charAt(0) == '.') {
//            return false;
//        }

        for (String f : listRuleFiles) {
            if (name.contains(f)) {
                File file = new File(name);
                file.setWritable(true);
                file.delete();
//                System.out.println(name);
                return -1;
            }
        }
        if (name.contains(".xml")) {
            if (name.contains("attrs.xml")
                    || name.contains("dimens.xml")
                    || name.contains("colors.xml")
                    || name.contains("bools.xml")
                    || name.contains("styles.xml")
                    || name.contains("integers.xml")
                    || name.contains("strings.xml")) {
                try {
                    processingFileAttrs(name);
                } catch (IOException ex) {
                    Logger.getLogger(TestTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return 0;
        }
        if (name.contains(".java")) {
            return 1;
        }
        //.
        //. Some more exclusions
        //.
        return -1;
    }

    private static void processingJavaFile() {
        for (String list : listJava) {
//            System.out.println(list);
            try {
                processingJavaFile(list);
            } catch (IOException ex) {
                Logger.getLogger(TestTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void processingJavaFile(String list) throws IOException {
        File f = new File(list);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        List<String> lines = Files.readAllLines(Paths.get(list), Charset.forName("UTF-8"));
        // 
        List<String> linesWrite = new ArrayList<>();
        for (String line : lines) {
            linesWrite.add(processingLine(line));
        }
        writeToFileNIOWay2(f, linesWrite);
    }

    private static String processingLine(String line) {
        for (RuleConfig cf : listRuleConfig) {
            //System.out.println(cf.getKey() + " -> " + line);
            if (line.trim().length() > 0 && line.contains(cf.getKey())) {
                String tmp = null;
                switch (cf.getType()) {
                    case All:
//                        System.out.println("->afall: " + line);
                        tmp = processingCaseAll(line, cf);
//                        System.out.println("->bf: " + tmp);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(TestTools.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        return tmp;
                    case Content:
//                        System.out.println("->afcon: " + line);
                        tmp = processingCaseContent(line, cf);
//                        System.out.println("->bf: " + tmp);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(TestTools.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        return tmp;
                    case Abs:
//                        System.out.println("->afabs: " + line);
                        tmp = processingCaseAbs(line, cf);
//                        System.out.println("->bf: " + tmp);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(TestTools.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        return tmp;
                    default:
                        throw new AssertionError(cf.getType().name());
                }
            }
        }
        return line;
    }

    private static RuleConfig[] readRuleConfig() throws IOException {
        List<RuleConfig> configs = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(PATH_CONFIG), Charset.forName("UTF-8"));
        for (String line : lines) {
            //System.out.println(line);
            String[] split = line.split("->");
            if (split.length > 0) {
                RuleConfig config = null;
                switch (split[0]) {
                    case "all":
                        config = new RuleConfig(RuleConfig.Type.All, split[1], split[2]);
                        break;
                    case "content":
                        config = new RuleConfig(RuleConfig.Type.Content, split[1], null);
                        break;
                    case "abs":
                        config = new RuleConfig(RuleConfig.Type.Abs, split[1], null);
                        break;
                    default:
                        continue;
                }
                configs.add(config);
            }
        }
        //System.out.println(configs.size());
        return configs.toArray(new RuleConfig[configs.size()]);
    }

    private static String processingCaseAll(String line, RuleConfig cf) {
        System.out.println(line);
        return new String(line.replace(cf.getKey(), cf.getValue()) + (isDebug ? "//" + cf.getKey() : ""));
    }

    private static String processingCaseContent(String line, RuleConfig cf) {
        try {
            String[] split = line.split(cf.getKey());
            String tmp = split[1];

            if (tmp.contains(")")) {
                split = tmp.split(")");
                String result = DoSomething.findString(split[0].trim());
                if (result == null) {
                    return line;
                }
                return new String(line.replace(split[0], result) + "//" + line);
            } else if (tmp.contains(",")) {
                split = tmp.split(",");
                String result = DoSomething.findString(split[0].trim());
                if (result == null) {
                    return line;
                }
                return new String(line.replace(split[0], result) + "//" + line);
            } else {
                String result = DoSomething.findString(tmp);
                if (result == null) {
                    return line;
                }
                return new String(line.replace(split[0], result) + "//" + line);
            }
        } catch (Exception ex) {
            System.out.println(cf.getKey() + " " + line);
            ex.printStackTrace();
        }
        return line;
    }

    private static String processingCaseAbs(String line, RuleConfig cf) {
//        String[] split = line.split(" ");
//        // case 12321 /*8*/
//        if (split.length > 0 && line.contains("/*")) {
//            return line.replace(split[1], split[2].replace("", "/*").replace("", "*/")) + "//" + line;
//        }
        return line;
    }

    private static List<String> readAttrsConfig() throws IOException {
        return Files.readAllLines(Paths.get(PATH_RULE_CONFIG), Charset.forName("UTF-8"));
    }

    private static void processingFileAttrs(String name) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(name), Charset.forName("UTF-8"));
        List<String> linesWrite = new ArrayList<>();
        String lineRemove = null;
        for (String line : lines) {
            if (lineRemove != null) {
                if (line.contains("<flag")
                        || line.contains("<enum")
                        || line.contains("<item name=")
                        || line.contains("</style>")
                        || line.contains("</attr>")) {
                    continue;
                } else {
                    lineRemove = null;
                }
            }
            boolean fl = false;
            for (String str : listAttrs) {
                if (line.contains(str)) {
                    if(line.contains("YouTube")){
                    System.out.println(str + " " + line);                    
                    }
//                    System.out.println(str + " " + line);
                    fl = true;
                    lineRemove = line;
                    break;
                }
            }
            if (!fl) {
                linesWrite.add(line);
            }
        }
        writeToFileNIOWay2(new File(name), linesWrite);
    }

    private static void writeToFileNIOWay2(File file, List<String> linesWrite) throws IOException {
        final int numberOfIterations = linesWrite.size();
        if (file.exists()) {
            file.delete();
        }
        for (int i = 0; i < numberOfIterations; i++) {
            Files.write(Paths.get(file.toURI()), (linesWrite.get(i) + "\n").getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private static void readRuleFileConfig() throws IOException {
        listRuleFiles = Files.readAllLines(Paths.get(PATH_RULE_FILE_CONFIG), Charset.forName("UTF-8"));
    }
}
