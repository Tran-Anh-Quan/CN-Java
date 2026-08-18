package vn.edu.eaut.lab7.model;

public class LopHoc {
    private String classId;
    private String className;
    private String advisor;
    private int studentCount;

    public LopHoc() {}

    public LopHoc(String classId, String className, String advisor, int studentCount) {
        this.classId = classId;
        this.className = className;
        this.advisor = advisor;
        this.studentCount = studentCount;
    }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getAdvisor() { return advisor; }
    public void setAdvisor(String advisor) { this.advisor = advisor; }
    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
}
