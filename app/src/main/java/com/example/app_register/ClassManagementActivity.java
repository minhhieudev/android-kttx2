package com.example.app_register;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ClassManagementActivity extends AppCompatActivity {
    private EditText edtClassCode, edtClassName, edtClassYear;
    private Button btnAddClass, btnDeleteClass, btnEditClass;
    private RecyclerView recyclerViewClasses;

    private FirebaseFirestore firestore;
    private ArrayList<Class> classList;
    private ClassAdapter classAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_management);

        firestore = FirebaseFirestore.getInstance();
        edtClassCode = findViewById(R.id.edtClassCode);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassYear = findViewById(R.id.edtClassYear);
        btnAddClass = findViewById(R.id.btnAddClass);
        btnDeleteClass = findViewById(R.id.btnDeleteClass);
        btnEditClass = findViewById(R.id.btnEditClass);
        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);

        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClasses.setAdapter(classAdapter);

        loadClasses();

        // Thiết lập sự kiện click cho từng item
        classAdapter.setOnItemClickListener(classItem -> {
            edtClassCode.setText(classItem.getMaLop());
            edtClassName.setText(classItem.getTenLop());
            edtClassYear.setText(classItem.getNienKhoa());
        });

        btnAddClass.setOnClickListener(v -> addClass());
        btnDeleteClass.setOnClickListener(v -> deleteClass());
        btnEditClass.setOnClickListener(v -> editClass());
    }

    private void loadClasses() {
        firestore.collection("lops").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                classList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Class classItem = document.toObject(Class.class);
                    classList.add(classItem);
                }
                classAdapter.notifyDataSetChanged();
            } else {
                Log.e("FirestoreError", "Error loading data", task.getException());
                Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addClass() {
        String maLop = edtClassCode.getText().toString();
        String tenLop = edtClassName.getText().toString();
        String nienKhoa = edtClassYear.getText().toString();

        if (maLop.isEmpty() || tenLop.isEmpty() || nienKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Class newClass = new Class(maLop, tenLop, nienKhoa);
        firestore.collection("lops").document(maLop).set(newClass)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                    loadClasses();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm lớp", Toast.LENGTH_SHORT).show());
    }

    private void deleteClass() {
        String maLop = edtClassCode.getText().toString();

        if (maLop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã lớp cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("lops").document(maLop).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Xóa lớp thành công", Toast.LENGTH_SHORT).show();
                    loadClasses();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa lớp", Toast.LENGTH_SHORT).show());
    }

    private void editClass() {
        String maLop = edtClassCode.getText().toString();
        String tenLop = edtClassName.getText().toString();
        String nienKhoa = edtClassYear.getText().toString();

        if (maLop.isEmpty() || tenLop.isEmpty() || nienKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("lops").document(maLop).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Mã lớp đã tồn tại -> Cập nhật lớp
                    Class updatedClass = new Class(maLop, tenLop, nienKhoa);
                    firestore.collection("lops").document(maLop).set(updatedClass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Cập nhật lớp thành công", Toast.LENGTH_SHORT).show();
                                loadClasses();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật lớp", Toast.LENGTH_SHORT).show());
                } else {
                    // Mã lớp chưa tồn tại -> Tạo mới lớp
                    Class newClass = new Class(maLop, tenLop, nienKhoa);
                    firestore.collection("lops").document(maLop).set(newClass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Thêm lớp mới thành công", Toast.LENGTH_SHORT).show();
                                loadClasses();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm lớp mới", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Lỗi khi kiểm tra mã lớp", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
