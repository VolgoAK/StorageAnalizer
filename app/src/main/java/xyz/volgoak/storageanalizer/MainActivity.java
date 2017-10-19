package xyz.volgoak.storageanalizer;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements OverviewFragment.OverviewFragListener{

    public static final String TAG = MainActivity.class.getSimpleName();

    String[] extensions = new String[]{"jpg", "png", "gif","jpeg"};

    FilesSorter mFilesSorter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFilesSorter = new FilesSorter();

        doSmtWithFiles(Environment.getExternalStorageDirectory());
        // TODO: 19.10.2017 remove this nasty hardcode
        doSmtWithFiles(new File("/storage/extSdCard"));

        ArrayList<FileList> fileLists = mFilesSorter.getCategoriesAsList();

        OverviewFragment fragment = OverviewFragment.newInstance(fileLists);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    private void doSmtWithFiles(File file){
//        File file = Environment.getExternalStorageDirectory();
        File[] files = file.listFiles();
        Queue<File> fileQueue = new LinkedList<>(Arrays.asList(files));

        while(!fileQueue.isEmpty()){
            File f = fileQueue.poll();
            if(f.isDirectory()){
                Collections.addAll(fileQueue, f.listFiles());
            }else{
                mFilesSorter.addFile(f);
            }
        }
    }

    @Override
    public void onCategorySelected(FileList category) {
        CategoryFragment fragment = CategoryFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}
