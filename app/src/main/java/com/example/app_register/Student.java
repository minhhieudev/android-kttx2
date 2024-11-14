package com.example.app_register;

public class Student {
    private String studentCode;
    private String name;
    private String birthYear;
    private String phone;
    private String address;
    private String classCode;
    private String className;
    private String nienKhoa;

    public Student() { }

    public Student(String studentCode, String name, String birthYear, String phone, String address, String classCode) {
        this.studentCode = studentCode;
        this.name = name;
        this.birthYear = birthYear;
        this.phone = phone;
        this.address = address;
        this.classCode = classCode;
    }

    // Getter và Setter
    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthYear() { return birthYear; }
    public void setBirthYear(String birthYear) { this.birthYear = birthYear; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }
}
