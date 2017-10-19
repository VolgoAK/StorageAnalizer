package xyz.volgoak.storageanalizer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Volgoak on 19.10.2017.
 */

public class FileList extends ArrayList<File> implements Serializable{

    /** Суммарный размер категории файлов. */
    private long mFilesSize;

    /** Массив расширений которые хранит данный лист */
    private String[] mExtensions;

    private String mCategoryName;

    public FileList(String categoryName, String[] extensions){
        mCategoryName = categoryName;
        mExtensions = extensions;
    }

    @Override
    public boolean add(File f) {
        mFilesSize += f.length();
        return super.add(f);
    }

    /**
     * Проверяет соответствует ли файл данной категории.
     * Если да, то дабавляет его в лист. Если массив с расширениями не задан,
     * то добавляет в лист любой файл.
     * @param f файли для проверки и добавления
     * @return соответствует ли файл данной категории
     */
    public boolean offerFile(File f){
        if(mExtensions != null){
            //check files for type
            for(String s : mExtensions){
                if(f.getName().endsWith(s)){
                    return add(f);
                }
            }
            return false;
        }else{
            //there is no type for check, then just add file to list
            return add(f);
        }
    }

    public long getFilesSize() {
        return mFilesSize;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setExtensions(String[] mExtensions) {
        this.mExtensions = mExtensions;
    }
}
