package xyz.volgoak.storageanalizer.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import xyz.volgoak.storageanalizer.FileList;
import xyz.volgoak.storageanalizer.R;
import xyz.volgoak.storageanalizer.Utils.FileSizeFormat;

/**
 * Created by Volgoak on 19.10.2017.
 */

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.VH> {

    private FileList mFiles;
    private Context mContext;

    public FilesRecyclerAdapter(Context context, FileList files){
        mFiles = files;
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new VH(root);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    class VH extends RecyclerView.ViewHolder {
        View root;
        TextView titleTv;
        TextView pathTv;
        TextView sizeTv;

        public VH(View root) {
            super(root);
            this.root = root;
            titleTv = (TextView) root.findViewById(R.id.tv_title_category_item);
            pathTv = (TextView) root.findViewById(R.id.tv_count_category_item);
            sizeTv = (TextView) root.findViewById(R.id.tv_size_category_item);
        }

        public void bind(File file) {
            titleTv.setText(file.getName());
            // TODO: 19.10.2017 change this shit
            String path = "path";
            try{
               path = file.getCanonicalPath();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            pathTv.setText(path);
            String size = FileSizeFormat.readableFileSize(file.length());
            sizeTv.setText(size);
        }
    }
}
