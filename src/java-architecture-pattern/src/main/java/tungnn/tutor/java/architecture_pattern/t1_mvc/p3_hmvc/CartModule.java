package tungnn.tutor.java.architecture_pattern.t1_mvc.p3_hmvc;

// --- CART MODULE ---
class CartModel {
    int itemCount = 5;
}

class CartView {
    void render(int count) {
        System.out.println("[Cart Component]: Bạn có " + count + " sản phẩm trong giỏ.");
    }
}

class CartController {
    private CartModel model = new CartModel();
    private CartView view = new CartView();

    public void display() {
        view.render(model.itemCount);
    }
}
