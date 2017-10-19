package xyz.volgoak.storageanalizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Volgoak on 19.10.2017.
 */

/**
 * Распределяет файлы по категориям и хранит их в FileLists
 */
public class FilesSorter {

    private ArrayList<FileList> fileLists;

    //Возможные расширения файлов для категорий.
    static String[] imageExtensions = new String[]{".jpg", ".png", ".gif", ".jpeg"};
    static String[] soundExtensions = new String[]{".mp3", ".wav", ".flac", ".mid"};
    static String[] videoExtensions = new String[]{".mp4", ".3gp", ".mkv"};
    static String[] archiveExtensions = new String[]{".zip", ".rar"};
    static String[] documentsExtension = new String[]{".txt", ".doc", ".pdf", ".fb2", ".csv", ".xml"};
    static String[] apkExtensions = new String[]{".apk"};

//    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public FilesSorter(){
        fileLists = new ArrayList<>();

        fileLists.add(new FileList("Images", imageExtensions));
        fileLists.add(new FileList("Sounds", soundExtensions));
        fileLists.add(new FileList("Videos", videoExtensions));
        fileLists.add(new FileList("Archives", archiveExtensions));
        fileLists.add(new FileList("Documents", documentsExtension));
        fileLists.add(new FileList("Apks", apkExtensions));
        fileLists.add(new FileList("Others", null));
    }

    public void addFile(File file){
        //add with extensions
        for(FileList files : fileLists){
            if(files.offerFile(file)) break;
        }
    }

    public ArrayList<FileList> getCategoriesAsList(){
        return fileLists;
    }
}
