package tungnn.tutor.java.pattern.architecture_pattern.t1_mvc.p3_hmvc;

// --- CONTENT MODULE ---
class ContentModel {
    String text = "Danh sách sản phẩm hôm nay...";
}

class ContentView {
    void render(String data) {
        System.out.println("[Content Component]: " + data);
    }
}

class ContentController {
    private ContentModel model = new ContentModel();
    private ContentView view = new ContentView();

    public void display() {
        view.render(model.text);
    }
}
