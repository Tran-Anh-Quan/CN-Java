package vn.edu.eaut.lab3;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private List<Student> list;
    private final String[] columnNames = {"Mã SV", "Họ Tên", "Điểm TB", "Xếp Loại"};

    public StudentTableModel() {
        this.list = new ArrayList<>();
    }

    public void addStudent(Student s) {
        list.add(s);
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
    }

    public void updateStudent(int index, Student s) {
        list.set(index, s);
        fireTableRowsUpdated(index, index);
    }

    public void removeStudent(int index) {
        list.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public Student getStudentAt(int index) {
        return list.get(index);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = list.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getId();
            case 1: return s.getName();
            case 2: return s.getDiemTrungBinh();
            case 3: return s.getXepLoai();
            default: return null;
        }
    }
}
