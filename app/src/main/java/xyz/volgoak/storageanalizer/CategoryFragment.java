package xyz.volgoak.storageanalizer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import xyz.volgoak.storageanalizer.Recycler.FilesRecyclerAdapter;


/**
 * Фрагмент отображает конкретную категорию файлов.
 */
public class CategoryFragment extends Fragment {

    public static final String FILES_LIST_PARAM = "files_list_param";

    private FileList mFilesList;

    public static CategoryFragment newInstance(FileList filesList) {

        Bundle args = new Bundle();
        args.putSerializable(FILES_LIST_PARAM, filesList);

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mFilesList = (FileList) getArguments().getSerializable(FILES_LIST_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv_category_fragment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        FilesRecyclerAdapter adapter = new FilesRecyclerAdapter(getContext(), mFilesList);
        rv.setAdapter(adapter);
    }
}
