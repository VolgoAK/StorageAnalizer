package xyz.volgoak.storageanalizer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements OverviewFragment.OverviewFragListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 5674;

    //Сортирует файлы и хранит списки
    private FilesSorter mFilesSorter;

    private ProgressBar mProgressBar;
    private FilesAnalyzeTask mFilesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
            mProgressBar.setVisibility(View.VISIBLE);
            mFilesSorter = new FilesSorter();
            mFilesTask = new FilesAnalyzeTask();


            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_REQUEST);
            } else mFilesTask.execute();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFilesTask.execute();
            } else {
                Toast.makeText(this, "Chicken! Just delete this app!", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Анализирует директорию, передает файлы в FilesSorter
     *
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
        if (mFilesTask != null) {
            mFilesTask.cancel(true);
        }
    }

    /**
     * Callback для OverviewFragment, запускает фрагмент, отображающий
     * конкретную категорию.
     *
     * @param category категория файлов для отображения.
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
     *
     * @return File корневая папка Sd карты или null если карта не найдена.
     */
    private File getSdCardPath() {
        String[] possiblePath = new String[]{"/sdcard1", "/extSdCard", "extsdcard",
                "sdcard1"};
        String root = "/storage";
        for (String var : possiblePath) {
            File file = new File(root, var);
            if (file.exists()) return file;
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
            if (sdCardFile != null) {
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
