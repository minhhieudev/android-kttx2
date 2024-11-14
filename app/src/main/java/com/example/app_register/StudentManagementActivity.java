package com.example.app_register;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class StudentManagementActivity extends AppCompatActivity {
    private EditText edtStudentCode, edtStudentName, edtStudentBirthYear, edtStudentPhone, edtStudentAddress;
    private Spinner spnClassCode;
    private Button btnAddStudent, btnDeleteStudent, btnEditStudent;
    private RecyclerView recyclerViewStudents;

    private FirebaseFirestore firestore;
    private ArrayList<Student> studentList;
    private ArrayList<String> classCodes;
    private StudentAdapter studentAdapter;
    private String selectedStudentId = null; // Biến lưu trữ ID sinh viên được chọn
    // Khai báo các biến cho tìm kiếm
    private EditText edtSearchKeyword;
    private Button btnSearchStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        firestore = FirebaseFirestore.getInstance();
        edtStudentCode = findViewById(R.id.edtStudentCode);
        edtStudentName = findViewById(R.id.edtStudentName);
        edtStudentBirthYear = findViewById(R.id.edtStudentBirthYear);
        edtStudentPhone = findViewById(R.id.edtStudentPhone);
        edtStudentAddress = findViewById(R.id.edtStudentAddress);
        spnClassCode = findViewById(R.id.spnClassCode);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
        btnEditStudent = findViewById(R.id.btnEditStudent);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);

        // Khởi tạo trường tìm kiếm và nút tìm kiếm
        edtSearchKeyword = findViewById(R.id.edtSearchStudent);
        btnSearchStudent = findViewById(R.id.btnSearchStudent);

        studentList = new ArrayList<>();
        classCodes = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentList, this::loadStudentDataForEdit);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(studentAdapter);

        loadClasses();
        loadStudents();

        btnAddStudent.setOnClickListener(v -> addStudent());
        btnDeleteStudent.setOnClickListener(v -> deleteStudent());
        btnEditStudent.setOnClickListener(v -> editStudent());

        // Xử lý sự kiện khi nhấn nút tìm kiếm
        btnSearchStudent.setOnClickListener(v -> searchStudent());
    }

    // Hàm tìm kiếm sinh viên theo mã hoặc tên
    private void searchStudent() {
        String keyword = edtSearchKeyword.getText().toString().trim().toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường
        if (keyword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("sinhviens")
                .get() // Tải tất cả sinh viên (tìm kiếm toàn bộ)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        studentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);

                            // Chuyển đổi dữ liệu từ Firestore thành chữ thường để so sánh
                            String studentCode = student.getStudentCode().toLowerCase(); // Chuyển mã sinh viên thành chữ thường
                            String studentName = student.getName().toLowerCase(); // Chuyển tên sinh viên thành chữ thường

                            // Kiểm tra nếu từ khóa tìm kiếm trùng với mã hoặc tên sinh viên (không phân biệt chữ hoa và thường)
                            if (studentCode.contains(keyword) || studentName.contains(keyword)) {
                                // Cập nhật thông tin lớp học và niên khóa cho sinh viên
                                updateStudentClassInfo(student);
                                studentList.add(student);
                            }
                        }
                        studentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tìm kiếm sinh viên", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error searching students", e);
                });
    }

    // Sử dụng callback để thông báo cho adapter sau khi dữ liệu lớp học đã được cập nhật.
    private void updateStudentClassInfo(Student student) {
        String maLop = student.getClassCode();
        firestore.collection("lops").document(maLop).get()
                .addOnCompleteListener(classTask -> {
                    if (classTask.isSuccessful() && classTask.getResult() != null) {
                        DocumentSnapshot classDoc = classTask.getResult();
                        String className = classDoc.getString("tenLop");
                        String nienKhoa = classDoc.getString("nienKhoa");

                        // Cập nhật thông tin lớp học và niên khóa cho sinh viên
                        student.setClassName(className);
                        student.setNienKhoa(nienKhoa);

                        // Thông báo cho adapter rằng dữ liệu đã được cập nhật
                        studentAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Lỗi khi tải dữ liệu lớp học cho sinh viên", classTask.getException());
                    }
                });
    }

    private void loadClasses() {
        firestore.collection("lops").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                classCodes.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String classCode = document.getId();
//                    String className = document.getString("className");
//                    String nienKhoa = document.getString("nienKhoa");

                    classCodes.add(classCode);

                    // Lưu tên lớp và niên khóa vào các đối tượng `Student` tương ứng
//                    for (Student student : studentList) {
//                        if (student.getClassCode().equals(classCode)) {
//                            student.setClassName(className);
//                            student.setNienKhoa(nienKhoa);
//                        }
//                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classCodes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnClassCode.setAdapter(adapter);
                studentAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
            } else {
                Log.e("FirestoreError", "Error loading classes", task.getException());
                Toast.makeText(this, "Lỗi tải dữ liệu lớp học", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadStudents() {
        firestore.collection("sinhviens").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                studentList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Student student = document.toObject(Student.class);

                    // Lấy mã lớp của sinh viên
                    String maLop = student.getClassCode();

                    // Truy vấn thông tin lớp từ bảng "lops" dựa vào classCode
                    firestore.collection("lops").document(maLop).get().addOnCompleteListener(classTask -> {
                        if (classTask.isSuccessful() && classTask.getResult() != null) {
                            DocumentSnapshot classDoc = classTask.getResult();
                            String className = classDoc.getString("tenLop");
                            String nienKhoa = classDoc.getString("nienKhoa");

                            // Cập nhật thông tin lớp cho sinh viên
                            student.setClassName(className);
                            student.setNienKhoa(nienKhoa);

                            // Thêm sinh viên đã cập nhật thông tin lớp vào danh sách
                            studentList.add(student);
                            studentAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirestoreError", "Error loading class data for student", classTask.getException());
                        }
                    });
                }
            } else {
                Log.e("FirestoreError", "Error loading students", task.getException());
                Toast.makeText(this, "Lỗi tải dữ liệu sinh viên", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addStudent() {
        String maSV = edtStudentCode.getText().toString();
        String ten = edtStudentName.getText().toString();
        String namSinh = edtStudentBirthYear.getText().toString();
        String soDienThoai = edtStudentPhone.getText().toString();
        String diaChi = edtStudentAddress.getText().toString();
        String maLop = spnClassCode.getSelectedItem().toString();

        if (maSV.isEmpty() || ten.isEmpty() || namSinh.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Student newStudent = new Student(maSV, ten, namSinh, soDienThoai, diaChi, maLop);
        firestore.collection("sinhviens").document(maSV).set(newStudent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                    loadStudents();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm sinh viên", Toast.LENGTH_SHORT).show());
    }

    private void deleteStudent() {
        if (selectedStudentId == null) {
            Toast.makeText(this, "Vui lòng chọn sinh viên để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("sinhviens").document(selectedStudentId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                    selectedStudentId = null;
                    loadStudents();
                    clearInputFields();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa sinh viên", Toast.LENGTH_SHORT).show());
    }

    private void editStudent() {
        String maSV = edtStudentCode.getText().toString();
        String ten = edtStudentName.getText().toString();
        String namSinh = edtStudentBirthYear.getText().toString();
        String soDienThoai = edtStudentPhone.getText().toString();
        String diaChi = edtStudentAddress.getText().toString();
        String maLop = spnClassCode.getSelectedItem().toString();

        if (maSV.isEmpty() || ten.isEmpty() || namSinh.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Student updatedStudent = new Student(maSV, ten, namSinh, soDienThoai, diaChi, maLop);
        firestore.collection("sinhviens").document(maSV).set(updatedStudent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
                    loadStudents();
                    selectedStudentId = null;
                    clearInputFields();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật sinh viên", Toast.LENGTH_SHORT).show());
    }

    // Hàm load thông tin sinh viên lên các ô nhập để chỉnh sửa
    private void loadStudentDataForEdit(Student student) {
        selectedStudentId = student.getStudentCode();
        edtStudentCode.setText(student.getStudentCode());
        edtStudentName.setText(student.getName());
        edtStudentBirthYear.setText(student.getBirthYear());
        edtStudentPhone.setText(student.getPhone());
        edtStudentAddress.setText(student.getAddress());

        // Đặt giá trị mã lớp trong Spinner dựa trên mã lớp của sinh viên
        int classPosition = classCodes.indexOf(student.getClassCode());
        if (classPosition >= 0) {
            spnClassCode.setSelection(classPosition);
        }
    }

    // Hàm xóa sạch các trường nhập liệu
    private void clearInputFields() {
        edtStudentCode.setText("");
        edtStudentName.setText("");
        edtStudentBirthYear.setText("");
        edtStudentPhone.setText("");
        edtStudentAddress.setText("");
        spnClassCode.setSelection(0);
    }
}
