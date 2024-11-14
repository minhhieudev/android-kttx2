package com.example.app_register;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private ArrayList<Class> classList;
    private OnItemClickListener listener;

    public ClassAdapter(ArrayList<Class> classList) {
        this.classList = classList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class classItem = classList.get(position);
        holder.tvClassCode.setText(classItem.getMaLop());
        holder.tvClassName.setText(classItem.getTenLop());
        holder.tvClassYear.setText(classItem.getNienKhoa());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(classItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassCode, tvClassName, tvClassYear;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassCode = itemView.findViewById(R.id.tvClassCode);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassYear = itemView.findViewById(R.id.tvClassYear);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Class classItem);
    }
}
