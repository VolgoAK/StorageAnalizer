package xyz.volgoak.storageanalizer.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.volgoak.storageanalizer.FileList;
import xyz.volgoak.storageanalizer.R;
import xyz.volgoak.storageanalizer.Utils.FileSizeFormat;

/**
 * Created by Volgoak on 19.10.2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.VH> {

    private Context mContext;
    private List<FileList> mFileLists;
    private OnCategorySelectListener mCategorySelectListener;

    public CategoryRecyclerAdapter(Context context, List<FileList> fileLists) {
        mContext = context;
        mFileLists = fileLists;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_item_layout, parent, false);
        return new VH(root);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mFileLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mFileLists.size();
    }

    public void setmCategorySelectListener(OnCategorySelectListener CategorySelectListener) {
        this.mCategorySelectListener = CategorySelectListener;
    }

    private void onItemClick(int position){
        if(mCategorySelectListener != null){
            mCategorySelectListener.onCategorySelected(mFileLists.get(position));
        }
    }

    class VH extends RecyclerView.ViewHolder {
        View root;
        TextView titleTv;
        TextView countTv;
        TextView sizeTv;

        public VH(View root) {
            super(root);
            this.root = root;
            titleTv = (TextView) root.findViewById(R.id.tv_title_overview_item);
            countTv = (TextView) root.findViewById(R.id.tv_count_overview_item);
            sizeTv = (TextView) root.findViewById(R.id.tv_size_overview_item);
        }

        public void bind(FileList files) {
            titleTv.setText(files.getCategoryName());
            String fileCount = mContext.getString(R.string.files_count, files.size());
            countTv.setText(fileCount);
            String fileSize = FileSizeFormat.readableFileSize(files.getFilesSize());
            sizeTv.setText(fileSize);
            // TODO: 19.10.2017 move to constructor?
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnCategorySelectListener{
        void onCategorySelected(FileList files);
    }
}
