/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:22
 * @description
 **/
public class Main {

    public static void main(String[] args) {
        Thread thread = new Thread(new HttpServer());
        thread.start();
    }
}
