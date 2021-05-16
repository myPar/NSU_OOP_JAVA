package Detail;

public class DetailFactory {
    private int maxId;

    public Detail makeDetail(Detail.Type type) {
        maxId++;
        Detail detail = new Detail(type, maxId);
        return detail;
    }
}
