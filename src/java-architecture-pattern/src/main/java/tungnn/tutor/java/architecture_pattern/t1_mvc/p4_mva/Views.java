package tungnn.tutor.java.architecture_pattern.t1_mvc.p4_mva;

// View dành cho giáo viên/quản lý
class AdminStudentView {
    void renderFullDetails(String name, double score) {
        System.out.println("[ADMIN] Sinh viên: " + name + " | Điểm số: " + score);
    }
}

// View dành cho phụ huynh (Chỉ quan tâm xếp loại)
class ParentStudentView {
    void displayGrade(String name, String rank) {
        System.out.println("[PARENT] Thông báo: Con " + name + " xếp loại: " + rank);
    }
}

// View dành cho kỹ thuật (Định dạng dữ liệu)
class JsonExportView {
    void export(String json) {
        System.out.println("[SYSTEM] Exporting JSON: " + json);
    }
}