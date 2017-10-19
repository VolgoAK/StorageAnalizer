package xyz.volgoak.storageanalizer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements OverviewFragment.OverviewFragListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private FilesSorter mFilesSorter;
    private ProgressBar mProgressBar;
    private FilesAnalyzeTask mFilesTask;
    // TODO: 19.10.2017 save files in on destroy method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*File[] fs = getExternalFilesDirs(null);
        for(File f : fs){
            Log.d(TAG, "onCreate: " + f.getAbsolutePath());
        }
        Log.d(TAG, "onCreate: " + Environment.getExternalStorageDirectory().getPath());
        Log.d(TAG, "onCreate: " + Environment.isExternalStorageRemovable());*/

//        Log.d(TAG, " sd path " + getSdCardPath().getAbsolutePath());

        if (savedInstanceState == null) {
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
            mProgressBar.setVisibility(View.VISIBLE);

            mFilesSorter = new FilesSorter();

            mFilesTask = new FilesAnalyzeTask();
            mFilesTask.execute();
        }
    }

    /**
     * Анализирует директорию, передает файлы в FilesSorter
     * @param file папка для анализа.
     */
    private void analyzeFiles(File file) {
        File[] files = file.listFiles();
        Queue<File> fileQueue = new LinkedList<>(Arrays.asList(files));

        while (!fileQueue.isEmpty()) {
            File f = fileQueue.poll();
            if (f.isDirectory()) {
                Collections.addAll(fileQueue, f.listFiles());
            } else {
                mFilesSorter.addFile(f);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mFilesTask != null){
            mFilesTask.cancel(true);
        }
    }

    /**
     * Callback для OverviewFragment, запускает фрагмент, отображающий
     * конкретную категорию.
     * @param category
     */
    @Override
    public void onCategorySelected(FileList category) {
        CategoryFragment fragment = CategoryFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Возвращает расположение карты памяти.
     * Ужасный хардкод, но к сожалению, я не смог найти лучшего решения.
     * @return
     */
    private File getSdCardPath(){
        String[] possiblePath = new String[]{"/sdcard1", "/extSdCard", "extsdcard",
            "sdcard1"};
        String root = "/storage";
        for(String var : possiblePath){
            File file = new File(root, var);
            if(file.exists()) return file;
        }
        return null;
    }

    /**
     * Запускает анализ файловой системы. По завершении запускает фрагмент,
     * который отображает файлы по категориям.
     */
    private class FilesAnalyzeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            analyzeFiles(Environment.getExternalStorageDirectory());
            File sdCardFile = getSdCardPath();
            if(sdCardFile != null){
                analyzeFiles(sdCardFile);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Hide ProgressBar
            mProgressBar.setVisibility(View.GONE);

            ArrayList<FileList> fileLists = mFilesSorter.getCategoriesAsList();

            OverviewFragment fragment = OverviewFragment.newInstance(fileLists);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }
}
