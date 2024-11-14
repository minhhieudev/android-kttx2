package com.example.app_register;

// StudentAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private ArrayList<Student> studentList;
    private OnStudentClickListener listener;

    public StudentAdapter(ArrayList<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentCode.setText("Mã SV: " + student.getStudentCode());
        holder.tvStudentName.setText("Tên: " + student.getName());
        holder.tvStudentBirthYear.setText("Năm sinh: " + student.getBirthYear());
        holder.tvStudentPhone.setText("Số điện thoại: " + student.getPhone());
        holder.tvStudentAddress.setText("Địa chỉ: " + student.getAddress());
        holder.tvStudentClassCode.setText("Mã lớp: " + student.getClassCode());
        holder.tvStudentClassName.setText("Tên lớp: " + student.getClassName());
        holder.tvStudentNienKhoa.setText("Niên khóa: " + student.getNienKhoa());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStudentClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentCode,tvStudentNienKhoa, tvStudentName, tvStudentBirthYear, tvStudentPhone, tvStudentAddress, tvStudentClassCode, tvStudentClassName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentCode = itemView.findViewById(R.id.tvStudentCode);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentBirthYear = itemView.findViewById(R.id.tvStudentBirthYear);
            tvStudentPhone = itemView.findViewById(R.id.tvStudentPhone);
            tvStudentAddress = itemView.findViewById(R.id.tvStudentAddress);
            tvStudentClassCode = itemView.findViewById(R.id.tvStudentClassCode);
            tvStudentNienKhoa = itemView.findViewById(R.id.tvStudentNienKhoa);
            tvStudentClassName = itemView.findViewById(R.id.tvStudentClassName);
        }
    }
}
