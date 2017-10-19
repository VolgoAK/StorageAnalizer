package xyz.volgoak.storageanalizer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.volgoak.storageanalizer.Recycler.CategoryRecyclerAdapter;


/**
 * Отображает категории файлов - "аудио", "видео" и т.д.
 */
public class OverviewFragment extends Fragment implements CategoryRecyclerAdapter.OnCategorySelectListener {

    public static final String PARAM_FILE_LISTS = "file_lists_param";

    private List<FileList> mFileLists;
    private OverviewFragListener mFragmentListener;


    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(ArrayList<FileList> fileLists) {

        Bundle args = new Bundle();
        args.putSerializable(PARAM_FILE_LISTS, fileLists);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFileLists = (List<FileList>) getArguments().getSerializable(PARAM_FILE_LISTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv_overview_frag);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(glm);

        CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(getContext(), mFileLists);
        adapter.setmCategorySelectListener(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OverviewFragListener) {
            mFragmentListener = (OverviewFragListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement OverviewFragListener");
        }
    }

    @Override
    public void onCategorySelected(FileList category) {
        mFragmentListener.onCategorySelected(category);
    }

    interface OverviewFragListener {
        void onCategorySelected(FileList category);
    }
}
